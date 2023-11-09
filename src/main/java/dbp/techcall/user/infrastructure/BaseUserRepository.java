package dbp.techcall.user.infrastructure;

import dbp.techcall.user.domain.Users;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;


public interface BaseUserRepository<T extends Users> extends JpaRepository<T, Long>{
    Optional<T> findById(Long id);

}
