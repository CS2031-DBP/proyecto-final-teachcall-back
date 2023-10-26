package dbp.techcall.auth;

import dbp.techcall.auth.dto.JwtRes;
import dbp.techcall.auth.dto.LoginReq;
import dbp.techcall.auth.dto.RegisterReq;
import dbp.techcall.auth.useCase.IAuthUseCase;
import dbp.techcall.professor.professor.infrastructure.models.Professor;
import dbp.techcall.professor.repository.ProfessorRepository;
import dbp.techcall.student.repository.StudentRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import dbp.techcall.jwt.useCase.service.JwtService;

import java.util.Optional;

@RequiredArgsConstructor
@Service
public class AuthService implements IAuthUseCase {

    @Autowired
    private final PasswordEncoder passwordEncoder;

    @Autowired
    private final JwtService jwtService;

    @Autowired
    private final StudentRepository studentRepository;

    @Autowired
    private final ProfessorRepository teacherRepository;

    @Autowired
    private final ModelMapper modelMapper;


    public JwtRes register(RegisterReq request) {
        var teacher = new Professor();
        teacher.setFirstName(request.getFirstName());
        teacher.setLastName(request.getLastName());
        teacher.setEmail(request.getEmail());
        teacher.setPassword(passwordEncoder.encode(request.getPassword()));

        teacherRepository.save(teacher);

        String jwt = jwtService.generateToken(teacher);
        return new JwtRes(jwt);
    }

    public JwtRes login(LoginReq request) {

        Professor teacher = teacherRepository.findByEmail(request.getEmail());

        if(teacher == null){
            throw new IllegalArgumentException("Email is not registered");
        }

        if(!passwordEncoder.matches(request.getPassword(), teacher.getPassword())){
            throw new IllegalArgumentException("Password is incorrect");
        }

        return new JwtRes(jwtService.generateToken(modelMapper.map(teacher, UserDetails.class)));
    }
}
