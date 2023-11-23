package dbp.techcall.booking.infrastructure;

import dbp.techcall.booking.domain.Booking;
import dbp.techcall.booking.dto.ProfessorBooking;
import dbp.techcall.booking.dto.StudentBooking;
import dbp.techcall.professor.domain.Professor;
import dbp.techcall.student.domain.Student;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;


import java.util.Optional;

public interface BookingRepository extends JpaRepository<Booking, Long> {

    Page<Booking> findAllByProfessorId(Long professorId, Pageable pageable);

    Page<ProfessorBooking> findAllByProfessor(Professor professor, Pageable pageable);

    Page<StudentBooking> findAllByStudent(Student student, Pageable pageable);

    Optional<Booking> findByCourseIdAndProfessorIdAndStudentIdAndTimeSlotId(
            Long courseId, Long professorId, Long studentId, Long timeSlotId);
}
