package yong.petdoc.controller.article;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import yong.petdoc.dto.request.article.ArticleCreateRequest;
import yong.petdoc.dto.response.article.ArticleListResponse;
import yong.petdoc.dto.response.page.PageResponse;
import yong.petdoc.service.article.ArticleService;

@RequiredArgsConstructor
@RequestMapping("/api/articles")
@RestController
public class ArticleController {

	private final ArticleService articleService;

	@PostMapping
	public ResponseEntity<Void> createArticle(@RequestBody ArticleCreateRequest request) {
		articleService.createArticle(request);
		return ResponseEntity
			.ok()
			.build();
	}

	@GetMapping
	public ResponseEntity<PageResponse<ArticleListResponse>> getArticles(
		@PageableDefault(page = 0, size = 10, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable
	) {
		return ResponseEntity
			.ok(articleService.getArticles(pageable));
	}
}
