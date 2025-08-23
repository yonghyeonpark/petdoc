package yong.petdoc.controller.post;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import yong.petdoc.dto.request.post.PostCreateRequest;
import yong.petdoc.dto.request.post.PostSearchRequest;
import yong.petdoc.dto.response.page.PageResponse;
import yong.petdoc.dto.response.post.PostDetailResponse;
import yong.petdoc.dto.response.post.PostListResponse;
import yong.petdoc.service.post.PostService;

@RequiredArgsConstructor
@RequestMapping("/api/posts")
@RestController
public class PostController {

	private final PostService postService;

	@PostMapping
	public ResponseEntity<Void> createPost(@RequestBody PostCreateRequest request) {
		postService.createPost(request);
		return ResponseEntity
			.ok()
			.build();
	}

	@GetMapping("/{postId}")
	public ResponseEntity<PostDetailResponse> getPost(@PathVariable Long postId) {
		return ResponseEntity
			.ok(postService.getPost(postId));
	}

	@GetMapping
	public ResponseEntity<PageResponse<PostListResponse>> getPosts(
		@PageableDefault(page = 0, size = 10, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable
	) {
		return ResponseEntity
			.ok(postService.getPosts(pageable));
	}

	@GetMapping("/search")
	public ResponseEntity<PageResponse<PostListResponse>> getPosts(
		@PageableDefault(page = 0, size = 10, sort = "createdAt") Pageable pageable,
		PostSearchRequest request
	) {
		return ResponseEntity
			.ok(postService.getPosts(pageable, request));
	}

	@PostMapping("/{postId}/{userId}")
	public ResponseEntity<Void> likePost(@PathVariable Long postId, @PathVariable Long userId) {
		postService.likePost(postId, userId);
		return ResponseEntity
			.ok()
			.build();
	}
}
