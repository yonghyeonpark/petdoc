package yong.petdoc.service.vetfacility;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import yong.petdoc.domain.vetfacility.VetFacility;
import yong.petdoc.domain.vetfacility.VetFacilityRepository;
import yong.petdoc.exception.CustomException;
import yong.petdoc.service.redis.RedisService;
import yong.petdoc.web.vetfacility.dto.response.VetFacilityResponse;

import static yong.petdoc.exception.ErrorCode.VET_FACILITY_NOT_FOUND;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class VetFacilityService {

    private final VetFacilityRepository vetFacilityRepository;
    private final RedisService redisService;

    public VetFacilityResponse getVetFacilityById(Long facilityId) {
        Long bookmarkCount = redisService.getSizeOfSet(String.valueOf(facilityId));
        VetFacility vetFacility = vetFacilityRepository.findById(facilityId)
                .orElseThrow(() -> new CustomException(VET_FACILITY_NOT_FOUND));
        return VetFacilityResponse.from(vetFacility, bookmarkCount);
    }
}
