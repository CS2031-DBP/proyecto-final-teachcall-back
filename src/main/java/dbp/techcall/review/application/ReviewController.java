package dbp.techcall.review.application;

import dbp.techcall.review.domain.Review;
import dbp.techcall.review.dto.ProfessorRatingInfo;
import dbp.techcall.review.dto.ReviewRequest;
import dbp.techcall.review.domain.ReviewService;
import dbp.techcall.review.dto.ReviewResponse;
import dbp.techcall.review.dto.UpdateReviewRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/review")
public class ReviewController {

    @Autowired
    private ReviewService reviewService;

    @PostMapping
    public ResponseEntity<String> createReview(@RequestBody ReviewRequest request){
        reviewService.saveReview(request);
        return ResponseEntity.ok("Review created");
    }


    @GetMapping("/{professorId}")
    public ResponseEntity<Page<ReviewResponse>> getReviewsByProfessorId(@PathVariable Long professorId, @RequestParam Integer page, @RequestParam Integer size, @RequestParam(required = false) String sort){
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        return ResponseEntity.ok(reviewService.getReviewsByProfessorId(professorId, pageable));
    }

    @GetMapping("top/{professorId}")
    public ResponseEntity<List<ReviewResponse>> getTopReviewsByProfessorId(@PathVariable Long professorId){
        return ResponseEntity.ok(reviewService.getTopReviewsByProfessorId(professorId));
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> updateReview(@PathVariable UUID id, @RequestBody UpdateReviewRequest review){
        reviewService.updateReview(id, review);
        return ResponseEntity.ok("Review updated");
    }

    @PatchMapping("/{id}")
    public ResponseEntity<String> updateReview(@PathVariable UUID id, @RequestBody ReviewRequest request){
        reviewService.patchUpdateReview(id, request);
        return ResponseEntity.ok("Review updated");
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteReview(@PathVariable UUID id){
        reviewService.deleteReview(id);
        return ResponseEntity.ok("Review deleted");
    }

    @GetMapping("/avg/{professorId}")
    public ResponseEntity<ProfessorRatingInfo> getAverageRating(@PathVariable Long professorId){
        return ResponseEntity.ok(reviewService.getAverageRating(professorId));
    }

}
