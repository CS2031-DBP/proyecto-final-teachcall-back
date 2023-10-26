package dbp.techcall.auth.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@RequiredArgsConstructor
public class LoginReq {
    private final String email;
    private final String password;
}
