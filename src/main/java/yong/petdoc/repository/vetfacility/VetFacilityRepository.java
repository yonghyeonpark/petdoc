package yong.petdoc.repository.vetfacility;

import org.springframework.data.jpa.repository.JpaRepository;

import yong.petdoc.domain.vetfacility.VetFacility;

public interface VetFacilityRepository extends JpaRepository<VetFacility, Long>, VetFacilityCustomRepository {
}
