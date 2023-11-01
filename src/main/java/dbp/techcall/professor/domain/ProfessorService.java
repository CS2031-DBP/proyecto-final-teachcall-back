package dbp.techcall.professor.domain;

import dbp.techcall.professor.dto.NewProfessorDto;
import dbp.techcall.professor.infrastructure.ProfessorRepository;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;




@Service
public class ProfessorService implements IProfessorService {

    @Autowired
    private ProfessorRepository professorRepository;

    private PasswordEncoder passwordEncoder ;

    private ModelMapper modelMapper;

    @Override
    public void save(NewProfessorDto newProfessor) {
        newProfessor.setPassword(passwordEncoder.encode(newProfessor.getPassword()));
        professorRepository.save(modelMapper.map(newProfessor, Professor.class));

    }

    public Professor findByEmail(String email) {
        return professorRepository.findByEmail(email);
    }



    public UserDetailsService userDetailsService() {
        return new UserDetailsService() {
            @Override
            public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
                return professorRepository.findByEmail(username);
            }
        };
    }

    public Professor findById(Long professorId) {
        Optional<Professor> professor =  professorRepository.findById(professorId);

        if(professor.isEmpty()) {
            throw new RuntimeException("Professor not found");
        }

        return professor.get();
    }
}
