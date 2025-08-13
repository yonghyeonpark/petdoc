package yong.petdoc.repository.board;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import yong.petdoc.domain.board.Board;

public interface BoardCustomRepository {

	Page<Board> findBoardsBySearchCondition(Pageable pageable, String searchType, String keyword);
}
