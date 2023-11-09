package dbp.techcall.workExperience.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@RequiredArgsConstructor
public class BasicExperienceRequest {
    private String title;
    private String employer;
    private String description;
    private LocalDate startDate;
    private LocalDate endDate;
}
