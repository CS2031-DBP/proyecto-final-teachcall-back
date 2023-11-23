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
            SELECT bk.id         AS id,
                   ts.date       AS date,
                   ts.start_time AS startTime,
                   p.first_name  AS firstName,
                   p.last_name   AS lastName,
                   c.title       AS title,
                   md.viewer_room_url AS link  -- Seleccionamos el viewer_room_url de la tabla meeting_details
            FROM time_slot AS ts
            JOIN (
                SELECT b.id,
                       md.viewer_room_url, -- Necesitamos seleccionar el viewer_room_url aquí para unirlo después
                       b.course_id    AS c_id,
                       b.professor_id AS p_id,
                       b.student_id   AS s_id
                FROM booking AS b
                JOIN meeting_details AS md ON b.id = md.booking_id -- Hacemos el JOIN aquí con la tabla meeting_details
                WHERE b.student_id = :id\s
            ) AS bk ON ts.booking_id = bk.id
            JOIN (
                SELECT id, first_name, last_name
                FROM professor
            ) AS p ON bk.p_id = p.id
            JOIN (
                SELECT id, title
                FROM course
            ) AS c ON bk.c_id = c.id
            ORDER BY startTime DESC;
            """
            , nativeQuery = true)
    Page<StudentBookingsRes> getBookingsInfoByStudentId(@Param("id") Long studentId, Pageable pageable);

    Page<ProfessorBooking> findAllByProfessor(Professor professor, Pageable pageable);

    Optional<Booking> findByCourseIdAndProfessorIdAndStudentIdAndTimeSlotId(
            Long courseId, Long professorId, Long studentId, Long timeSlotId);
}
