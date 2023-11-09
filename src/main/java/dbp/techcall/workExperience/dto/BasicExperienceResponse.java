package dbp.techcall.workExperience.dto;

import java.time.LocalDate;

public interface BasicExperienceResponse {
    String getTitle();
    String getEmployer();
    String getDescription();
    LocalDate getStartDate();
    LocalDate getEndDate();
}
