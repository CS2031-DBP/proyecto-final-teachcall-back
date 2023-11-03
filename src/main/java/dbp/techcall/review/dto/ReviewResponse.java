package dbp.techcall.review.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@RequiredArgsConstructor
public class ReviewResponse {
    private String body;
    private Integer rating;
    private String studentName;
    private String createdAt;
}
