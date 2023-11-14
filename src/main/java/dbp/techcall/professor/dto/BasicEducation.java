package dbp.techcall.professor.dto;

import java.time.LocalDate;

public interface BasicEducation {
    public LocalDate getStartDate();
    public LocalDate getEndDate();
    public String getSchoolName();
    public String getDegree();
}
