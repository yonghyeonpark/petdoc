package yong.petdoc.controller.board;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import yong.petdoc.dto.request.board.BoardSearchRequest;
import yong.petdoc.dto.request.board.CreateBoardRequest;
import yong.petdoc.dto.response.board.BoardDetailResponse;
import yong.petdoc.dto.response.page.PageResponse;
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

	@GetMapping
	public ResponseEntity<PageResponse<BoardDetailResponse>> getBoards(
		@PageableDefault(page = 0, size = 10, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable
	) {
		return ResponseEntity
			.ok(boardService.getBoards(pageable));
	}

	@GetMapping("/search")
	public ResponseEntity<PageResponse<BoardDetailResponse>> getBoards(
		@PageableDefault(page = 0, size = 10, sort = "createdAt") Pageable pageable,
		BoardSearchRequest request
	) {
		return ResponseEntity
			.ok(boardService.getBoards(pageable, request));
	}
}
