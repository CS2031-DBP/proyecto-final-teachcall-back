package dbp.techcall.professor.domain;

import dbp.techcall.education.domain.Education;
import dbp.techcall.education.dto.BasicEducationRequest;
import dbp.techcall.education.dto.BasicEducationResponse;
import dbp.techcall.education.infrastructure.EducationRepository;
import dbp.techcall.professor.dto.NewProfessorDto;
import dbp.techcall.professor.infrastructure.ProfessorRepository;
import dbp.techcall.review.exceptions.ResourceNotFoundException;
import dbp.techcall.school.domain.School;
import dbp.techcall.school.infrastructure.SchoolRepository;
import dbp.techcall.workExperience.domain.WorkExperience;
import dbp.techcall.workExperience.dto.BasicExperienceRequest;
import dbp.techcall.workExperience.dto.BasicExperienceResponse;
import dbp.techcall.workExperience.infrastructure.WorkExperienceRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

@Service
public class ProfessorService implements IProfessorService {

    @Autowired
    private ProfessorRepository professorRepository;

    @Autowired
    private EducationRepository educationRepository;

    @Autowired
    private SchoolRepository schoolRepository;

    @Autowired
    private WorkExperienceRepository workExperienceRepository;

    private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @Autowired
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


    public List<BasicEducationResponse> getEducationsById(Long id) {
        return educationRepository.findAllByProfessorId(id);
    }

    public void addEducation(Long id, BasicEducationRequest education) {
        Professor professor = findById(id);
        School school = schoolRepository.findByName(education.getSchoolName());

        if (school == null) {
            School newSchool = new School();
            newSchool.setName(education.getSchoolName());
            newSchool.setImgUrl(education.getImgUrl());
            schoolRepository.save(newSchool);
        }
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

        Education newEducation = new Education();
        newEducation.setDegree(education.getDegree());
        newEducation.setDescription(education.getDescription());
        newEducation.setStartDate(LocalDate.parse(education.getStartDate(), formatter));
        newEducation.setEndDate(LocalDate.parse(education.getEndDate(), formatter));
        newEducation.setProfessor(professor);
        newEducation.setSchool(school);
        educationRepository.save(newEducation);

        professor.getEducations().add(newEducation);

        professorRepository.save(professor);

    }

    public void addExperience(Long id, BasicExperienceRequest experience) {
        Professor professor = findById(id);

        if (professor == null) {
            throw new ResourceNotFoundException("Professor not found");
        }

        WorkExperience newExperience = new WorkExperience();
        newExperience.setTitle(experience.getTitle());
        newExperience.setEmployer(experience.getEmployer());
        newExperience.setDescription(experience.getDescription());
        newExperience.setStartDate(experience.getStartDate());
        newExperience.setEndDate(experience.getEndDate());
        newExperience.setProfessor(professor);

        professor.getWorkExperiences().add(newExperience);

        professorRepository.save(professor);
    }

    public Page<List<BasicExperienceResponse>> getExperiencesById(Long id, Integer page) {
        Pageable pageable = PageRequest.of(page, 10, Sort.by("startDate").descending());
        return workExperienceRepository.findWorkExperiencesByProfessorId(id, pageable);
    }
}
