package dbp.techcall.booking.infrastructure;

import dbp.techcall.booking.domain.Booking;
import dbp.techcall.booking.dto.StudentBookingsRes;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface BookingRepository extends JpaRepository<Booking, Long> {

    @Query(
            value = """
                    select ts.date       as date,
                           ts.start_time as startTime,
                           p.first_name  as firstName,
                           p.last_name   as lastName,
                           c.title       as title,
                           bk.link       as link
                    from time_slot as ts
                             join
                         (select id,
                                 link,
                                 course_id    as c_id,
                                 professor_id as p_id,
                                 student_id   as s_id
                          from booking
                          where student_id = :studentId ) as bk
                         on ts.booking_id = bk.id
                             join
                         (select id, first_name, last_name
                          from professor) as p
                         on bk.p_id = p.id
                             join (select id, title
                                   from course) as c
                                  on bk.c_id = c.id
                    ORDER BY startTime DESC;"""
            , nativeQuery = true)
    Page<StudentBookingsRes> getBookingsInfoByStudentId(Long studentId, Pageable pageable);
}
