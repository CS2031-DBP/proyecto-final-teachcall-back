package dbp.techcall.professorAvailability.domain;

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

    public DayTimeSlotsResponse getAvailabilityByDay(Long professorId, Integer week, Integer day) {
        Professor professor = professorService.findById(professorId);

        if( professor == null) throw new RuntimeException("Professor not found");

        List<ProfessorAvailability> professorAvailabilities = professorAvailabilityRepository
                .findByProfessorIdAndWeekNumberAndDay( professor, week, day);

        if (professorAvailabilities.isEmpty()) throw new UnsetAvailabilityException("Professor does not have daily schedule yet");


        DayTimeSlotsResponse response = new DayTimeSlotsResponse();
        response.setProfessorId(professorId);

        Map<Integer, List<Pair<LocalTime,LocalTime>>> dayTimeSlots = new HashMap<>();

        for(ProfessorAvailability professorAvailability : professorAvailabilities){
            if(!dayTimeSlots.containsKey(day))  dayTimeSlots.put(day, new ArrayList<>());

            dayTimeSlots.get(professorAvailability.getDay())
                    .add(Pair.of(
                            professorAvailability.getStartTime(),
                            professorAvailability.getEndTime()
                    ));
        }

        response.setDayTimeSlots(dayTimeSlots);
        return response;
    }

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
