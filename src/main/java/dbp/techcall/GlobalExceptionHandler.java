package dbp.techcall;

import dbp.techcall.auth.exceptions.UserAlreadyExistsException;
import dbp.techcall.booking.exceptions.BookingNotFoundException;
import dbp.techcall.professor.exceptions.AlreadyCompletedTourException;
import dbp.techcall.professorAvailability.exceptions.AvailabilityDatesErrorException;
import dbp.techcall.professorAvailability.exceptions.UnsetAvailabilityException;
import dbp.techcall.review.exceptions.ResourceNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(UserAlreadyExistsException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    protected String handleUserAlreadyExistsException(UserAlreadyExistsException e) {
        return e.getMessage();
    }

    @ExceptionHandler(UsernameNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    protected String handleUsernameNotFoundException(UsernameNotFoundException e) {
        return e.getMessage();
    }

    @ExceptionHandler(BookingNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    protected String handleBookingNotFoundException(BookingNotFoundException e) {
        return e.getMessage();
    }

    @ExceptionHandler(AvailabilityDatesErrorException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    protected String handleAvailabilityDatesErrorException(AvailabilityDatesErrorException e) {
        return e.getMessage();
    }

    @ExceptionHandler(UnsetAvailabilityException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    protected String handleNoAvailabilitySetException(UnsetAvailabilityException e) {
        return e.getMessage();
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    protected String handleResourceNotFoundException(ResourceNotFoundException e) {
        return e.getMessage();
    }

    @ExceptionHandler(AlreadyCompletedTourException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    protected String handleAlreadyCompletedTourException(AlreadyCompletedTourException e) {
        return e.getMessage();
    }
}
