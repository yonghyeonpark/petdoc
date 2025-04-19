package yong.petdoc.web.review.dto.request;

public record CreateReviewRequest(
        String comment,
        Long userId
) {
}
