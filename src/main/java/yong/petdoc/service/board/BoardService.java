package yong.petdoc.service.board;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import yong.petdoc.domain.board.Board;
import yong.petdoc.domain.user.User;
import yong.petdoc.dto.request.board.CreateBoardRequest;
import yong.petdoc.dto.response.board.BoardDetailResponse;
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
			.orElseThrow(() -> new RuntimeException("존재하지 않는 유저입니다."));
		Board board = request.toEntity(writer);
		boardRepository.save(board);
	}

	@Transactional(readOnly = true)
	public BoardDetailResponse getBoard(Long boardId) {
		Board board = boardRepository.findById(boardId)
			.orElseThrow(() -> new RuntimeException("존재하지 않는 게시판입니다."));
		return BoardDetailResponse.from(board);
	}
}
