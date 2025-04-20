package yong.petdoc.web.bookmark.dto.response;

import java.util.List;

public record MyBookmarksResponse(
        List<BookmarkResponse> reviews,
        int page,
        int size,
        int totalPages,
        long totalElements,
        boolean hasNext
) {
}
