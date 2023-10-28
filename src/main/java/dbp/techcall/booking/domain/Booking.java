package dbp.techcall.booking.domain;

import dbp.techcall.meetingDetails.domain.MeetingDetails;
import dbp.techcall.timeSlot.domain.TimeSlot;
import dbp.techcall.course.domain.Course;
import dbp.techcall.professor.domain.Professor;
import dbp.techcall.student.domain.Student;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name="booking")
public class Booking{
    @Id
    @Column(name = "id",nullable = false)
    private Integer id;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="student_id", referencedColumnName="id", insertable = false, updatable = false)
    private Student student;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="course_id", referencedColumnName="id", insertable = false, updatable = false)
    private Course course;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="professor_id", referencedColumnName="id", insertable = false, updatable = false)
    private Professor professor;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="time_slot_id", referencedColumnName="id", insertable = false, updatable = false)
    private TimeSlot timeSlot;

    @Column(name="status",nullable = false)
    private String status;

    @Column(name="link",nullable = true)
    private String link;

    @Getter
    @OneToOne(mappedBy = "booking", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private MeetingDetails meetingDetails;


}
