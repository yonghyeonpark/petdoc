package yong.petdoc.repository.vetfacility;

import java.util.List;

import yong.petdoc.domain.vetfacility.VetFacility;
import yong.petdoc.domain.vetfacility.VetFacilityType;

public interface VetFacilityCustomRepository {

	List<VetFacility> findVetFacilities(
		Double latitude,
		Double longitude,
		Integer radius,
		VetFacilityType type
	);
}
