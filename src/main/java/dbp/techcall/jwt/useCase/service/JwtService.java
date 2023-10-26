package dbp.techcall.jwt.useCase.service;


import com.auth0.jwt.JWT;
import dbp.techcall.jwt.useCase.IJwtService;
import dbp.techcall.professor.domain.ProfessorService;
import dbp.techcall.student.domain.StudentService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.Payload;


import java.util.Date;
import java.util.Map;

@RequiredArgsConstructor
@Service
public class JwtService implements IJwtService {

    private final ProfessorService professorService;
    private final StudentService studentService;

    @Value("${my.awesome.secret}")
    private String secretKey;

    public String extractUserName(String jwt) {
        Payload payload = JWT.decode(jwt);
        return payload.getSubject();
    }

    public String extractUserRole(String jwt) {
        Payload payload = JWT.decode(jwt);
        return payload.getClaim("role").asString();
    }


    public String generateToken(UserDetails data) {
        Date now = new Date();
        Date expiration = new Date(now.getTime() + 1000 * 60 * 60 * 10);

        Algorithm algorithm = Algorithm.HMAC256(secretKey);

        return JWT.create()
                .withSubject(data.getUsername())
                .withClaim("role", data.getAuthorities().toArray()[0].toString())
                .withExpiresAt(expiration)
                .withIssuedAt(now)
                .sign(algorithm);
    }


    public void validateToken(String token, String userEmail) throws AuthenticationException{

        System.out.println("token: " + token);

        JWT.require(Algorithm.HMAC256(secretKey)).build().verify(token);
        final String userRole = this.extractUserRole(token);
        UserDetails userDetails =
                userRole.equals("PROFESSOR") ?
                        professorService.userDetailsService().loadUserByUsername(userEmail) :
                        studentService.userDetailsService().loadUserByUsername(userEmail);

        SecurityContext context = SecurityContextHolder.createEmptyContext();
        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                userDetails, token, userDetails.getAuthorities());
        context.setAuthentication(authToken);
        SecurityContextHolder.setContext(context);

    }


    public String generateToken(Map<String, Object> extraClaims, UserDetails userDetails) {
        return null;
    }


    public boolean isTokenExpired(String token) {
        return false;
    }

    public Date extractExpiration(String token) {
        return null;
    }
}
