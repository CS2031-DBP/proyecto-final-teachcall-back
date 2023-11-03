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
        Professor professor = professorService.findById(request.getProfessorId());
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


    /**
     * Get top 5 reviews by professorId
     * and use a Sort Object to sort by rating and createdAt in descending order (highest rating first)
     *
     *
     * @param professorId
     * @return List<ReviewResponse> - List of top 5 reviews by professorId
     * <pre>
     * Example:<br>
     * [<br>
     *  {<br>
     *      "body": "This is a review",<br>
     *      "rating": 5,<br>
     *      "studentName": "John",<br>
     *      "createdAt": "2021-04-20T00:00:00.000+00:00"<br>
     *  }<br>
     *]<br>
     *</pre>
     *
     *
     */
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
                    reviewResponse.setCreatedAt(review.getCreatedAt().toString());
            return reviewResponse;
        }).toList();
    }

    /**
     * Update review by id and review object passed in request body (PUT)
     *
     * @throws ResourceNotFoundException if review is not found
     * @param id  id of review to update
     * @param review review object passed in request body
     */
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

    /**
     * Get average rating and review count by professorId using a custom query
     *
     * @param professorId
     * @return ProfessorRatingInfo - object containing average rating and review count
     * @see dbp.techcall.review.infrastructure.ReviewRepository#getAverageRating(Long)
     * @see dbp.techcall.review.dto.ProfessorRatingInfo
     */
    public ProfessorRatingInfo getAverageRating(Long professorId) {
        List<Double> info =  reviewRepository.getAverageRating(professorId);

        ProfessorRatingInfo professorRatingInfo = new ProfessorRatingInfo();
        professorRatingInfo.setProfessorId(professorId);
        professorRatingInfo.setAverageRating(info.get(0));
        professorRatingInfo.setReviewCount(info.get(1).intValue());

        return professorRatingInfo;
    }

    /**
     * Update review by id and review object passed in request body (PATCH)
     * Due to being a PATCH request, only update fields that are not null
     *
     * @implNote It would be useful to implement something like <a href="https://datatracker.ietf.org/doc/html/rfc6902">RFC 6902</a> (JSON Patch) in the future.
     *
     *
     * @throws ResourceNotFoundException if review is not found
     * @param id  id of review to update
     * @param request review object passed in request body
     */
    public void patchUpdateReview(Long id, ReviewRequest request) {
        Review reviewToUpdate = reviewRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Review not found"));

        if (request.getBody() != null) reviewToUpdate.setBody(request.getBody());
        if (request.getRating() != null) reviewToUpdate.setRating(request.getRating());
        reviewToUpdate.setUpdatedAt(new Date(System.currentTimeMillis()));

        reviewRepository.save(reviewToUpdate);
    }
}
