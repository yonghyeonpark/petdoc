package yong.petdoc.web.bookmark.dto.request;

public record CreateBookmarkRequest(
        Long userId,
        Long vetFacilityId
) {
}
