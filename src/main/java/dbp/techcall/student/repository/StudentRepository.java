package dbp.techcall.student.repository;

import dbp.techcall.student.domain.Student;
import dbp.techcall.user.infrastructure.BaseUserRepository;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StudentRepository extends BaseUserRepository<Student> {
    Student findByEmail(String username);
}
