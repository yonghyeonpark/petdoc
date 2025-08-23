package yong.petdoc.dto.request.post;

public record PostSearchRequest(
	String searchType,
	String keyword
) {
}
