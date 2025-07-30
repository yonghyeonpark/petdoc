package yong.petdoc.dto.response.review;

import java.time.LocalDateTime;

import yong.petdoc.domain.review.Review;

public record ReviewResponse(
	Long id,
	String comment,
	Long userId,
	LocalDateTime createdAt
) {

	public static ReviewResponse from(Review review) {
		return new ReviewResponse(
			review.getId(),
			review.getComment(),
			review.getUser().getId(),
			review.getCreatedAt()
		);
	}
}
