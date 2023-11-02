package dbp.techcall.review.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@RequiredArgsConstructor
public class ReviewRequest {
    private String body;
    private Integer rating;
    private Long studentId;
    private Long professorId;
}
