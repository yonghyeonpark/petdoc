package yong.petdoc.web.vetfacility;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;
import yong.petdoc.service.bookmark.BookmarkService;
import yong.petdoc.service.review.ReviewService;
import yong.petdoc.service.vetfacility.VetFacilityService;
import yong.petdoc.web.bookmark.dto.request.CreateBookmarkRequest;
import yong.petdoc.web.bookmark.dto.request.DeleteBookmarkRequest;
import yong.petdoc.web.review.dto.request.CreateReviewRequest;
import yong.petdoc.web.review.dto.request.DeleteReviewRequest;
import yong.petdoc.web.review.dto.request.UpdateReviewRequest;
import yong.petdoc.web.vetfacility.dto.response.VetFacilityResponse;

import java.net.URI;

@RequiredArgsConstructor
@RequestMapping("/api/facilities")
@RestController
public class VetFacilityController {

    private final VetFacilityService vetFacilityService;
    private final ReviewService reviewService;
    private final BookmarkService bookmarkService;

    @GetMapping("/{facilityId}")
    public ResponseEntity<VetFacilityResponse> getVetFacilityById(@PathVariable Long facilityId) {
        return ResponseEntity
                .ok()
                .body(vetFacilityService.getVetFacilityById(facilityId));
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
            @RequestBody CreateBookmarkRequest request,
            UriComponentsBuilder uriComponentsBuilder
    ) {
        Long bookmarkId = bookmarkService.createBookmark(facilityId, request);
        URI location = uriComponentsBuilder
                .path("/api/bookmarks/{bookmarkId}")
                .buildAndExpand(bookmarkId)
                .toUri();
        return ResponseEntity
                .created(location)
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
