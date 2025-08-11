package yong.petdoc.repository.vetfacility;

import java.util.List;

import yong.petdoc.domain.vetfacility.VetFacility;
import yong.petdoc.domain.vetfacility.VetFacilityType;

public interface VetFacilityCustomRepository {

	List<VetFacility> findVetFacilities(
		Double minLatitude,
		Double maxLatitude,
		Double minLongitude,
		Double maxLongitude,
		VetFacilityType type
	);
}
