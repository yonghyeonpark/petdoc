package yong.petdoc.dto.response.vetfacility;

import java.util.List;

import yong.petdoc.domain.vetfacility.VetFacility;
import yong.petdoc.dto.response.review.ReviewResponse;

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