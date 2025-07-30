package yong.petdoc.controller.user;

import java.net.URI;
import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import lombok.RequiredArgsConstructor;
import yong.petdoc.dto.request.bookmark.GetMyBookmarksRequest;
import yong.petdoc.dto.request.review.GetMyReviewsRequest;
import yong.petdoc.dto.request.user.CreateUserRequest;
import yong.petdoc.dto.request.vetfacility.GetRecentVetFacilitiesRequest;
import yong.petdoc.dto.response.bookmark.MyBookmarksResponse;
import yong.petdoc.dto.response.review.MyReviewsResponse;
import yong.petdoc.dto.response.vetfacility.RecentVetFacilityDto;
import yong.petdoc.service.bookmark.BookmarkService;
import yong.petdoc.service.review.ReviewService;
import yong.petdoc.service.user.UserService;
import yong.petdoc.service.vetfacility.VetFacilityService;

@RequiredArgsConstructor
@RequestMapping("/api/users")
@RestController
public class UserController {

	private final UserService userService;
	private final ReviewService reviewService;
	private final BookmarkService bookmarkService;
	private final VetFacilityService vetFacilityService;

	@PostMapping
	public ResponseEntity<Void> createUser(
		@RequestBody CreateUserRequest request,
		UriComponentsBuilder uriComponentsBuilder
	) {
		Long userId = userService.createUser(request);
		URI location = uriComponentsBuilder
			.path("/api/users/{userId}")
			.buildAndExpand(userId)
			.toUri();
		return ResponseEntity
			.created(location)
			.build();
	}

	@GetMapping("/me/reviews")
	public ResponseEntity<MyReviewsResponse> getMyReviews(
		@RequestBody GetMyReviewsRequest request,
		Pageable pageable
	) {
		return ResponseEntity
			.ok(reviewService.getMyReviews(request, pageable));
	}

	@GetMapping("/me/bookmarks")
	public ResponseEntity<MyBookmarksResponse> getMyBookmarks(
		@RequestBody GetMyBookmarksRequest request,
		Pageable pageable
	) {
		return ResponseEntity
			.ok(bookmarkService.getMyBookmarks(request, pageable));
	}

	@GetMapping("/me/recent-facilities")
	public ResponseEntity<List<RecentVetFacilityDto>> getRecentVetFacilities(
		@RequestBody GetRecentVetFacilitiesRequest request
	) {
		return ResponseEntity
			.ok(vetFacilityService.getRecentVetFacilities(request));
	}
}
