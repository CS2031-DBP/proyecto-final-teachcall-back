package dbp.techcall.post.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@RequiredArgsConstructor
public class BasicPost {

    private final String title;
    private final String body;

}
