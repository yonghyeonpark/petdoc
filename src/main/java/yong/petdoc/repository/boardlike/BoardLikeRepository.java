package yong.petdoc.repository.boardlike;

import org.springframework.data.jpa.repository.JpaRepository;

import yong.petdoc.domain.boardlike.BoardLike;

public interface BoardLikeRepository extends JpaRepository<BoardLike, Long> {
}
