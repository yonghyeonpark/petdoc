package yong.petdoc.dto.response.board;

import yong.petdoc.domain.board.Board;
import yong.petdoc.domain.user.User;

public record BoardDetailResponse(
	Long writerId,
	String writerNickname,
	Long boardId,
	String title,
	String content,
	Integer views
) {

	public static BoardDetailResponse from(Board board) {
		User writer = board.getUser();
		return new BoardDetailResponse(
			writer.getId(),
			writer.getNickname(),
			board.getId(),
			board.getTitle(),
			board.getContent(),
			board.getViews()
		);
	}
}
