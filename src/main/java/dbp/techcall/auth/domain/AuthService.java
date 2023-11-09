package dbp.techcall.auth.domain;

import dbp.techcall.auth.application.IAuthUseCase;
import dbp.techcall.auth.dto.JwtRes;
import dbp.techcall.auth.dto.LoginReq;
import dbp.techcall.auth.dto.RegisterReq;
import dbp.techcall.auth.exceptions.UserAlreadyExistsException;
import dbp.techcall.jwt.JwtService;
import dbp.techcall.professor.domain.Professor;
import dbp.techcall.professor.infrastructure.ProfessorRepository;
import dbp.techcall.student.domain.Student;
import dbp.techcall.student.repository.StudentRepository;
import dbp.techcall.user.BasicUserInfo;
import dbp.techcall.user.domain.Users;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

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

    public JwtRes register(RegisterReq request){

        if (request.getRole().equals("student")) {
            Student existingStudent = studentRepository.findByEmail(request.getEmail());

            if (existingStudent != null) {
                throw new UserAlreadyExistsException("Student");
            }
        }
        if (request.getRole().equals("teacher")) {
            Professor existingProfessor = teacherRepository.findByEmail(request.getEmail());

            if (existingProfessor != null) {
                throw new UserAlreadyExistsException("Teacher");
            }
        }

        if (request.getRole().equals("student")) {
            Student student = new Student();
            student.setFirstName(request.getFirstName());
            student.setLastName(request.getLastName());
            student.setEmail(request.getEmail());
            student.setPassword(passwordEncoder.encode(request.getPassword()));
            student.setRole(request.getRole());
            student.setCreatedAt(ZonedDateTime.now());

            studentRepository.save(student);

            String jwt = jwtService.generateToken(student);
            JwtRes response = new JwtRes();
            response.setToken(jwt);
            response.setUser(modelMapper.map(student, BasicUserInfo.class));
            return  response;
        }
        if (request.getRole().equals("teacher")) {
            Professor professor = new Professor();
            professor.setFirstName(request.getFirstName());
            professor.setLastName(request.getLastName());
            professor.setEmail(request.getEmail());
            professor.setPassword(passwordEncoder.encode(request.getPassword()));
            professor.setRole(request.getRole());
            professor.setCreatedAt(ZonedDateTime.now());
            professor.setTourCompleted(false);

            teacherRepository.save(professor);

            String jwt = jwtService.generateToken(professor);
            JwtRes response =  new JwtRes();

            response.setToken(jwt);
            response.setUser(modelMapper.map(professor, BasicUserInfo.class));
            return  response;
        }
        else {
            throw new IllegalArgumentException("Role is not valid");
        }

    }

    public JwtRes login(LoginReq req){

        Users user;

        if (req.getRole().equals("student")) {
            user = studentRepository.findByEmail(req.getEmail());
        }
        else if (req.getRole().equals("teacher")) {
            user = teacherRepository.findByEmail(req.getEmail());
        }
        else {
            throw new IllegalArgumentException("Role is not valid");
        }

        if(user == null){
            throw new UsernameNotFoundException("Email is not registered");
        }

        if(!passwordEncoder.matches(req.getPassword(), user.getPassword())){
            throw new IllegalArgumentException("Password is incorrect");
        }

        JwtRes response = new JwtRes();
        response.setToken(jwtService.generateToken(modelMapper.map(user, UserDetails.class)));
        response.setUser(modelMapper.map(user, BasicUserInfo.class));
        return response;
    }
}
