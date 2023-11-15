package dbp.techcall.post.dto;


import dbp.techcall.student.domain.Student;
import dbp.techcall.student.dto.StudentId;

import java.time.LocalDateTime;
import java.util.Set;

public interface PostInfo {
    public Long getId();
    public String getTitle();
    public String getBody();
    public Long getLikes();
    public LocalDateTime getCreatedAt();
}
