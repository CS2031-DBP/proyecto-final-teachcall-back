package dbp.techcall.professor.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@RequiredArgsConstructor
public class AddCategoriesReq {
    private String email;
    private List<Long> categories;

}
