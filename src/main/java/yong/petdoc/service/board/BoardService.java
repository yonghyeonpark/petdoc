package yong.petdoc.service.board;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import yong.petdoc.domain.board.Board;
import yong.petdoc.domain.user.User;
import yong.petdoc.dto.request.board.CreateBoardRequest;
import yong.petdoc.dto.response.board.BoardDetailResponse;
import yong.petdoc.exception.CustomException;
import yong.petdoc.exception.ErrorCode;
import yong.petdoc.repository.board.BoardRepository;
import yong.petdoc.repository.user.UserRepository;

@RequiredArgsConstructor
@Service
public class BoardService {

	private final BoardRepository boardRepository;
	private final UserRepository userRepository;

	@Transactional
	public void createBoard(CreateBoardRequest request) {
		User writer = userRepository.findById(request.writerId())
			.orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));
		Board board = request.toEntity(writer);
		boardRepository.save(board);
	}

	@Transactional
	public BoardDetailResponse getBoard(Long boardId) {
		Board board = boardRepository.findById(boardId)
			.orElseThrow(() -> new CustomException(ErrorCode.BOARD_NOT_FOUND));

		board.incrementViews();

		return BoardDetailResponse.from(board);
	}
}
