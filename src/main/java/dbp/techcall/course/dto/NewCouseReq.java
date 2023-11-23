package dbp.techcall.course.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@RequiredArgsConstructor
public class NewCouseReq {
    String title;
    String description;
    double price;
}
