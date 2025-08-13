package yong.petdoc.repository.board;

import static yong.petdoc.domain.board.QBoard.*;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;
import yong.petdoc.domain.board.Board;

@RequiredArgsConstructor
public class BoardCustomRepositoryImpl implements BoardCustomRepository {

	private final JPAQueryFactory queryFactory;

	@Override
	public Page<Board> findBoardsBySearchCondition(Pageable pageable, String searchType, String keyword) {
		List<Board> boards = queryFactory
			.selectFrom(board)
			.leftJoin(board.user).fetchJoin()
			.where(buildSearchCondition(searchType, keyword))
			.offset(pageable.getOffset())
			.limit(pageable.getPageSize())
			.orderBy(board.createdAt.desc())
			.fetch();

		JPAQuery<Long> countQuery = queryFactory
			.select(board.count())
			.from(board)
			.where(buildSearchCondition(searchType, keyword));

		return PageableExecutionUtils.getPage(boards, pageable, countQuery::fetchOne);
	}

	private BooleanExpression buildSearchCondition(String searchType, String keyword) {
		if (searchType == null || searchType.isBlank()) {
			return null;
		}

		return switch (searchType) {
			case "title" -> board.title.contains(keyword);
			case "content" -> board.content.contains(keyword);
			case "writer" -> board.user.nickname.contains(keyword);
			case "titleContent" -> board.title.contains(keyword)
				.or(board.content.contains(keyword));
			default -> null;
		};
	}
}
