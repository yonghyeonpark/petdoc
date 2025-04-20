package yong.petdoc.web.bookmark.dto.response;

import java.util.List;

public record MyBookmarksResponse(
        List<BookmarkResponse> bookmarks,
        int page,
        int size,
        int totalPages,
        long totalElements,
        boolean hasNext
) {
}
