package yong.petdoc.controller.board;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import yong.petdoc.dto.request.board.CreateBoardRequest;
import yong.petdoc.dto.response.board.BoardDetailResponse;
import yong.petdoc.service.board.BoardService;

@RequiredArgsConstructor
@RequestMapping("/api/boards")
@RestController
public class BoardController {

	private final BoardService boardService;

	@PostMapping
	public ResponseEntity<Void> createBoard(@RequestBody CreateBoardRequest request) {
		boardService.createBoard(request);
		return ResponseEntity
			.ok()
			.build();
	}

	@GetMapping("/{boardId}")
	public ResponseEntity<BoardDetailResponse> getBoard(@PathVariable Long boardId) {
		return ResponseEntity
			.ok(boardService.getBoard(boardId));
	}
}
