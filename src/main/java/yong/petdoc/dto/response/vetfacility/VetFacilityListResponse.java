package yong.petdoc.dto.response.vetfacility;

import org.locationtech.jts.geom.Point;

import yong.petdoc.domain.vetfacility.VetFacility;

public record VetFacilityListResponse(
	Long id,
	String name,
	String type,
	double latitude,
	double longitude
) {

	public static VetFacilityListResponse from(VetFacility vetFacility) {
		Point location = vetFacility.getLocation();
		return new VetFacilityListResponse(
			vetFacility.getId(),
			vetFacility.getName(),
			vetFacility.getVetFacilityType().name(),
			location.getY(),
			location.getX()
		);
	}
}
