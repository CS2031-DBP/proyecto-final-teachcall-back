package dbp.techcall.booking.models;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name="meeting_details", schema = "spring_app")
public class MeetingDetails {
    @Id
    @Column(name = "id",nullable = false)
    private Integer id;

    @Column(name="enddate",nullable = false)
    private String endDate;

    @Column(name="islocked",nullable = false)
    private Boolean isLocked;

    @Column(name="roommode",nullable = false)
    private String roomMode;

   @Column(name="roomnameprefix",nullable = false)
    private String roomNamePrefix;

    @Column(name="templatetype",nullable = false)
    private String templateType;

//    @Column(name="recording",nullable = false)
//    private  recording; //jsonb
    //type jsonb

    @Column(name="hostroomurl",nullable = false)
    private String hostRoomUrl;

    @Column(name="viewerroomurl",nullable = false)
    private String viewerRoomUrl;

    @Column(name="booking_id",nullable = false)
    private Integer bookingId;
     // Foreign Key


    //Constructores:

    public MeetingDetails(Integer id,
                          String endDate,
                          Boolean isLocked,
                          String roomMode,
                          String roomNamePrefix,
                          String templateType,
                          String hostRoomUrl,
                          String viewerRoomUrl,
                          Integer bookingId) {
        this.id = id;
        this.endDate = endDate;
        this.isLocked = isLocked;
        this.roomMode = roomMode;
        this.roomNamePrefix = roomNamePrefix;
        this.templateType = templateType;
        this.hostRoomUrl = hostRoomUrl;
        this.viewerRoomUrl = viewerRoomUrl;
        this.bookingId = bookingId;
    }

    //Default constructor
    public MeetingDetails() {
    }


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public Boolean getLocked() {
        return isLocked;
    }

    public void setLocked(Boolean locked) {
        isLocked = locked;
    }

    public String getRoomMode() {
        return roomMode;
    }

    public void setRoomMode(String roomMode) {
        this.roomMode = roomMode;
    }

    public String getRoomNamePrefix() {
        return roomNamePrefix;
    }

    public void setRoomNamePrefix(String roomNamePrefix) {
        this.roomNamePrefix = roomNamePrefix;
    }

    public String getTemplateType() {
        return templateType;
    }

    public void setTemplateType(String templateType) {
        this.templateType = templateType;
    }

    public String getHostRoomUrl() {
        return hostRoomUrl;
    }

    public void setHostRoomUrl(String hostRoomUrl) {
        this.hostRoomUrl = hostRoomUrl;
    }

    public String getViewerRoomUrl() {
        return viewerRoomUrl;
    }

    public void setViewerRoomUrl(String viewerRoomUrl) {
        this.viewerRoomUrl = viewerRoomUrl;
    }

    public Integer getBookingId() {
        return bookingId;
    }

    public void setBookingId(Integer bookingId) {
        this.bookingId = bookingId;
    }
}
