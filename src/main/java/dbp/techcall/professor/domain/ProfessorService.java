package dbp.techcall.professor.domain;

import dbp.techcall.professor.dto.NewProfessorDto;
import dbp.techcall.professor.infrastructure.ProfessorRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class ProfessorService implements IProfessorService {

    private ProfessorRepository professorRepository;

    private PasswordEncoder passwordEncoder;

    private ModelMapper modelMapper;

    @Override
    public void save(NewProfessorDto newProfessor) {
        newProfessor.setPassword(passwordEncoder.encode(newProfessor.getPassword()));
        professorRepository.save(modelMapper.map(newProfessor, Professor.class));

    }

    public UserDetailsService userDetailsService() {
        return new UserDetailsService() {
            @Override
            public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
                return professorRepository.findByEmail(username);
            }
        };
    }
}
