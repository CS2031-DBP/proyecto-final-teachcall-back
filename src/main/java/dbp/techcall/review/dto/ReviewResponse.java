package dbp.techcall.review.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@RequiredArgsConstructor
public class ReviewResponse {
    private UUID id;
    private String body;
    private Integer rating;
    private String studentName;
    private String createdAt;
}
