package dbp.techcall.auth.dto;

import dbp.techcall.user.BasicUserInfo;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@RequiredArgsConstructor
public class JwtRes {
    private String token;
    private BasicUserInfo user;
}
