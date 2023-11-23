package dbp.techcall.booking.infrastructure;

import dbp.techcall.booking.domain.Booking;
import dbp.techcall.booking.dto.ProfessorBooking;
import dbp.techcall.booking.dto.StudentBookingsRes;
import dbp.techcall.professor.domain.Professor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface BookingRepository extends JpaRepository<Booking, Long> {

    @Query(
            value = """
            SELECT
              bk.id,
              ts.date,
              ts.start_time AS startTime,
              p.first_name AS firstName,
              p.last_name AS lastName,
              c.title,
              md.viewer_room_url AS link
            FROM\s
              booking AS bk
            JOIN\s
              meeting_details AS md ON bk.id = md.booking_id
            JOIN\s
              time_slot AS ts ON bk.id = ts.booking_id
            JOIN\s
              professor AS p ON bk.professor_id = p.id
            JOIN\s
              course AS c ON bk.course_id = c.id
            WHERE\s
              bk.student_id = ?
            ORDER BY\s
              ts.start_time DESC
            FETCH FIRST ? ROWS ONLY;
            
            """
            , nativeQuery = true)
    Page<StudentBookingsRes> getBookingsInfoByStudentId(@Param("id") Long studentId, Pageable pageable);

    Page<ProfessorBooking> findAllByProfessor(Professor professor, Pageable pageable);

    Optional<Booking> findByCourseIdAndProfessorIdAndStudentIdAndTimeSlotId(
            Long courseId, Long professorId, Long studentId, Long timeSlotId);
}
