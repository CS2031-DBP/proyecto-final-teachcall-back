package dbp.techcall.review.domain;

import com.nimbusds.jose.crypto.impl.PRFParams;
import dbp.techcall.professor.domain.Professor;
import dbp.techcall.professor.domain.ProfessorService;
import dbp.techcall.review.dto.ProfessorRatingInfo;
import dbp.techcall.review.dto.ReviewRequest;
import dbp.techcall.review.dto.ReviewResponse;
import dbp.techcall.review.dto.UpdateReviewRequest;
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
import java.util.UUID;

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


    /**
     * Save a new review
     *
     * @throws UsernameNotFoundException if professor or student is not found
     * @param request review object passed in request body
     */
    public void saveReview(ReviewRequest request) {
        Professor professor = professorService.findById(request.getProfessorId());
        Student student = studentService.getStudentById(request.getStudentId());

        if (professor == null || student == null) throw new UsernameNotFoundException("Professor and Student must not be null");

        Review newReview = modelMapper.map(request, Review.class);
        newReview.setCreatedAt(new Date(System.currentTimeMillis()));

        reviewRepository.save(newReview);
    }

    /**
     * Get reviews by professorId using pagination.
     * Pagination parameters must be passed in request params
     * <pre>
     *     Example: /review/1?page=0&size=10&sort=createdAt,desc
     *     page - page number
     *     size - number of reviews per page
     *     sort - sort by createdAt in descending order
     * </pre>
     * @param email professor email
     * @param pageable Pageable object containing pagination parameters
     * @return Page<ReviewResponse> - Page of reviews by professorId
     */
    public Page<ReviewResponse> getReviewsByProfessorId(String email, Pageable pageable) {
        System.out.println("\n\n\n" + email + "\n\n\n");
        Professor professor = professorService.findByEmail(email);
        Page<Review> page = reviewRepository.findAllByProfessorId(professor.getId(), pageable);
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
     * @param review An UpdateReviewRequest object passed in request body
     */
    public void updateReview(UUID id, UpdateReviewRequest review) {
        Review reviewToUpdate = reviewRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Review not found"));

        reviewToUpdate.setBody(review.getBody());
        reviewToUpdate.setRating(review.getRating());
        reviewToUpdate.setUpdatedAt(new Date(System.currentTimeMillis()));

        reviewRepository.save(reviewToUpdate);
    }

    public void deleteReview(UUID id) {
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
        List<Object[]> info =  reviewRepository.getAverageRating(professorId);

        ProfessorRatingInfo professorRatingInfo = new ProfessorRatingInfo();
        professorRatingInfo.setProfessorId(professorId);
        professorRatingInfo.setAverageRating((Double) info.get(0)[0]);
        professorRatingInfo.setReviewCount((Long) info.get(0)[1]);

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
    public void patchUpdateReview(UUID id, ReviewRequest request) {
        Review reviewToUpdate = reviewRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Review not found"));

        if (request.getBody() != null) reviewToUpdate.setBody(request.getBody());
        if (request.getRating() != null) reviewToUpdate.setRating(request.getRating());
        reviewToUpdate.setUpdatedAt(new Date(System.currentTimeMillis()));

        reviewRepository.save(reviewToUpdate);
    }
}
