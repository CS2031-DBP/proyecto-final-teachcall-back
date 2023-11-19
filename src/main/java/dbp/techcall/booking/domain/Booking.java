package dbp.techcall.booking.domain;

import dbp.techcall.course.domain.Course;
import dbp.techcall.meetingDetails.domain.MeetingDetails;
import dbp.techcall.professor.domain.Professor;
import dbp.techcall.student.domain.Student;
import dbp.techcall.timeSlot.domain.TimeSlot;
import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@RequiredArgsConstructor
@Entity
@Table(name="booking")
public class Booking{
    @Id
    @Column(name = "id",nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name="status",nullable = true)
    private String status;

    @Column(name="link",nullable = true)
    private String link;

    @ManyToOne()
    @JoinColumn(name="student_id", referencedColumnName="id")
    private Student student;

    @ManyToOne()
    @JoinColumn(name="course_id", referencedColumnName="id")
    private Course course;

    @ManyToOne()
    @JoinColumn(name="professor_id", referencedColumnName="id")
    private Professor professor;


    @OneToMany(mappedBy = "booking" ,cascade = {CascadeType.ALL})
    private Set<TimeSlot> timeSlot = new HashSet<>();

    @Getter
    @OneToOne(mappedBy = "booking", cascade = CascadeType.ALL)
    private MeetingDetails meetingDetails;

}
