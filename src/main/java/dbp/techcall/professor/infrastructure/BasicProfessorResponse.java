package dbp.techcall.professor.infrastructure;

public interface BasicProfessorResponse {

    public Long getId();

    public String getFirstName();

    public String getLastName();

    public String getDescription();

    public Double getPricePerHour();

    public Double getRating();

    public Integer getReviewCount();
}
