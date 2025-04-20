package yong.petdoc.web.review.dto.response;

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
