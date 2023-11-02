package dbp.techcall.review.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@RequiredArgsConstructor
public class ProfessorRatingInfo {

    private Long professorId;
    private Double averageRating;
    private Integer reviewCount;

}
