package dbp.techcall.education.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@RequiredArgsConstructor
public class BasicEducationRequest {
    private String degree;
    private String description;
    private String startDate;
    private String endDate;
    private String schoolName;
    private String imgUrl;
}
