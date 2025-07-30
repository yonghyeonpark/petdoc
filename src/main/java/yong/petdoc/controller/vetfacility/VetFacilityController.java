package yong.petdoc.controller.vetfacility;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import yong.petdoc.dto.request.bookmark.CreateBookmarkRequest;
import yong.petdoc.dto.request.bookmark.DeleteBookmarkRequest;
import yong.petdoc.dto.request.review.CreateReviewRequest;
import yong.petdoc.dto.request.review.DeleteReviewRequest;
import yong.petdoc.dto.request.review.UpdateReviewRequest;
import yong.petdoc.dto.request.vetfacility.GetVetFacilityRequest;
import yong.petdoc.dto.response.vetfacility.VetFacilityResponse;
import yong.petdoc.service.bookmark.BookmarkService;
import yong.petdoc.service.review.ReviewService;
import yong.petdoc.service.vetfacility.VetFacilityService;

@RequiredArgsConstructor
@RequestMapping("/api/facilities")
@RestController
public class VetFacilityController {

	private final VetFacilityService vetFacilityService;
	private final ReviewService reviewService;
	private final BookmarkService bookmarkService;

	@GetMapping("/{facilityId}")
	public ResponseEntity<VetFacilityResponse> getVetFacilityById(
		@PathVariable Long facilityId,
		@RequestBody GetVetFacilityRequest request
	) {
		return ResponseEntity
			.ok()
			.body(vetFacilityService.getVetFacilityById(facilityId, request));
	}

	@PostMapping("/{facilityId}/reviews")
	public ResponseEntity<Void> createReview(
		@PathVariable Long facilityId,
		@RequestBody CreateReviewRequest request
	) {
		reviewService.createReview(facilityId, request);
		return ResponseEntity
			.status(HttpStatus.CREATED)
			.build();
	}

	@PutMapping("/{facilityId}/reviews")
	public ResponseEntity<Void> updateReview(
		@PathVariable Long facilityId,
		@RequestBody UpdateReviewRequest request
	) {
		reviewService.updateReview(facilityId, request);
		return ResponseEntity
			.noContent()
			.build();
	}

	@DeleteMapping("/{facilityId}/reviews")
	public ResponseEntity<Void> deleteReview(
		@PathVariable Long facilityId,
		@RequestBody DeleteReviewRequest request
	) {
		reviewService.deleteReview(facilityId, request);
		return ResponseEntity
			.noContent()
			.build();
	}

	@PostMapping("/{facilityId}/bookmarks")
	public ResponseEntity<Void> createBookmark(
		@PathVariable Long facilityId,
		@RequestBody CreateBookmarkRequest request
	) {
		bookmarkService.createBookmark(facilityId, request);
		return ResponseEntity
			.status(HttpStatus.CREATED)
			.build();
	}

	@DeleteMapping("/{facilityId}/bookmarks")
	public ResponseEntity<Void> deleteBookmark(
		@PathVariable Long facilityId,
		@RequestBody DeleteBookmarkRequest request
	) {
		bookmarkService.deleteBookmark(facilityId, request);
		return ResponseEntity
			.noContent()
			.build();
	}
}
