package yong.petdoc.repository.vetfacility;

import java.math.BigDecimal;
import java.util.List;

import yong.petdoc.domain.vetfacility.VetFacility;
import yong.petdoc.domain.vetfacility.VetFacilityType;

public interface VetFacilityCustomRepository {

	List<VetFacility> findVetFacilities(
		BigDecimal latitude,
		BigDecimal longitude,
		Integer radius,
		VetFacilityType type
	);
}
