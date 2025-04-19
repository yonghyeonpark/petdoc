package yong.petdoc.web.review.dto.request;

public record UpdateReviewRequest(
        String comment,
        Long userId
) {
}