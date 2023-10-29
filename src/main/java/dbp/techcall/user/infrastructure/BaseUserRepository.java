package dbp.techcall.user.infrastructure;

import dbp.techcall.user.domain.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;

@NoRepositoryBean
public interface BaseUserRepository<T extends Users> extends JpaRepository<T, Long>{
    T findById(long id);
}
