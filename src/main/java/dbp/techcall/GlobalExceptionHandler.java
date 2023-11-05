package dbp.techcall;

import dbp.techcall.auth.exceptions.UserAlreadyExistsException;
import dbp.techcall.booking.exceptions.BookingNotFoundException;
import dbp.techcall.meetingDetails.exception.InvalidWherebyActionException;
import dbp.techcall.meetingDetails.exception.InvalidWherebyApiKeyException;
import dbp.techcall.meetingDetails.exception.MeetingDetailsNotFoundException;
import dbp.techcall.meetingDetails.exception.WherebyRequestLimitExceededException;
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

    @ExceptionHandler(InvalidWherebyApiKeyException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    protected String handleInvalidWherebyApiKeyException(InvalidWherebyApiKeyException e){return e.getMessage();}

    @ExceptionHandler(InvalidWherebyActionException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    protected String handleInvalidWherebyActionException(InvalidWherebyActionException e){return e.getMessage();}

    @ExceptionHandler(WherebyRequestLimitExceededException.class)
    @ResponseStatus(HttpStatus.TOO_MANY_REQUESTS)
    protected String handleWherebyRequestLimitExceededException(WherebyRequestLimitExceededException e){return e.getMessage();}

    @ExceptionHandler(MeetingDetailsNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    protected String handleMeetingDetailsNotFoundException(MeetingDetailsNotFoundException e){return e.getMessage();}
}
