package yong.petdoc.repository.post;

import static yong.petdoc.domain.post.QPost.*;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;
import yong.petdoc.domain.post.Post;

@RequiredArgsConstructor
public class PostCustomRepositoryImpl implements PostCustomRepository {

	private final JPAQueryFactory queryFactory;

	@Override
	public Page<Post> findPostsBySearchCondition(Pageable pageable, String searchType, String keyword) {
		List<Post> posts = queryFactory
			.selectFrom(post)
			.leftJoin(post.user).fetchJoin()
			.where(buildSearchCondition(searchType, keyword))
			.offset(pageable.getOffset())
			.limit(pageable.getPageSize())
			.orderBy(post.createdAt.desc())
			.fetch();

		JPAQuery<Long> countQuery = queryFactory
			.select(post.count())
			.from(post)
			.where(buildSearchCondition(searchType, keyword));

		return PageableExecutionUtils.getPage(posts, pageable, countQuery::fetchOne);
	}

	private BooleanExpression buildSearchCondition(String searchType, String keyword) {
		if (searchType == null || searchType.isBlank()) {
			return null;
		}

		return switch (searchType) {
			case "title" -> post.title.contains(keyword);
			case "content" -> post.content.contains(keyword);
			case "writer" -> post.user.nickname.contains(keyword);
			case "titleContent" -> post.title.contains(keyword)
				.or(post.content.contains(keyword));
			default -> null;
		};
	}
}
