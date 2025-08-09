package yong.petdoc.service.vetfacility;

import static yong.petdoc.constant.redis.RedisKey.*;
import static yong.petdoc.exception.ErrorCode.*;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.RequiredArgsConstructor;
import yong.petdoc.domain.vetfacility.VetFacility;
import yong.petdoc.domain.vetfacility.VetFacilityType;
import yong.petdoc.dto.request.vetfacility.GetRecentVetFacilitiesRequest;
import yong.petdoc.dto.request.vetfacility.GetVetFacilityRequest;
import yong.petdoc.dto.request.vetfacility.VetFacilityListRequest;
import yong.petdoc.dto.response.vetfacility.RecentVetFacilityDto;
import yong.petdoc.dto.response.vetfacility.VetFacilityListResponse;
import yong.petdoc.dto.response.vetfacility.VetFacilityResponse;
import yong.petdoc.exception.CustomException;
import yong.petdoc.repository.vetfacility.VetFacilityRepository;
import yong.petdoc.service.bookmark.BookmarkService;
import yong.petdoc.service.redis.RedisService;
import yong.petdoc.service.review.ReviewService;

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

	public List<VetFacilityListResponse> getVetFacilities(VetFacilityListRequest request) {
		return vetFacilityRepository.findVetFacilities(
				request.latitude(),
				request.longitude(),
				request.radius(),
				VetFacilityType.fromName(request.type())
			).stream()
			.map(VetFacilityListResponse::from)
			.toList();
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