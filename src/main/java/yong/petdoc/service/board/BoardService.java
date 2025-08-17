package yong.petdoc.service.board;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import yong.petdoc.domain.board.Board;
import yong.petdoc.domain.user.User;
import yong.petdoc.dto.request.board.BoardSearchRequest;
import yong.petdoc.dto.request.board.CreateBoardRequest;
import yong.petdoc.dto.response.board.BoardDetailResponse;
import yong.petdoc.dto.response.board.BoardListResponse;
import yong.petdoc.dto.response.page.PageResponse;
import yong.petdoc.exception.CustomException;
import yong.petdoc.exception.ErrorCode;
import yong.petdoc.repository.board.BoardRepository;
import yong.petdoc.repository.user.UserRepository;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class BoardService {

	private final BoardRepository boardRepository;
	private final UserRepository userRepository;

	private final BoardAsyncService boardAsyncService;

	@Transactional
	public void createBoard(CreateBoardRequest request) {
		User writer = userRepository.findById(request.writerId())
			.orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));
		Board board = request.toEntity(writer);
		boardRepository.save(board);
	}

	public BoardDetailResponse getBoard(Long boardId) {
		Board board = boardRepository.findById(boardId)
			.orElseThrow(() -> new CustomException(ErrorCode.BOARD_NOT_FOUND));

		boardAsyncService.increaseViews(boardId);

		return BoardDetailResponse.from(board);
	}

	public PageResponse<BoardListResponse> getBoards(Pageable pageable) {
		Page<BoardListResponse> boards = boardRepository.findAll(pageable)
			.map(BoardListResponse::from);
		return PageResponse.of(boards);
	}

	public PageResponse<BoardListResponse> getBoards(Pageable pageable, BoardSearchRequest request) {
		Page<BoardListResponse> boards = boardRepository.findBoardsBySearchCondition(
			pageable,
			request.searchType(),
			request.keyword()
		).map(BoardListResponse::from);
		return PageResponse.of(boards);
	}
}
