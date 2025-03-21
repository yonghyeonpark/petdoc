package yong.petdoc.service.vetfacility;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import yong.petdoc.domain.vetfacility.VetFacility;
import yong.petdoc.domain.vetfacility.VetFacilityRepository;
import yong.petdoc.exception.CustomException;
import yong.petdoc.web.vetfacility.dto.response.VetFacilityResponse;

import static yong.petdoc.exception.ErrorCode.FACILITY_NOT_FOUND;

@RequiredArgsConstructor
@Transactional
@Service
public class VetFacilityService {

    private final VetFacilityRepository vetFacilityRepository;

    public VetFacilityResponse getVetFacilityById(Long facilityId) {
        VetFacility vetFacility = vetFacilityRepository.findById(facilityId)
                .orElseThrow(() -> new CustomException(FACILITY_NOT_FOUND));
        return VetFacilityResponse.from(vetFacility);
    }
}
