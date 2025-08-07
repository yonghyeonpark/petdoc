package yong.petdoc.dto.request.board;

import yong.petdoc.domain.board.Board;
import yong.petdoc.domain.user.User;

public record CreateBoardRequest(
	Long writerId,
	String title,
	String content
) {

	public Board toEntity(User writer) {
		return new Board(
			title,
			content,
			writer
		);
	}
}
