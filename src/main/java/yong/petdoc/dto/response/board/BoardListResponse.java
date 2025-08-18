package yong.petdoc.dto.response.board;

import java.time.LocalDateTime;

import yong.petdoc.domain.board.Board;
import yong.petdoc.domain.user.User;

public record BoardListResponse(
	Long writerId,
	String writerNickname,
	Long boardId,
	String title,
	LocalDateTime createdAt,
	Integer views,
	Integer likes
) {

	public static BoardListResponse from(Board board) {
		User writer = board.getUser();
		return new BoardListResponse(
			writer.getId(),
			writer.getNickname(),
			board.getId(),
			board.getTitle(),
			board.getCreatedAt(),
			board.getViews(),
			board.getLikes()
		);
	}
}
