package yong.petdoc.web.bookmark.dto.request;

public record DeleteBookmarkRequest(
        Long userId,
        Long vetFacilityId
) {
}
