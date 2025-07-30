package yong.petdoc.dto.request.review;

public record CreateReviewRequest(
	String comment,
	Long userId
) {
}
