package yong.petdoc.service.article;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import yong.petdoc.dto.request.article.ArticleCreateRequest;
import yong.petdoc.repository.article.ArticleRepository;

@RequiredArgsConstructor
@Service
public class ArticleService {

	private final ArticleRepository articleRepository;

	@Transactional
	public void createArticle(ArticleCreateRequest request) {
		articleRepository.save(request.toEntity());
	}
}
