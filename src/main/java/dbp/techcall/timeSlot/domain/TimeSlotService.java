package dbp.techcall.timeSlot.domain;

import dbp.techcall.booking.domain.Booking;
import dbp.techcall.booking.dto.BookingInfo;
import dbp.techcall.professor.domain.Professor;
import dbp.techcall.professor.domain.ProfessorService;
import dbp.techcall.review.exceptions.ResourceNotFoundException;
import dbp.techcall.timeSlot.dto.*;
import dbp.techcall.timeSlot.exceptions.UnsetAvailabilityException;
import dbp.techcall.timeSlot.infrastructure.TimeSlotRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.temporal.*;
import java.util.*;
import java.time.DayOfWeek;
import java.time.LocalDate;


@Service
public class TimeSlotService {

    @Autowired
    private TimeSlotRepository timeSlotRepository;

    @Autowired
    private ProfessorService professorService;


    /**
     * This method calculates the number of the current week corresponding to the current year
     * and find every availability slot in the following 4 week including the current week
     *
     * @param professorId A Long used to find the professor and its related Availabilities
     * @return A DTO containing the professorId and a Map whose keys are the number of the next 4 weeks and the days available for those weeks.
     * <pre>
     * Example:<br>
     * {<br>
     *      "professorId": 1,<br>
     *      "weekAvailability": {<br>
     *      "1": [ 1, 2, 3, 4, 5, 6],<br>
     *      "2": [ 1,3,6],<br>
     *      "4": [ 2, 3, 5, 6],<br>
     *      }<br>
     *  }<br>
     *      </pre>
     * @throws UnsetAvailabilityException If the professor has not yet defined his or her availability for any week
     * @throws RuntimeException           If the professorId does not correspond to an existing professor
     */
    public NextFourWeeksAvailabilityResponse getNextFourWeeksAvailability(Long professorId) {
        Professor professor = professorService.findById(professorId);

        if (professor == null) {
            throw new RuntimeException("Professor not found");
        }

        List<List<TimeSlot>> timeSlotList = new ArrayList<>();

        for (int i = 0; i < 4; i++) {
            LocalDate date = LocalDate.now();
            int weekOfYear = date.get(ChronoField.ALIGNED_WEEK_OF_YEAR);

            timeSlotList
                    .add(timeSlotRepository.findByProfessorIdAndWeekNumber(professorId, weekOfYear + 1));
        }

        if (timeSlotList.isEmpty()) {
            throw new UnsetAvailabilityException("Professor does not have any availability set yet");
        }

        NextFourWeeksAvailabilityResponse weekAvailability = new NextFourWeeksAvailabilityResponse();

        weekAvailability.setProfessorId(professorId);
        weekAvailability.setWeekAvailability(new HashMap<>());

        for (List<TimeSlot> week : timeSlotList) {
            if (week.isEmpty()) {
                continue;
            }
            weekAvailability.getWeekAvailability().put(week.get(0).getWeekNumber(), new HashSet<>());

            for (TimeSlot day : week) {
                weekAvailability
                        .getWeekAvailability()
                        .get(day.getWeekNumber())
                        .add(day.getDay());
            }
        }

        return weekAvailability;

    }

    /**
     * This method finds every availability slot for a specific day of the week in a specific week.
     *
     * @param email The email of the professor whose availability we want to fetch.
     * @param week  An Integer corresponding to the week number from which we want to fetch the data.
     * @param day   An integer corresponding to the specific day of the week (0-6) for which we want to know the availability.
     * @return Up to 13 tuples corresponding to each one-hour interval in the teacher's range of availability sorted by start time.
     * <pre>
     *         Example:<br>
     *         {<br>
     *          "professorId": 1,<br>
     *         "dayTimeSlots": {<br>
     *              "1": [<br>
     *                  {<br>
     *                  "startTime": "08:00",<br>
     *                  "endTime": "09:00",<br>
     *                  "isAvailable": true<br>
     *                  },<br>
     *                  {<br>
     *                  "startTime": "09:00",<br>
     *                  "endTime": "10:00",<br>
     *                  "isAvailable": true<br>
     *                  },<br>
     *                  {<br>
     *                  "startTime": "10:00",<br>
     *                  "endTime": "11:00",<br>
     *                  "isAvailable": true<br>
     *                  }<br>
     *                  ]<br>
     *              }<br>
     *         }<br>
     *         </pre>
     * @throws UnsetAvailabilityException If the professor has not yet defined his or her availability for any week
     * @throws RuntimeException           If the professorId does not correspond to an existing professor object
     */
    public DayTimeSlotsResponse getAvailabilityByDay(String email, Integer week, Integer day) {
        Professor professor = professorService.findByEmail(email);

        if (professor == null) throw new RuntimeException("Professor not found");

        Sort sort = Sort.by(Sort.Direction.ASC, "startTime");

        List<TimeSlot> professorAvailabilities = timeSlotRepository
                .findByProfessorIdAndWeekNumberAndDay(professor.getId(), week, day, sort);

        if (professorAvailabilities.isEmpty())
            throw new UnsetAvailabilityException("Professor does not have daily schedule yet");


        DayTimeSlotsResponse response = new DayTimeSlotsResponse();
        response.setProfessorId(professor.getId());

        Map<Integer, List<SlotAvailability>> dayTimeSlots = new HashMap<>();

        for (TimeSlot timeSlot : professorAvailabilities) {
            if (!dayTimeSlots.containsKey(day)) dayTimeSlots.put(day, new ArrayList<>());

            SlotAvailability slotAvailability = new SlotAvailability();

            slotAvailability.setSlotId(timeSlot.getId());
            slotAvailability.setStartTime(timeSlot.getStartTime());
            slotAvailability.setEndTime(timeSlot.getEndTime());
            slotAvailability.setIsAvailable(timeSlot.getIsAvailable());

            dayTimeSlots.get(day).add(slotAvailability);
        }

        response.setDayTimeSlots(dayTimeSlots);
        return response;
    }


    /**
     * This method sets the availability of a professor for a specific week.
     * Calculates the number of one-hour slots between the start time and the end time and creates a ProfessorAvailability object for each one of them.
     *
     * @param request A DTO containing the professorId, the weekNumber and a Map whose keys are the days of the week and the values are the time ranges in which the professor is available.
     *                The time ranges are represented by a Pair of LocalTime objects, the first one representing the start time and the second one representing the end time.
     *                <pre>
     *                Example:<br>
     *                {<br>
     *                    "professorId": 1653,<br>
     *                    "weekNumber": 45,<br>
     *                    "timeRanges": {<br>
     *                        "1": {<br>
     *                            "startTime": "09:00:00",<br>
     *                            "endTime": "10:00:00"<br>
     *                        },<br>
     *                        "2": {<br>
     *                            "startTime": "09:00:00",<br>
     *                            "endTime": "10:00:00"<br>
     *                        },<br>
     *                        "3": {<br>
     *                            "startTime": "09:00:00",<br>
     *                            "endTime": "10:00:00"<br>
     *                        },<br>
     *                        "4": {<br>
     *                            "startTime": "09:00:00",<br>
     *                            "endTime": "10:00:00"<br>
     *                        }<br>
     *                    }<br>
     *                }<br>
     *                </pre>
     * @throws RuntimeException If the professorId does not correspond to an existing professor or the start time is after the end time or the start time is before 6:00 or the end time is after 22:00
     * @apiNote Take into account the restrictions on the start and end time and its formats.
     * The start time must be before the end time and the start time must be after 8:00 and the end time must be before 21:00
     * See this <a href="https://docs.oracle.com/javase/8/docs/api/java/time/format/DateTimeFormatter.html"> link </a> for more information on the accepted formats
     */
    public void setAvailabilityByWeekNumber(WeekAvailabilityRequest request) {
        Professor professor = professorService.findByEmail(request.getProfessorEmail());

        if (professor == null) {
            throw new RuntimeException("Professor not found");
        }

        for (Map.Entry<Integer, TimeRange> entry : request.getTimeRanges().entrySet()) {

            LocalTime startTime = LocalTime.parse(entry.getValue().getFirst());
            LocalTime endTime = LocalTime.parse(entry.getValue().getSecond());

            System.out.println("\n\nCREANDO UN TIME SLOT CON");
            System.out.println("startTime:" + startTime);
            System.out.println("endTime" + endTime);
            System.out.println("para el día " + getDayOfWeek(entry.getKey()));

            System.out.println("Para una currentWeek calculda :" + LocalDate.now().get(WeekFields.ISO.weekOfWeekBasedYear()));

            if (startTime.isAfter(endTime)) throw new RuntimeException("Start time cannot be after end time");
            if (startTime.isBefore(LocalTime.of(6, 0))) throw new RuntimeException("Start time cannot be before 6:00");
            if (endTime.isAfter(LocalTime.of(22, 0))) throw new RuntimeException("End time cannot be after 22:00");


            long oneHourSlots = ChronoUnit.MINUTES.between(startTime, endTime) / 60;

            Integer weekNumber = request.getWeekNumber();

            System.out.println("weekNumber: " + weekNumber);

            for (int iSlot = 0; iSlot < oneHourSlots; iSlot++) {
                TimeSlot timeSlot = new TimeSlot();

                LocalDate currentDate = LocalDate.now();
                LocalDate firstDayOfWeek = currentDate.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));
                int woy =currentDate.get(WeekFields.ISO.weekOfWeekBasedYear());

                LocalDate adjustedDate = firstDayOfWeek.plusDays((weekNumber-woy) * 7L + (entry.getKey() - 1));


                System.out.println("LocalDate.now(): " + currentDate);
                System.out.println("Adjusted Date inicial: " + adjustedDate);

                System.out.println("Finalmente crea la adjusted Date a:" + adjustedDate);
                timeSlot.setDate(adjustedDate);
                timeSlot.setProfessor(professor);
                timeSlot.setDay(entry.getKey());
                timeSlot.setStartTime(startTime.plusHours(iSlot));
                timeSlot.setEndTime(startTime.plusHours(iSlot + 1));
                timeSlot.setWeekNumber(weekNumber);
                timeSlot.setIsAvailable(true);
                timeSlotRepository.save(timeSlot);
            }
        }
    }

    /**
     * This method finds every available day in a given week for a specific professor.
     * <p>
     * The request url should be in the following format: <br>
     * <pre>
     * <code>[GET] /professor-availability/weekly/{professorId}?week={weekNumber}</code>
     * </pre>
     *
     * @param email The email of the professor whose availability we want to fetch.
     * @param week  The week number should be provided as a query parameter.
     * @return A DTO containing  a list of available days.
     * <pre>
     *     Example:<br>
     *     { "availableDays": [ 1, 2, 3, 4, 5, 6] }<br>
     * </pre>
     * @throws RuntimeException If the professorId does not correspond to an existing professor object
     */
    public WeekAvailabilityResponse getAvailabilityByWeekNumber(String email, Integer week) {
        Professor professor = professorService.findByEmail(email);

        if (professor == null) {
            throw new RuntimeException("Professor not found");
        }

        List<BasicDayAvailability> professorAvailabilities = timeSlotRepository
                .findByProfessorIdAndWeekNumber(professor.getId(), week);

        if (professorAvailabilities.isEmpty()) {
            throw new UnsetAvailabilityException("Professor does not have any availability set yet");
        }

        WeekAvailabilityResponse response = new WeekAvailabilityResponse();
        response.setAvailableDays(new HashSet<>());

        for (BasicDayAvailability timeSlot : professorAvailabilities) {
            response.getAvailableDays().add(timeSlot.getDay());
        }

        return response;
    }

    private static DayOfWeek getDayOfWeek(int day) {
        switch (day) {
            case 1:
                return DayOfWeek.MONDAY;
            case 2:
                return DayOfWeek.TUESDAY;
            case 3:
                return DayOfWeek.WEDNESDAY;
            case 4:
                return DayOfWeek.THURSDAY;
            case 5:
                return DayOfWeek.FRIDAY;
            case 6:
                return DayOfWeek.SATURDAY;
            default:
                throw new IllegalArgumentException("Invalid day value. It should be between 1 and 6.");
        }
    }

    public List<BasicDayAvailability> getFreeTimeSlots(Long id, Integer week, Integer day) {
        List<BasicDayAvailability> timeSlots = timeSlotRepository.findByProfessorIdAndWeekNumberAndDay(id, week, day);

        List<BasicDayAvailability> freeTimeSlots = new ArrayList<>();

        for (BasicDayAvailability timeSlot : timeSlots) {
            if (timeSlot.getIsAvailable()) {
                freeTimeSlots.add(timeSlot);
            }
        }
        return freeTimeSlots;

    }

    public WeekAvailabilityResponse getAvailabilityByProfessorIdAndWeekNumber(Long id, Integer week) {
        Professor professor = professorService.findById(id);

        if (professor == null) {
            throw new RuntimeException("Professor not found");
        }

        List<BasicDayAvailability> professorAvailabilities = timeSlotRepository
                .findByProfessorIdAndWeekNumber(professor.getId(), week);

        if (professorAvailabilities.isEmpty()) {
            throw new UnsetAvailabilityException("Professor does not have any availability set yet");
        }

        WeekAvailabilityResponse response = new WeekAvailabilityResponse();
        response.setAvailableDays(new HashSet<>());

        for (BasicDayAvailability timeSlot : professorAvailabilities) {
            response.getAvailableDays().add(timeSlot.getDay());
        }

        return response;
    }

    public BookingInfo getBookingInfo(Long timeSlotId) {
        TimeSlot timeSlot = timeSlotRepository.findById(timeSlotId).orElse(null);

        if (timeSlot == null) {
            throw new RuntimeException("TimeSlot not found");
        }

        Booking booking = timeSlot.getBooking();

        if (booking == null) {
            throw new ResourceNotFoundException("Booking not found");
        }

        return new BookingInfo(booking);
    }
}
