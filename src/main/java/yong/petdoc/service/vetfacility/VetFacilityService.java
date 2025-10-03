package yong.petdoc.service.vetfacility;

import static yong.petdoc.exception.ErrorCode.*;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import yong.petdoc.domain.vetfacility.VetFacility;
import yong.petdoc.domain.vetfacility.VetFacilityType;
import yong.petdoc.dto.request.vetfacility.VetFacilityListRequest;
import yong.petdoc.dto.response.vetfacility.VetFacilityListResponse;
import yong.petdoc.dto.response.vetfacility.VetFacilityResponse;
import yong.petdoc.exception.CustomException;
import yong.petdoc.repository.vetfacility.VetFacilityRepository;
import yong.petdoc.service.bookmark.BookmarkService;
import yong.petdoc.service.review.ReviewService;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class VetFacilityService {

	private final VetFacilityRepository vetFacilityRepository;
	private final ReviewService reviewService;
	private final BookmarkService bookmarkService;

	public VetFacilityResponse getVetFacilityById(Long facilityId) {
		VetFacility vetFacility = vetFacilityRepository.findById(facilityId)
			.orElseThrow(() -> new CustomException(VET_FACILITY_NOT_FOUND));

		return VetFacilityResponse.from(
			vetFacility,
			reviewService.getReviewsByVetFacilityId(facilityId),
			false // bookmarkService.isBookmarked(facilityId, userId)
		);
	}

	public List<VetFacilityListResponse> getVetFacilities(VetFacilityListRequest request) {
		return vetFacilityRepository.findVetFacilities(
				request.minLatitude(),
				request.maxLatitude(),
				request.minLongitude(),
				request.maxLongitude(),
				VetFacilityType.fromName(request.type())
			).stream()
			.map(VetFacilityListResponse::from)
			.toList();
	}
}