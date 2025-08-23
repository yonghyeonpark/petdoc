package yong.petdoc.controller.article;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import yong.petdoc.dto.request.article.ArticleCreateRequest;
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
}
