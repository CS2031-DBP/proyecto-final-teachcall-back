package dbp.techcall.professorAvailability.domain;

import dbp.techcall.professorAvailability.dto.SlotAvailability;
import org.aspectj.weaver.patterns.ConcreteCflowPointcut;
import org.springframework.data.domain.Sort;
import org.springframework.data.util.Pair;
import dbp.techcall.professor.domain.Professor;
import dbp.techcall.professor.domain.ProfessorService;
import dbp.techcall.professorAvailability.dto.DayTimeSlotsResponse;
import dbp.techcall.professorAvailability.dto.NextFourWeeksAvailabilityResponse;
import dbp.techcall.professorAvailability.dto.WeekAvailabilityRequest;
import dbp.techcall.professorAvailability.exceptions.UnsetAvailabilityException;
import dbp.techcall.professorAvailability.infrastructure.ProfessorAvailabilityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.temporal.ChronoField;
import java.time.temporal.ChronoUnit;
import java.util.*;


@Service
public class ProfessorAvailabilityService {
    @Autowired
    private ProfessorAvailabilityRepository professorAvailabilityRepository;

    @Autowired
    private ProfessorService professorService;


    /**
     * This method calculates the number of the current week corresponding to the current year and find every availability slot in the following 4 weeks including the current week
     * @param professorId
     *      A Long used to find the professor and its related Availabilities
     *
     *
     * @throws UnsetAvailabilityException If the professor has not yet defined his or her availability for any week
     * @throws RuntimeException If the professorId does not correspond to an existing professor
     * @return A DTO containing the professorId and a Map whose keys are the number of the next 4 weeks and the days available for those weeks.
     *      <pre>
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
     *
     */
    public NextFourWeeksAvailabilityResponse getNextFourWeeksAvailability(Long professorId) {
        Professor professor = professorService.findById(professorId);

        if (professor == null) {
            throw new RuntimeException("Professor not found");
        }

        List<List<ProfessorAvailability>> professorAvailabilityList = new ArrayList<>();

        for (int i = 0; i < 4; i++) {
            LocalDate date = LocalDate.now();
            int weekOfYear = date.get(ChronoField.ALIGNED_WEEK_OF_YEAR);

            professorAvailabilityList
                    .add(professorAvailabilityRepository.findByProfessorIdAndWeekNumber(professorId, weekOfYear + 1));
        }

        if (professorAvailabilityList.isEmpty()) {
            throw new UnsetAvailabilityException("Professor does not have any availability set yet");
        }

        NextFourWeeksAvailabilityResponse weekAvailability = new NextFourWeeksAvailabilityResponse();

        weekAvailability.setProfessorId(professorId);
        weekAvailability.setWeekAvailability(new HashMap<>());

        for(List<ProfessorAvailability> week : professorAvailabilityList){
            if (week.isEmpty()) {
                continue;
            }
            weekAvailability.getWeekAvailability().put(week.get(0).getWeekNumber(), new HashSet<>());

            for(ProfessorAvailability day : week){
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
     * @param professorId
     *      A Long used to find the professor and its related Availabilities
     * @param week
     *      An Integer corresponding to the week number from which we want to fetch the data.
     * @param day
     *      An integer corresponding to the specific day of the week (0-6) for which we want to know the availability.
     * @throws UnsetAvailabilityException If the professor has not yet defined his or her availability for any week
     * @throws RuntimeException If the professorId does not correspond to an existing professor
     * @return Up to 13 tuples corresponding to each one-hour interval in the teacher's range of availability sorted by start time.
     *     <pre>
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
     *
     */
    public DayTimeSlotsResponse getAvailabilityByDay(Long professorId, Integer week, Integer day) {
        Professor professor = professorService.findById(professorId);

        if( professor == null) throw new RuntimeException("Professor not found");

        Sort sort = Sort.by(Sort.Direction.ASC, "startTime");

        List<ProfessorAvailability> professorAvailabilities = professorAvailabilityRepository
                .findByProfessorIdAndWeekNumberAndDay( professorId, week, day, sort);

        if (professorAvailabilities.isEmpty()) throw new UnsetAvailabilityException("Professor does not have daily schedule yet");


        DayTimeSlotsResponse response = new DayTimeSlotsResponse();
        response.setProfessorId(professorId);

        Map<Integer, List<SlotAvailability>> dayTimeSlots = new HashMap<>();

        for(ProfessorAvailability professorAvailability : professorAvailabilities){
            if(!dayTimeSlots.containsKey(day))  dayTimeSlots.put(day, new ArrayList<>());

            SlotAvailability slotAvailability = new SlotAvailability();

            slotAvailability.setStartTime(professorAvailability.getStartTime());
            slotAvailability.setEndTime(professorAvailability.getEndTime());
            slotAvailability.setIsAvailable(professorAvailability.getIsAvailable());

            dayTimeSlots.get(day).add(slotAvailability);
        }

        response.setDayTimeSlots(dayTimeSlots);
        return response;
    }


    /**
     *
     * @param request
     *      A DTO containing the professorId, the weekNumber and a Map whose keys are the days of the week and the values are the time ranges in which the professor is available.
     *      The time ranges are represented by a Pair of LocalTime objects, the first one representing the start time and the second one representing the end time.
     *      <pre>
     * Example:<br>
     * {<br>
     * "professorId": 1,<br>
     * "weekNumber": 1<br>
     * "timeRanges": {<br>
     *      "1": {<br>
     *          "first": "08:00",<br>
     *          "second": "12:00"<br>
     *           },<br>
     *      "2": {<br>
     *          "first": "08:00",<br>
     *          "second": "12:00"<br>
     *           },<br>
     *      "3": {<br>
     *          "first": "08:00",<br>
     *          "second": "12:00"<br>
     *         }<br>
     *      }<br>
     * }<br>
     * </pre>
     *
     * @throws RuntimeException If the professorId does not correspond to an existing professor or the start time is after the end time or the start time is before 6:00 or the end time is after 22:00
     */
    public void setAvailabilityByWeekNumber(WeekAvailabilityRequest request) {
        Professor professor = professorService.findById(request.getProfessorId());

        if (professor == null) {
            throw new RuntimeException("Professor not found");
        }

        for (Map.Entry<Integer, Pair<LocalTime, LocalTime>> entry : request.getTimeRanges().entrySet()) {

            if(entry.getValue().getFirst().isAfter(entry.getValue().getSecond())) throw new RuntimeException("Start time cannot be after end time" );
            if(entry.getValue().getFirst().isBefore(LocalTime.of(6,0))) throw new RuntimeException("Start time cannot be before 6:00" );
            if(entry.getValue().getSecond().isAfter(LocalTime.of(22,0))) throw new RuntimeException("End time cannot be after 22:00" );

            LocalTime startTime = entry.getValue().getFirst();
            LocalTime endTime = entry.getValue().getSecond();

            long oneHourSlots = ChronoUnit.MINUTES.between(startTime, endTime) / 60;

            Integer weekNumber = request.getWeekNumber();

            for(int iSlot = 0; iSlot < oneHourSlots ;iSlot++){
                ProfessorAvailability professorAvailability = new ProfessorAvailability();

                professorAvailability.setProfessor(professor);
                professorAvailability.setDay(entry.getKey());
                professorAvailability.setStartTime(startTime.plusHours(iSlot));
                professorAvailability.setEndTime(startTime.plusHours((iSlot + 1)));
                professorAvailability.setWeekNumber(weekNumber);
                professorAvailabilityRepository.save(professorAvailability);
            }
        }
    }

}
