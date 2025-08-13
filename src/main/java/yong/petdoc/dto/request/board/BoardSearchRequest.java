package yong.petdoc.dto.request.board;

public record BoardSearchRequest(
	String searchType,
	String keyword
) {
}
