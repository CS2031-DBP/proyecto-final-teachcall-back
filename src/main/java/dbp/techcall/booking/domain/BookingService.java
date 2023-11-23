package dbp.techcall.booking.domain;


import dbp.techcall.booking.dto.*;
import dbp.techcall.booking.event.BookingCreatedEvent;
import dbp.techcall.booking.event.BookingDeleteEvent;
import dbp.techcall.booking.exceptions.BookingNotFoundException;
import dbp.techcall.booking.infrastructure.BookingRepository;
import dbp.techcall.course.domain.Course;
import dbp.techcall.course.dto.TitleDescriptionProjection;
import dbp.techcall.course.infrastructure.CourseRepository;
import dbp.techcall.meetingDetails.domain.MeetingDetailsService;
import dbp.techcall.professor.domain.Professor;
import dbp.techcall.professor.infrastructure.ProfessorRepository;
import dbp.techcall.review.exceptions.ResourceNotFoundException;
import dbp.techcall.student.domain.Student;
import dbp.techcall.student.dto.StudentNames;
import dbp.techcall.student.repository.StudentRepository;
import dbp.techcall.timeSlot.domain.TimeSlot;
import dbp.techcall.timeSlot.dto.DateTimeProjection;
import dbp.techcall.timeSlot.infrastructure.TimeSlotRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class BookingService {

    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private MeetingDetailsService meetingDetailsService;

    @Autowired
    private ProfessorRepository professorRepository;

    @Autowired
    private TimeSlotRepository timeSlotRepository;

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private ApplicationEventPublisher eventPublisher;

    private final List<Booking> bookings;

    public BookingService(List<Booking> bookings) {
        this.bookings = bookings;
    }

    public Booking createBooking(Booking booking) {
        // Agregar una nueva reserva
        bookings.add(booking);
        return booking;
    }

    public Booking getBookingById(Long bookingId) {
        return bookingRepository.findById(bookingId)
                .orElseThrow(() -> new RuntimeException("Booking not found with id: " + bookingId));
    }


    public Booking updateBooking(Long id, Booking booking) {
        // Actualizar una reserva existente por su ID
        for (int i = 0; i < bookings.size(); i++) {
            if (bookings.get(i).getId().equals(id)) {
                bookings.set(i, booking);
                return booking;
            }
        }
        return null;
    }

    public void deleteBooking(Long id) {
        // Eliminar una reserva por su ID
        bookings.removeIf(booking -> booking.getId().equals(id));
        eventPublisher.publishEvent(new BookingDeleteEvent(this, id));
    }


    public BookingInfo getBookingInfoById(Long id) {
        Optional<Booking> booking = bookingRepository.findById(id);

        if (booking.isEmpty()) {
            throw new BookingNotFoundException("La reserva con el ID " + id + " no se encontró.");
        }

        return new BookingInfo(booking.get());

    }

    public void addBooking(BasicBookingReq req, String username) throws ResourceNotFoundException {
        Student student = studentRepository.findByEmail(username);
        if (student == null) {
            throw new ResourceNotFoundException("Student not found, user might be a professor");
        }

        Course course = courseRepository.findById(req.getCourseId()).get();
        Professor professor = professorRepository.findById(req.getProfessorId()).get();
        TimeSlot timeSlot = timeSlotRepository.findById(req.getTimeSlotId()).get();
        Booking newBooking = new Booking();
        newBooking.setCourse(course);
        newBooking.setProfessor(professor);
        newBooking.setStudent(student);
        newBooking.getTimeSlot().add(timeSlot);
        bookingRepository.save(newBooking);
        timeSlot.setIsAvailable(false);
        timeSlot.setBooking(newBooking);
        timeSlotRepository.save(timeSlot);
        eventPublisher.publishEvent(new BookingCreatedEvent(this, newBooking));
    }

    public Page<StudentBookingsRes> getStudentBookings(String username, Integer page) {
        Student student = studentRepository.findByEmail(username);
        if (student == null) {
            throw new ResourceNotFoundException("Student not found, user might be a professor");
        }
        Long studentId = student.getId();

        Pageable pageable = PageRequest.of(page, 10);

        return bookingRepository.getBookingsInfoByStudentId(studentId, pageable);

    }

    public Page<ProfessorBookingRes> getProfessorBookings(String username, Integer page) {
        Professor professor = professorRepository.findByEmail(username);
        if (professor == null) {
            throw new ResourceNotFoundException("Professor not found, user might be a student");
        }

        Pageable pageable = PageRequest.of(page, 10);
        Page<ProfessorBooking> bookings = bookingRepository.findAllByProfessor(professor, pageable);

        if (bookings.isEmpty()) {
            throw new ResourceNotFoundException("No bookings found for professor with ID " + professor.getId());
        }

        List<ProfessorBookingRes> response = new ArrayList<>();

        for(ProfessorBooking b : bookings.getContent()){
            StudentNames student = studentRepository.findStudentNamesById(b.getStudent().getId());
            DateTimeProjection timeSlot = timeSlotRepository.findDateTimeProjectionById(b.getTimeSlot().getId());
            TitleDescriptionProjection course = courseRepository.findTitleDescriptionProjectionById(b.getCourse().getId());

            ProfessorBookingRes professorBookingRes = new ProfessorBookingRes();

            professorBookingRes.setId(b.getId());
            professorBookingRes.setLink(meetingDetailsService.getMeetingDetailsHostRoomUrl(b.getId().intValue()));
            professorBookingRes.setStudent(student);
            professorBookingRes.setTimeSlot(timeSlot);
            professorBookingRes.setCourse(course);

            response.add(professorBookingRes);
        }

        return new PageImpl<>(response, pageable, bookings.getTotalElements());
    }
}






