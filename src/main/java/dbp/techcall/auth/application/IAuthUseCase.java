package dbp.techcall.auth.application;

import dbp.techcall.auth.dto.JwtRes;
import dbp.techcall.auth.dto.LoginReq;
import dbp.techcall.auth.dto.RegisterReq;

public interface IAuthUseCase {
    JwtRes registerProfessor(RegisterReq request);

    JwtRes loginProfessor(LoginReq request);

    JwtRes registerStudent(RegisterReq request);

    JwtRes loginStudent(LoginReq request);
}
