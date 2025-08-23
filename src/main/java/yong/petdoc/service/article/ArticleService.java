package yong.petdoc.service.article;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import yong.petdoc.dto.request.article.ArticleCreateRequest;
import yong.petdoc.dto.response.article.ArticleListResponse;
import yong.petdoc.dto.response.page.PageResponse;
import yong.petdoc.repository.article.ArticleRepository;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class ArticleService {

	private final ArticleRepository articleRepository;

	@Transactional
	public void createArticle(ArticleCreateRequest request) {
		articleRepository.save(request.toEntity());
	}

	public PageResponse<ArticleListResponse> getArticles(Pageable pageable) {
		Page<ArticleListResponse> articles = articleRepository.findAll(pageable)
			.map(ArticleListResponse::from);
		return PageResponse.of(articles);
	}
}
