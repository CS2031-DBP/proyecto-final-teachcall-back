package dbp.techcall.jwt;

import org.springframework.security.core.userdetails.UserDetails;

import java.util.Date;
import java.util.Map;

public interface IJwtService {
    String extractUserName(String jwt);

    String generateToken(UserDetails data);

    void validateToken(String token, String userEmail) throws Exception;

    String generateToken(Map<String, Object> extraClaims, UserDetails userDetails);

    boolean isTokenExpired(String token);

    Date extractExpiration(String token);


}
