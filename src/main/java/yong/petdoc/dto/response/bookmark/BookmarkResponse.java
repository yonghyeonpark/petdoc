package yong.petdoc.dto.response.bookmark;

import yong.petdoc.domain.vetfacility.VetFacility;

public record BookmarkResponse(
	Long bookmarkId,
	String vetFacilityName,
	String lotAddress,
	String roadAddress
) {

	public static BookmarkResponse from(Long bookmarkId, VetFacility vetFacility) {
		return new BookmarkResponse(
			bookmarkId,
			vetFacility.getName(),
			vetFacility.getLotAddress(),
			vetFacility.getRoadAddress()
		);
	}
}
