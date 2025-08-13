package yong.petdoc.repository.board;

import org.springframework.data.jpa.repository.JpaRepository;

import yong.petdoc.domain.board.Board;

public interface BoardRepository extends JpaRepository<Board, Long>, BoardCustomRepository {
}
