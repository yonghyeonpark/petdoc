package yong.petdoc.service.board;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import yong.petdoc.domain.board.Board;
import yong.petdoc.exception.CustomException;
import yong.petdoc.exception.ErrorCode;
import yong.petdoc.repository.board.BoardRepository;

@RequiredArgsConstructor
@Service
public class BoardAsyncService {

	private final BoardRepository boardRepository;

	@Async
	@Transactional
	public void increaseViews(Long boardId) {
		Board board = boardRepository.findById(boardId)
			.orElseThrow(() -> new CustomException(ErrorCode.BOARD_NOT_FOUND));

		board.incrementViews();
	}
}
