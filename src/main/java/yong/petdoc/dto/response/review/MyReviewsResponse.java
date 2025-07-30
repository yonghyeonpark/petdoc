package yong.petdoc.dto.response.review;

import java.util.List;

public record MyReviewsResponse(
	List<ReviewResponse> reviews,
	int page,
	int size,
	int totalPages,
	long totalElements,
	boolean hasNext
) {
}
