package yong.petdoc.dto.response.vetfacility;

public record RecentVetFacilityDto(
	Long facilityId,
	String name,
	String lotAddress,
	String roadAddress
) {
}
