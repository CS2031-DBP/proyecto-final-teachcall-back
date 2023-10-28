package dbp.techcall.booking.models;

public class BookingInfo {
    private String courseSubject;
    private String professorName;
    private String professorLastname;
    private String meetingDetailsLink;

    // Getters y setters para los campos

    public String getCourseSubject() {
        return courseSubject;
    }

    public void setCourseSubject(String courseSubject) {
        this.courseSubject = courseSubject;
    }

    public String getProfessorName() {
        return professorName;
    }

    public void setProfessorName(String professorName) {
        this.professorName = professorName;
    }

    public String getProfessorLastname() {
        return professorLastname;
    }

    public void setProfessorLastname(String professorLastname) {
        this.professorLastname = professorLastname;
    }

    public String getMeetingDetailsLink() {
        return meetingDetailsLink;
    }

    public void setMeetingDetailsLink(String meetingDetailsLink) {
        this.meetingDetailsLink = meetingDetailsLink;
    }

    public Booking getBooking() {
        return getBooking();
    }

    public void setBooking(Booking booking) {
        this.setBooking(booking);
    }
}
