package yong.petdoc.domain.boardlike;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import yong.petdoc.domain.BaseTimeEntity;
import yong.petdoc.domain.board.Board;
import yong.petdoc.domain.user.User;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class BoardLike extends BaseTimeEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne
	private Board board;

	@ManyToOne
	private User user;

	public BoardLike(Board board, User user) {
		this.board = board;
		this.user = user;
	}
}
