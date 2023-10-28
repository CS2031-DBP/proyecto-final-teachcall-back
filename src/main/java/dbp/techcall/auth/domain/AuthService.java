package dbp.techcall.auth.domain;

import dbp.techcall.auth.dto.JwtRes;
import dbp.techcall.auth.dto.LoginReq;
import dbp.techcall.auth.dto.RegisterReq;
import dbp.techcall.auth.exceptions.UserAlreadyExistsException;
import dbp.techcall.auth.application.IAuthUseCase;
import dbp.techcall.professor.domain.Professor;
import dbp.techcall.professor.infrastructure.ProfessorRepository;
import dbp.techcall.student.repository.StudentRepository;
import dbp.techcall.student.domain.Student;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import dbp.techcall.jwt.JwtService;


import java.time.ZonedDateTime;

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


    public JwtRes registerProfessor(RegisterReq request) {
        Professor existingProfessor = teacherRepository.findByEmail(request.getEmail());
        if (existingProfessor != null) {
            throw new UserAlreadyExistsException("Teacher");
        }

        Professor professor = new Professor();
        professor.setFirstName(request.getFirstName());
        professor.setLastName(request.getLastName());
        professor.setEmail(request.getEmail());
        professor.setPassword(passwordEncoder.encode(request.getPassword()));
        professor.setCreatedAt(ZonedDateTime.now());

        teacherRepository.save(professor);

        String jwt = jwtService.generateToken(professor);
        return new JwtRes(jwt);
    }
    public JwtRes registerStudent(RegisterReq request) {
        Student existingStudent = studentRepository.findByEmail(request.getEmail());

        if (existingStudent != null) {
            throw new UserAlreadyExistsException("Student");
        }

        Student student = new Student();
        student.setFirstName(request.getFirstName());
        student.setLastName(request.getLastName());
        student.setEmail(request.getEmail());
        student.setPassword(passwordEncoder.encode(request.getPassword()));
        student.setCreatedAt(ZonedDateTime.now());

        studentRepository.save(student);

        String jwt = jwtService.generateToken(student);
        return new JwtRes(jwt);
        }

    public JwtRes loginProfessor(LoginReq request) {

        Professor teacher = teacherRepository.findByEmail(request.getEmail());

        if(teacher == null){
            throw new IllegalArgumentException("Email or password are incorrect");
        }

        if(!passwordEncoder.matches(request.getPassword(), teacher.getPassword())){
            throw new IllegalArgumentException("Email or password are incorrect");
        }

        return new JwtRes(jwtService.generateToken(modelMapper.map(teacher, UserDetails.class)));
    }
    public JwtRes loginStudent(LoginReq request) {

        Student student = studentRepository.findByEmail(request.getEmail());

        if(student == null){
            throw new UsernameNotFoundException("Email is not registered");
        }

        if(!passwordEncoder.matches(request.getPassword(), student.getPassword())){
            throw new IllegalArgumentException("Password is incorrect");
        }

        return new JwtRes(jwtService.generateToken(modelMapper.map(student, UserDetails.class)));
    }

}
