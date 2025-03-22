package yong.petdoc.web.bookmark;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;
import yong.petdoc.service.bookmark.BookmarkService;
import yong.petdoc.web.bookmark.dto.request.CreateBookmarkRequest;

import java.net.URI;

@RequiredArgsConstructor
@RequestMapping("/api/bookmarks")
@RestController
public class BookmarkController {

    private final BookmarkService bookmarkService;

    @PostMapping
    public ResponseEntity<Void> createBookmark(
            @RequestBody CreateBookmarkRequest request,
            UriComponentsBuilder uriComponentsBuilder
    ) {
        Long bookmarkId = bookmarkService.createBookmark(request);
        URI location = uriComponentsBuilder
                .path("/api/bookmarks/{bookmarkId}")
                .buildAndExpand(bookmarkId)
                .toUri();
        return ResponseEntity
                .created(location)
                .build();
    }
}
