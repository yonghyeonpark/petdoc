package yong.petdoc.web.user;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;
import yong.petdoc.service.bookmark.BookmarkService;
import yong.petdoc.service.review.ReviewService;
import yong.petdoc.service.user.UserService;
import yong.petdoc.web.bookmark.dto.request.GetMyBookmarksRequest;
import yong.petdoc.web.bookmark.dto.response.MyBookmarksResponse;
import yong.petdoc.web.review.dto.request.GetMyReviewsRequest;
import yong.petdoc.web.review.dto.response.MyReviewsResponse;
import yong.petdoc.web.user.dto.request.CreateUserRequest;

import java.net.URI;

@RequiredArgsConstructor
@RequestMapping("/api/users")
@RestController
public class UserController {

    private final UserService userService;
    private final ReviewService reviewService;
    private final BookmarkService bookmarkService;

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
    public ResponseEntity<MyReviewsResponse> getReviewsByUserId(
            @RequestBody GetMyReviewsRequest request,
            Pageable pageable
    ) {
        return ResponseEntity
                .ok(reviewService.getMyReviews(request, pageable));
    }

    @GetMapping("/me/bookmarks")
    public ResponseEntity<MyBookmarksResponse> getBookmarksByUserId(
            @RequestBody GetMyBookmarksRequest request,
            Pageable pageable
    ) {
        return ResponseEntity
                .ok(bookmarkService.getMyBookmarks(request, pageable));
    }
}
