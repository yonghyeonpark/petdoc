package yong.petdoc.dto.request.vetfacility;

public record VetFacilityListRequest(
	Double latitude,
	Double longitude,
	Integer radius,
	String type
) {
}
