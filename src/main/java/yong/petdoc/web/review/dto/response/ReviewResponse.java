package yong.petdoc.web.review.dto.response;

import yong.petdoc.domain.review.Review;

import java.time.LocalDateTime;

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
