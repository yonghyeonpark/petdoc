package yong.petdoc.dto.response.board;

import java.time.LocalDateTime;

import yong.petdoc.domain.board.Board;
import yong.petdoc.domain.user.User;

public record BoardDetailResponse(
	Long writerId,
	String writerNickname,
	Long boardId,
	String title,
	String content,
	LocalDateTime createdAt,
	Integer views,
	Integer likes
) {

	public static BoardDetailResponse from(Board board) {
		User writer = board.getUser();
		return new BoardDetailResponse(
			writer.getId(),
			writer.getNickname(),
			board.getId(),
			board.getTitle(),
			board.getContent(),
			board.getCreatedAt(),
			board.getViews(),
			board.getLikes()
		);
	}
}
