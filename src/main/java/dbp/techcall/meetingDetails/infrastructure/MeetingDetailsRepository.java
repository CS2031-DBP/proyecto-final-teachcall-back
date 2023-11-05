package dbp.techcall.meetingDetails.infrastructure;

import dbp.techcall.meetingDetails.domain.MeetingDetails;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MeetingDetailsRepository extends JpaRepository<MeetingDetails, Integer> {
}
