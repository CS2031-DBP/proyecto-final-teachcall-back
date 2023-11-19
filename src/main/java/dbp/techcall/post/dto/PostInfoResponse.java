package dbp.techcall.post.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@RequiredArgsConstructor
public class PostInfoResponse {
    Integer id;
    String title;
    String body;
    LocalDateTime createdAt;
    String firstName;
    String lastName;
    boolean liked;
    int likesQ;
}
