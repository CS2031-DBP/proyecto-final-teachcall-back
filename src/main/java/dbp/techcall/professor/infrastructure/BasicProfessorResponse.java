package dbp.techcall.professor.infrastructure;

public interface BasicProfessorResponse {

    public Long getId();

    public String getFirstName();

    public String getLastName();

    public String getDescription();

    public Double getpricePerHour();

    public Double getRating();

    public Integer getReviewCount();
}
