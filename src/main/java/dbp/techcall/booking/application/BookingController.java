package dbp.techcall.booking.application;

import dbp.techcall.booking.domain.Booking;
import dbp.techcall.booking.domain.BookingService;
import dbp.techcall.booking.dto.*;
import dbp.techcall.review.exceptions.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/bookings")
public class BookingController {

    @Autowired
    private BookingService bookingService;


    @GetMapping("/short/{id}")
    public BookingInfo getBookingInfoById(@PathVariable Long id) {
            BookingInfo bookingInfo = bookingService.getBookingInfoById(id);
            return bookingInfo;
    }

    @PostMapping
    public ResponseEntity<Booking> createBooking(@RequestBody Booking booking) {
        Booking createdBooking = bookingService.createBooking(booking);
        return new ResponseEntity<>(createdBooking, HttpStatus.CREATED);
    }

    @PutMapping("/short/{id}")
    public Booking updateBooking(@PathVariable Long id, @RequestBody Booking booking) {
        return bookingService.updateBooking(id, booking);
    }

    @DeleteMapping("/short/{id}")
    public ResponseEntity<Void> deleteBooking(@PathVariable Long id) {
        bookingService.deleteBooking(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PostMapping("/new")
    public ResponseEntity<String> addBooking(@RequestBody BasicBookingReq bookingReq) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        String username = userDetails.getUsername();

        try {
            bookingService.addBooking(bookingReq, username);
            return ResponseEntity.ok("booking added");
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Booking already exists");
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Resource not found");
        }
    }


    @PreAuthorize("hasRole('student')")
    @GetMapping("/student")
    public ResponseEntity<Page<StudentBookingsRes>> getStudentBookings(@RequestParam(defaultValue = "0") Integer page ){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        String username = userDetails.getUsername();

        return ResponseEntity.ok(bookingService.getStudentBookings(username, page));
    }

    @PreAuthorize("hasRole('teacher')")
    @GetMapping("/professor")
    public ResponseEntity<Page<ProfessorBookingRes>> getProfessorBookings(@RequestParam(defaultValue = "0") Integer page){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        String username = userDetails.getUsername();

        return ResponseEntity.ok(bookingService.getProfessorBookings(username, page));
    }

}



