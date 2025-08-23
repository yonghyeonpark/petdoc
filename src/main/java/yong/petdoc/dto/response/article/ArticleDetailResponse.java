package yong.petdoc.dto.response.article;

import java.time.LocalDateTime;

import yong.petdoc.domain.article.Article;

public record ArticleDetailResponse(
	String writerNickname,
	Long articleId,
	String title,
	String content,
	LocalDateTime createdAt
) {

	public static ArticleDetailResponse from(Article article) {
		return new ArticleDetailResponse(
			"관리자",
			article.getId(),
			article.getTitle(),
			article.getContent(),
			article.getCreatedAt()
		);
	}
}
