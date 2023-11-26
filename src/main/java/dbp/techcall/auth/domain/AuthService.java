package dbp.techcall.auth.domain;

import dbp.techcall.auth.application.IAuthUseCase;
import dbp.techcall.auth.dto.JwtRes;
import dbp.techcall.auth.dto.LoginReq;
import dbp.techcall.auth.dto.RegisterReq;
import dbp.techcall.auth.event.UserRegisteredEvent;
import dbp.techcall.auth.event.UserRegistrationListener;
import dbp.techcall.auth.exceptions.UserAlreadyExistsException;
import dbp.techcall.jwt.JwtService;
import dbp.techcall.professor.domain.Professor;
import dbp.techcall.professor.infrastructure.ProfessorRepository;
import dbp.techcall.s3.StorageService;
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

import java.nio.file.ReadOnlyFileSystemException;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

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

    @Autowired
    private final UserRegistrationListener eventPublisher;

    @Autowired
    private final StorageService storageService;

    public JwtRes register(RegisterReq request) {

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
            eventPublisher.onApplicationEvent(new UserRegisteredEvent(student));
            String jwt = jwtService.generateToken(student);
            JwtRes response = new JwtRes();
            response.setToken(jwt);
            response.setUser(modelMapper.map(student, BasicUserInfo.class));
            return response;
        }
        else if (request.getRole().equals("teacher")) {
            Professor professor = new Professor();
            professor.setFirstName(request.getFirstName());
            professor.setLastName(request.getLastName());
            professor.setEmail(request.getEmail());
            professor.setPassword(passwordEncoder.encode(request.getPassword()));
            professor.setRole(request.getRole());
            professor.setCreatedAt(ZonedDateTime.now());
            professor.setTourCompleted(false);

            teacherRepository.save(professor);
            eventPublisher.onApplicationEvent(new UserRegisteredEvent(professor));

            String jwt = jwtService.generateToken(professor);
            JwtRes response = new JwtRes();

            response.setToken(jwt);
            response.setUser(modelMapper.map(professor, BasicUserInfo.class));
            System.out.println(jwt);
            return response;

        } else {
            throw new IllegalArgumentException("Role is not valid");
        }

    }

    public JwtRes login(LoginReq req) {

        Users user;
        List<String> objectKeys = new ArrayList<>();

        if (req.getRole().equals("student")) {
            user = studentRepository.findByEmail(req.getEmail());
        } else if (req.getRole().equals("teacher")) {
            user = teacherRepository.findByEmail(req.getEmail());
            if (user == null) throw new UsernameNotFoundException("Email is not registered");
            objectKeys.addAll(extractProfileImgKeys((Professor) user));
        } else {
            throw new ReadOnlyFileSystemException();
        }

        if (user == null) throw new UsernameNotFoundException("Email is not registered");
        if (!passwordEncoder.matches(req.getPassword(), user.getPassword()))
            throw new IllegalArgumentException("Password is incorrect");


        JwtRes response = new JwtRes();
        if (objectKeys.isEmpty()) {
            response.setPp(" ");
            response.setCp(" ");
        } else {
            response.setPp(
                    objectKeys.get(0) == null
                            ? ""
                            : storageService.generatePresignedUrl(objectKeys.get(0)));

            response.setCp(
                    objectKeys.get(1) == null
                            ? ""
                            : storageService.generatePresignedUrl(objectKeys.get(1)));
        }

        response.setToken(jwtService.generateToken(modelMapper.map(user, UserDetails.class)));
        response.setUser(modelMapper.map(user, BasicUserInfo.class));
        return response;
    }

    private List<String> extractProfileImgKeys(Professor professor) {
        List<String> keys = new ArrayList<>();
        keys.add(professor.getProfilePicKey());
        keys.add(professor.getCoverPicKey());
        return keys;
    }
}
