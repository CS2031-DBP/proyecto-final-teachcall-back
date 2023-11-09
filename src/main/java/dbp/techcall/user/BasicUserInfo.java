package dbp.techcall.user;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@RequiredArgsConstructor
public class BasicUserInfo {
    private String firstName;
    private String lastName;
    private String email;
    private Boolean tourCompleted;
}
