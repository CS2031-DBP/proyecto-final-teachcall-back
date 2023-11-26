package dbp.techcall.post.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@RequiredArgsConstructor
public class MyPostsResponse {
    Integer id;
    String title;
    String body;
    LocalDateTime createdAt;
    String mediaUrl;
    String mediaExtension;
    int likes;
}
