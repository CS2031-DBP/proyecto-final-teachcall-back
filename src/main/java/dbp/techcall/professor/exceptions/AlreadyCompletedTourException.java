package dbp.techcall.professor.exceptions;

public class AlreadyCompletedTourException extends RuntimeException {
    public AlreadyCompletedTourException(String professorAlreadyCompletedTour) {
        super(professorAlreadyCompletedTour);
    }
}
