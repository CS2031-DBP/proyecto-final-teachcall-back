package dbp.techcall.professorAvailability.dto;


import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.util.Map;
import java.util.Set;

@Getter
@Setter
@RequiredArgsConstructor
public class NextFourWeeksAvailabilityResponse {

    private Long professorId;
    private Map<Integer, Set<Integer>> weekAvailability;
}
