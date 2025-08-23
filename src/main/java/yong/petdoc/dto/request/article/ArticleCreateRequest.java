package yong.petdoc.dto.request.article;

import yong.petdoc.domain.article.Article;

public record ArticleCreateRequest(String title, String content) {

	public Article toEntity() {
		return new Article(title, content);
	}
}
