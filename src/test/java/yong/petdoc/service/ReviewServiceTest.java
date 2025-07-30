package yong.petdoc.service;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import yong.petdoc.domain.review.Review;
import yong.petdoc.dto.request.review.CreateReviewRequest;
import yong.petdoc.dto.request.review.DeleteReviewRequest;
import yong.petdoc.dto.request.review.GetMyReviewsRequest;
import yong.petdoc.dto.request.review.UpdateReviewRequest;
import yong.petdoc.dto.response.review.MyReviewsResponse;
import yong.petdoc.repository.review.ReviewRepository;
import yong.petdoc.service.review.ReviewService;

@Transactional
@ActiveProfiles("test")
@SpringBootTest
public class ReviewServiceTest {

	@Autowired
	private ReviewService reviewService;

	@Autowired
	private ReviewRepository reviewRepository;

	@DisplayName("리뷰 생성 시 DB에 저장된다.")
	@Test
	void createReview() {
		// given
		Long vetFacilityId = 1L;
		Long userId = 1L;
		String comment = "good";

		// when
		CreateReviewRequest request = new CreateReviewRequest(comment, userId);
		reviewService.createReview(vetFacilityId, request);

		// then
		Review review = reviewRepository.findAll().get(0);
		assertThat(review.getUser().getId()).isEqualTo(userId);
		assertThat(review.getVetFacility().getId()).isEqualTo(vetFacilityId);
		assertThat(review.getComment()).isEqualTo(comment);
	}

	@DisplayName("리뷰 수정 시 정보가 정상적으로 변경된다.")
	@Test
	void updateReview() {
		// given
		Long vetFacilityId = 1L;
		Long userId = 1L;
		String comment = "good";
		reviewService.createReview(vetFacilityId, new CreateReviewRequest(comment, userId));

		// when
		String newComment = "new good";
		UpdateReviewRequest request = new UpdateReviewRequest(newComment, userId);
		reviewService.updateReview(vetFacilityId, request);

		// then
		Review review = reviewRepository.findAll().get(0);
		assertThat(review.getComment()).isEqualTo(newComment);
	}

	@DisplayName("리뷰 삭제 시 DB에서 데이터가 정상적으로 삭제된다.")
	@Test
	void deleteReview() {
		// given
		Long vetFacilityId = 1L;
		Long userId = 1L;
		String comment = "good";
		reviewService.createReview(vetFacilityId, new CreateReviewRequest(comment, userId));

		// when
		DeleteReviewRequest request = new DeleteReviewRequest(userId);
		reviewService.deleteReview(vetFacilityId, request);

		// then
		assertThat(reviewRepository.findAll().size()).isEqualTo(0);
	}

	@DisplayName("내가 생성한 리뷰 목록을 페이지네이션으로 조회하면 요청한 페이지 정보와 데이터가 응답된다.")
	@Test
	void getMyReviews_withPagination() {
		// given
		Long userId = 1L;
		String comment = "good";
		GetMyReviewsRequest request = new GetMyReviewsRequest(userId);

		for (long i = 1; i <= 7; i++) {
			reviewService.createReview(i, new CreateReviewRequest(comment, userId));
		}

		// when
		Pageable pageable = PageRequest.of(1, 2, Sort.by("createdAt").descending());
		MyReviewsResponse myReviews = reviewService.getMyReviews(request, pageable);

		// then
		assertThat(myReviews.reviews().size()).isEqualTo(2);
		assertThat(myReviews.page()).isEqualTo(1);
		assertThat(myReviews.size()).isEqualTo(2);
		assertThat(myReviews.totalPages()).isEqualTo(4);
		assertThat(myReviews.totalElements()).isEqualTo(7);
	}
}
