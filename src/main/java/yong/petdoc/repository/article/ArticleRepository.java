package yong.petdoc.repository.article;

import org.springframework.data.jpa.repository.JpaRepository;

import yong.petdoc.domain.article.Article;

public interface ArticleRepository extends JpaRepository<Article, Long> {
}
