package yong.petdoc.service.review;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import yong.petdoc.domain.review.Review;
import yong.petdoc.domain.review.ReviewRepository;
import yong.petdoc.domain.user.User;
import yong.petdoc.domain.user.UserRepository;
import yong.petdoc.domain.vetfacility.VetFacility;
import yong.petdoc.domain.vetfacility.VetFacilityRepository;
import yong.petdoc.exception.CustomException;
import yong.petdoc.exception.ErrorCode;
import yong.petdoc.web.review.dto.request.CreateReviewRequest;
import yong.petdoc.web.review.dto.response.ReviewResponse;

import java.util.List;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class ReviewService {

    private final UserRepository userRepository;
    private final VetFacilityRepository vetFacilityRepository;
    private final ReviewRepository reviewRepository;

    @Transactional
    public void save(CreateReviewRequest request) {
        User user = userRepository.findById(request.userId())
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));
        VetFacility vetFacility = vetFacilityRepository.findById(request.vetFacilityId())
                .orElseThrow(() -> new CustomException(ErrorCode.VET_FACILITY_NOT_FOUND));

        Review review = new Review(
                request.comment(),
                user,
                vetFacility
        );
        reviewRepository.save(review);
    }

    public List<ReviewResponse> getReviewsByVetFacilityId(Long vetFacilityId) {
        return reviewRepository.findByVetFacilityId(vetFacilityId).stream()
                .map(ReviewResponse::from)
                .toList();
    }
}
