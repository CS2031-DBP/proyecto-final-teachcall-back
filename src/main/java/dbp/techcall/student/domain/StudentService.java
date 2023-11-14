package dbp.techcall.student.domain;

import dbp.techcall.student.repository.StudentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class StudentService {

    private final StudentRepository studentRepository;

    public UserDetailsService userDetailsService() {
        return new UserDetailsService() {
            @Override
            public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
                return studentRepository.findByEmail(username);
            }
        };
    }



    public Student getStudentById(Long studentId) throws UsernameNotFoundException{
        return studentRepository.findById(studentId).orElse(null);
    }
}
