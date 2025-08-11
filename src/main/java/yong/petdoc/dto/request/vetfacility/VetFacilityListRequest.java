package yong.petdoc.dto.request.vetfacility;

public record VetFacilityListRequest(
	Double minLatitude,
	Double maxLatitude,
	Double minLongitude,
	Double maxLongitude,
	String type
) {
}
