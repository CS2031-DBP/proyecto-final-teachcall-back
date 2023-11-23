package dbp.techcall.auth.application;

import dbp.techcall.auth.domain.AuthService;
import dbp.techcall.auth.dto.JwtRes;
import dbp.techcall.auth.dto.LoginReq;
import dbp.techcall.auth.dto.RegisterReq;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("auth")

public class AuthController {

    @Autowired
    private AuthService authService;

    @PostMapping("/signup")
    public ResponseEntity<JwtRes> register(@RequestBody RegisterReq request) {
        return ResponseEntity.ok(authService.register(request));
    }

    @PostMapping("/signin")
    public ResponseEntity<JwtRes> signin(@RequestBody LoginReq request) {
        return ResponseEntity.ok(authService.login(request));
    }
}
