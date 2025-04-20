package yong.petdoc.service.vetfacility;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import yong.petdoc.domain.vetfacility.VetFacility;
import yong.petdoc.domain.vetfacility.VetFacilityRepository;
import yong.petdoc.exception.CustomException;
import yong.petdoc.service.bookmark.BookmarkService;
import yong.petdoc.service.redis.RedisService;
import yong.petdoc.service.review.ReviewService;
import yong.petdoc.service.vetfacility.dto.RecentVetFacilityDto;
import yong.petdoc.web.vetfacility.dto.request.GetRecentVetFacilitiesRequest;
import yong.petdoc.web.vetfacility.dto.request.GetVetFacilityRequest;
import yong.petdoc.web.vetfacility.dto.response.VetFacilityResponse;

import java.util.List;

import static yong.petdoc.constant.redis.RedisKey.VET_FACILITY_BOOKMARK_PREFIX;
import static yong.petdoc.constant.redis.RedisKey.VET_FACILITY_RECENT_BY_USER_PREFIX;
import static yong.petdoc.exception.ErrorCode.JSON_SERIALIZATION_FAILED;
import static yong.petdoc.exception.ErrorCode.VET_FACILITY_NOT_FOUND;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class VetFacilityService {

    private final VetFacilityRepository vetFacilityRepository;
    private final RedisService redisService;
    private final ReviewService reviewService;
    private final BookmarkService bookmarkService;
    private final ObjectMapper objectMapper;

    public VetFacilityResponse getVetFacilityById(Long facilityId, GetVetFacilityRequest request) {
        Long userId = request.userId();
        VetFacility vetFacility = vetFacilityRepository.findById(facilityId)
                .orElseThrow(() -> new CustomException(VET_FACILITY_NOT_FOUND));
        Long bookmarkCount = redisService.getSizeOfSet(VET_FACILITY_BOOKMARK_PREFIX + facilityId);

        // 수의 시설 정보를 JSON으로 직렬화 후 최근 조회 목록에 저장
        String facilityJson;
        try {
            facilityJson = objectMapper.writeValueAsString(
                    new RecentVetFacilityDto(
                            facilityId,
                            vetFacility.getName(),
                            vetFacility.getLotAddress(),
                            vetFacility.getRoadAddress()
                    )
            );
        } catch (JsonProcessingException e) {
            throw new CustomException(JSON_SERIALIZATION_FAILED, e);
        }
        redisService.addRecentFacility(VET_FACILITY_RECENT_BY_USER_PREFIX + userId, facilityJson);

        return VetFacilityResponse.from(
                vetFacility,
                bookmarkCount,
                reviewService.getReviewsByVetFacilityId(facilityId),
                bookmarkService.isBookmarked(facilityId, userId)
        );
    }

    public List<RecentVetFacilityDto> getRecentVetFacilities(GetRecentVetFacilitiesRequest request) {
        return redisService.getRecentVetFacilities(VET_FACILITY_RECENT_BY_USER_PREFIX + request.userId()).stream()
                .map(json -> {
                    try {
                        return objectMapper.readValue(json, RecentVetFacilityDto.class);
                    } catch (JsonProcessingException e) {
                        throw new CustomException(JSON_SERIALIZATION_FAILED, e);
                    }
                })
                .toList();
    }
}