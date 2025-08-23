package yong.petdoc.dto.response.article;

import java.time.LocalDateTime;

import yong.petdoc.domain.article.Article;

public record ArticleListResponse(
	String writerNickname,
	Long articleId,
	String title,
	String content,
	LocalDateTime createdAt
) {

	public static ArticleListResponse from(Article article) {
		return new ArticleListResponse(
			"관리자",
			article.getId(),
			article.getTitle(),
			article.getContent(),
			article.getCreatedAt()
		);
	}
}
