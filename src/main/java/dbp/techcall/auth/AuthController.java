package dbp.techcall.auth;

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

    @PostMapping("/signup/teacher")
    public ResponseEntity<JwtRes> registerTeacher(@RequestBody RegisterReq request) {
        return ResponseEntity.ok(authService.registerProfessor(request));
    }

    @PostMapping("/signup/student")
    public ResponseEntity<JwtRes> registerStudent(@RequestBody RegisterReq request) {
        return ResponseEntity.ok(authService.registerStudent(request));
    }


    @PostMapping("/signin/teacher")
    public ResponseEntity<JwtRes> signinTeacher(@RequestBody LoginReq request) {
        return ResponseEntity.ok(authService.loginProfessor(request));
    }

    @PostMapping("/signin/student")
    public ResponseEntity<JwtRes> signinStudent(@RequestBody LoginReq request) {
        return ResponseEntity.ok(authService.loginStudent(request));
    }
}
