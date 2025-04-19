package yong.petdoc.web.review;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import yong.petdoc.service.review.ReviewService;
import yong.petdoc.web.review.dto.request.CreateReviewRequest;

@RequiredArgsConstructor
@RequestMapping("/api/reviews")
@RestController
public class ReviewController {

    private final ReviewService reviewService;

    @PostMapping
    public ResponseEntity<Void> createReview(@RequestBody CreateReviewRequest request) {
        reviewService.save(request);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .build();
    }
}
