package yong.petdoc.dto.request.review;

public record UpdateReviewRequest(
	String comment,
	Long userId
) {
}