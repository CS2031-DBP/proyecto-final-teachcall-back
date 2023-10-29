package dbp.techcall.auth.application;

import dbp.techcall.auth.domain.AuthService;
import dbp.techcall.auth.dto.JwtRes;
import dbp.techcall.auth.dto.LoginReq;
import dbp.techcall.auth.dto.RegisterReq;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RequiredArgsConstructor
@RestController
@RequestMapping("auth")
@CrossOrigin(origins ={"http://localhost:5173", "http://127.0.0.1:5173"})
public class AuthController {

    @Autowired
    private final AuthService authService;

    @PostMapping("/signup")
    public ResponseEntity<JwtRes> register(@RequestBody RegisterReq request) {
        return ResponseEntity.ok(authService.register(request));
    }

    @PostMapping("/signin")
    public ResponseEntity<JwtRes> signin(@RequestBody LoginReq request) {
        return ResponseEntity.ok(authService.login(request));
    }
}
