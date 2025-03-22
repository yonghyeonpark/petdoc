package yong.petdoc.web.bookmark.dto.request;

public record CreateBookmarkRequest(
        Long userid,
        Long vetFacilityId
) {
}
