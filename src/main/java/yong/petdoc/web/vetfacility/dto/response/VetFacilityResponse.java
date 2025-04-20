package yong.petdoc.web.vetfacility.dto.response;

import yong.petdoc.domain.vetfacility.VetFacility;
import yong.petdoc.web.review.dto.response.ReviewResponse;

import java.util.List;

public record VetFacilityResponse(
        Long id,
        String name,
        String lotAddress,
        String roadAddress,
        String phoneNumber,
        String placeUrl,
        long bookmarkCount,
        List<ReviewResponse> reviews,
        boolean isBookmarked
) {

    public static VetFacilityResponse from(
            VetFacility vetFacility,
            long bookmarkCount,
            List<ReviewResponse> reviews,
            boolean isBookmarked
    ) {
        return new VetFacilityResponse(
                vetFacility.getId(),
                vetFacility.getName(),
                vetFacility.getLotAddress(),
                vetFacility.getRoadAddress(),
                vetFacility.getPhoneNumber(),
                vetFacility.getPlaceUrl(),
                bookmarkCount,
                reviews,
                isBookmarked
        );
    }
}