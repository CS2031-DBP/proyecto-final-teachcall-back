package dbp.techcall.professor.domain;

import com.amazonaws.services.s3.model.ListObjectsV2Request;
import com.amazonaws.services.s3.model.ListObjectsV2Result;
import com.amazonaws.services.s3.model.S3ObjectSummary;
import dbp.techcall.auth.dto.JwtRes;
import dbp.techcall.category.domain.Category;
import dbp.techcall.category.infrastructure.CategoryRepository;
import dbp.techcall.education.domain.Education;
import dbp.techcall.education.dto.BasicEducationRequest;
import dbp.techcall.education.infrastructure.EducationRepository;
import dbp.techcall.professor.dto.*;
import dbp.techcall.professor.exceptions.AlreadyCompletedTourException;
import dbp.techcall.professor.exceptions.EmptyFileException;
import dbp.techcall.professor.infrastructure.BasicProfessorResponse;
import dbp.techcall.professor.infrastructure.ProfessorRepository;
import dbp.techcall.review.exceptions.ResourceNotFoundException;
import dbp.techcall.s3.StorageService;
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
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Objects;
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
    CategoryRepository categoryRepository;

    @Autowired
    private WorkExperienceRepository workExperienceRepository;

    private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private StorageService storageService;

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
        Optional<Professor> professor = professorRepository.findById(professorId);

        if (professor.isEmpty()) {
            throw new RuntimeException("Professor not found");
        }

        return professor.get();
    }

    public void addEducation(String email, BasicEducationRequest education) {
        Professor professor = findByEmail(email);
        School school = schoolRepository.findByName(education.getSchoolName());

        if (school == null) {
            School newSchool = new School();
            newSchool.setName(education.getSchoolName());
            newSchool.setImgUrl(education.getImgUrl());
            schoolRepository.save(newSchool);

            school = newSchool;

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

    public Page<BasicExperienceResponse> getExperiencesById(Long id, Integer page) {
        Pageable pageable = PageRequest.of(page, 10, Sort.by("startDate").descending());
        return workExperienceRepository.findWorkExperiencesByProfessorId(id, pageable);
    }

    public void setCompletedTour(String id) {
        Professor professor = findByEmail(id);

        if (professor == null) {
            throw new ResourceNotFoundException("Professor not found");
        }

        if (professor.getTourCompleted()) {
            throw new AlreadyCompletedTourException("Professor already completed tour");
        }

        professor.setTourCompleted(true);
        professorRepository.save(professor);
    }

    public void setDescription(String email, String description) {
        Professor professor = findByEmail(email);

        if (professor == null) {
            throw new ResourceNotFoundException("Professor not found");
        }

        professor.setDescription(description);
        professorRepository.save(professor);
    }

    public void addCategoriesByEmail(AddCategoriesReq categories) {
        Professor professor = findByEmail(categories.getEmail());

        if (professor == null) {
            throw new ResourceNotFoundException("Professor not found");
        }

        for (Long categoryId : categories.getCategories()) {
            Optional<Category> category = categoryRepository.findById(categoryId);

            if (category.isEmpty()) {
                throw new ResourceNotFoundException("Category not found");
            }

            professor.getCategories().add(category.get());
        }

        professorRepository.save(professor);

    }

    public Page<BasicProfessorResponse> getProfessors(Integer page) {
        Pageable pageable = PageRequest.of(page, 10);
        return professorRepository.findAllProfessorsWithPagination(pageable);
    }


    public Page<BasicProfessorResponse> getProfessorsByCategory(Integer page, String category) {
        Pageable pageable = PageRequest.of(page, 10);
        return professorRepository.findAllProfessorsByCategoryWithPagination(pageable, category);
    }

    public Double getRating(String email) {
        return professorRepository.getRating(email);
    }

    public String getDescription(String email) {
        return professorRepository.findByEmail(email).getDescription();
    }

    public LastExperienceDto getExperience(String email) {
        Professor professor = professorRepository.findByEmail(email);
        if (professor == null) {
            throw new ResourceNotFoundException("Professor not found");
        }
        return professorRepository.getExperience(professor.getId());
    }

    public Page<BasicEducation> getEducationWithPagination(String email, int page) {
        Professor professor = professorRepository.findByEmail(email);
        Pageable pageable = PageRequest.of(page, 5, Sort.by("endDate"));

        return educationRepository.getEducationWithPagination(professor.getId(), pageable);
    }

    public BasicProfessorResponse getProfessorById(Long id) {
        return professorRepository.findProfessor(id);
    }

    //change password
    public Boolean changePassword(String email, String password) {
        Professor professor = findByEmail(email);
        System.out.println(password);
        System.out.println(passwordEncoder.encode(password));
        professor.setPassword(passwordEncoder.encode(password));
        professor.setUpdatedAt(ZonedDateTime.now());
        professorRepository.save(professor);
        return true;
    }

    public static EditProfessorDTO mapProfessorToEditProfessorDTO(Professor professor) {
        EditProfessorDTO editProfessorDTO = new EditProfessorDTO();
        editProfessorDTO.setFirstName(professor.getFirstName());
        editProfessorDTO.setLastName(professor.getLastName());
        editProfessorDTO.setDescription(professor.getDescription());
//        editProfessorDTO.setPhoto(professor.getPhoto());
        return editProfessorDTO;
    }


    public boolean addMedia(Professor professor, MultipartFile img, String fileName) {

        if (img.isEmpty()) throw new EmptyFileException("Empty file");

        String extension = img.getOriginalFilename().substring(img.getOriginalFilename().lastIndexOf("."));
        String provObjectKey = professor.getId().toString() + "/profile/" + fileName + extension;

        String objectKey = Objects.equals(fileName, "cover")
                ? professor.getCoverPicKey()
                : professor.getProfilePicKey();

        try {
            String key = storageService.uploadFile(img, objectKey == null ? provObjectKey : objectKey);

            if (fileName.equals("cover")) professor.setCoverPicKey(key);
            else if (fileName.equals("pp")) professor.setProfilePicKey(key);

            professorRepository.save(professor);
            return true;

        } catch (Exception e) {
            throw new RuntimeException("\nError on ProfessorService - addProfilePic on uploading file to S3");
        }

    }

    public String getMediaUrl(Professor professor, String fileName) {
        try {
            String objectKey = Objects.equals(fileName, "cover")
                    ? professor.getCoverPicKey()
                    : professor.getProfilePicKey();

            if (objectKey == null) throw new ResourceNotFoundException("User does not have a " + fileName + "image");

            String url = storageService.generatePresignedUrl(objectKey);
            if (url.isEmpty()) throw new ResourceNotFoundException("User does not have a " + fileName + "image");

            return url;
        } catch (Exception e) {
            throw new RuntimeException("\nError on ProfessorService - getMediaUrl on generating presigned url from S3");
        }
    }

    public boolean deleteMedia(Professor professor, String fileName) {
        try {
            String objectKey = Objects.equals(fileName, "cover")
                    ? professor.getCoverPicKey()
                    : professor.getProfilePicKey();
            if (objectKey == null) throw new ResourceNotFoundException("User does not have a " + fileName + "image");

            storageService.deleteFile(objectKey);
            return true;
        } catch (Exception e) {
            throw new RuntimeException("\nError on ProfessorService - deleteMedia on deleting file from S3");
        }

    }
}










