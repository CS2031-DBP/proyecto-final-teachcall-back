package dbp.techcall.student.repository;

import dbp.techcall.student.student.domain.models.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.userdetails.UserDetails;

public interface StudentRepository extends JpaRepository<Student, Integer> {
    UserDetails findByEmail(String username);
}
