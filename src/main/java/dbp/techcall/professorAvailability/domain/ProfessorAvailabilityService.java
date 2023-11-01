package dbp.techcall.professorAvailability.domain;

import org.springframework.data.util.Pair;
import dbp.techcall.professor.domain.Professor;
import dbp.techcall.professor.domain.ProfessorService;
import dbp.techcall.professorAvailability.dto.DayTimeSlotsResponse;
import dbp.techcall.professorAvailability.dto.NextFourWeeksAvailabilityResponse;
import dbp.techcall.professorAvailability.dto.WeekAvailabilityRequest;
import dbp.techcall.professorAvailability.exceptions.UnsetAvailabilityException;
import dbp.techcall.professorAvailability.infrastructure.ProfessorAvailabilityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.temporal.ChronoField;
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

            professorAvailabilityList.add(professorAvailabilityRepository.findByProfessorIdAndWeekNumber(professorId, weekOfYear + 1));

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

    public void setAvailabilityByWeekNumber(WeekAvailabilityRequest request) {
        Professor professor = professorService.findById(request.getProfessorId());

        if (professor == null) {
            throw new RuntimeException("Professor not found");
        }

        for (Map.Entry<Integer, Pair<LocalTime, LocalTime>> entry : request.getTimeRanges().entrySet()) {
            ProfessorAvailability professorAvailability = new ProfessorAvailability();

            professorAvailability.setProfessor(professor);
            professorAvailability.setDay(entry.getKey());
            professorAvailability.setStartTime(entry.getValue().getFirst());
            professorAvailability.setEndTime(entry.getValue().getSecond());
            professorAvailability.setWeekNumber(request.getWeekNumber());

            professorAvailabilityRepository.save(professorAvailability);
        }
    }

    public DayTimeSlotsResponse getAvailabilityByDay(Long professorId, Integer week, Integer day) {
        Professor professor = professorService.findById(professorId);

        if( professor == null) throw new RuntimeException("Professor not found");

        List<ProfessorAvailability> professorAvailabilities = professorAvailabilityRepository
                .findByProfessorIdAndWeekNumberAndDay(
                        professor,
                        week,
                        day
                );

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
                            )
                    );
        }

        response.setDayTimeSlots(dayTimeSlots);
        return response;
    }
}
