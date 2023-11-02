package dbp.techcall.review.domain;

import dbp.techcall.professor.domain.Professor;
import dbp.techcall.professor.domain.ProfessorService;
import dbp.techcall.review.dto.ProfessorRatingInfo;
import dbp.techcall.review.dto.ReviewRequest;
import dbp.techcall.review.dto.ReviewResponse;
import dbp.techcall.review.exceptions.ResourceNotFoundException;
import dbp.techcall.review.infrastructure.ReviewRepository;
import dbp.techcall.student.domain.Student;
import dbp.techcall.student.domain.StudentService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class ReviewService {

    @Autowired
    private ReviewRepository reviewRepository;

    @Autowired
    private ProfessorService professorService;

    @Autowired
    private StudentService studentService;

    @Autowired
    private ModelMapper modelMapper;

    public void saveReview(ReviewRequest request) {
        Professor professor = professorService.getProfessorById(request.getProfessorId());
        Student student = studentService.getStudentById(request.getStudentId());

        if (professor == null || student == null) throw new UsernameNotFoundException("Professor and Student must not be null");

        Review newReview = modelMapper.map(request, Review.class);
        newReview.setCreatedAt(new Date(System.currentTimeMillis()));

        reviewRepository.save(newReview);
    }

    public Page<ReviewResponse> getReviewsByProfessorId(Long professorId, Pageable pageable) {
        Page<Review> page = reviewRepository.findAllByProfessorId(professorId, pageable);
        return page
                .map(review -> {
                    ReviewResponse reviewResponse = new ReviewResponse();
                    reviewResponse.setBody(review.getBody());
                    reviewResponse.setRating(review.getRating());
                    reviewResponse.setStudentName(review.getStudent().getFirstName());
            return reviewResponse;
        });
    }

    public List<ReviewResponse> getTopReviewsByProfessorId(Long professorId) {

        Sort sort = Sort.by(
                Sort.Order.desc("rating"),
                Sort.Order.desc("createdAt")
        );

        Pageable pageable = PageRequest.of(0, 5, sort);
        Page<Review> page = reviewRepository.findAllByProfessorId(professorId,pageable);

        return page
                .map(review -> {
                    ReviewResponse reviewResponse = new ReviewResponse();
                    reviewResponse.setBody(review.getBody());
                    reviewResponse.setRating(review.getRating());
                    reviewResponse.setStudentName(review.getStudent().getFirstName());
            return reviewResponse;
        }).toList();
    }


    public void updateReview(Long id, Review review) {
        Review reviewToUpdate = reviewRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Review not found"));

        reviewToUpdate.setBody(review.getBody());
        reviewToUpdate.setRating(review.getRating());
        reviewToUpdate.setUpdatedAt(new Date(System.currentTimeMillis()));

        reviewRepository.save(reviewToUpdate);
    }

    public void deleteReview(Long id) {
        Review reviewToDelete = reviewRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Review not found"));
        reviewRepository.delete(reviewToDelete);
    }

    public ProfessorRatingInfo getAverageRating(Long professorId) {
        List<Double> info =  reviewRepository.getAverageRating(professorId);

        ProfessorRatingInfo professorRatingInfo = new ProfessorRatingInfo();
        professorRatingInfo.setProfessorId(professorId);
        professorRatingInfo.setAverageRating(info.get(0));
        professorRatingInfo.setReviewCount(info.get(1).intValue());

        return professorRatingInfo;
    }
}
