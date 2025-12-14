package yong.petdoc.service.post;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import yong.petdoc.domain.post.Post;
import yong.petdoc.domain.postlike.PostLike;
import yong.petdoc.domain.user.User;
import yong.petdoc.dto.request.post.PostCreateRequest;
import yong.petdoc.dto.request.post.PostSearchRequest;
import yong.petdoc.dto.response.page.PageResponse;
import yong.petdoc.dto.response.post.PostDetailResponse;
import yong.petdoc.dto.response.post.PostListResponse;
import yong.petdoc.exception.CustomException;
import yong.petdoc.exception.ErrorCode;
import yong.petdoc.repository.post.PostRepository;
import yong.petdoc.repository.postlike.PostLikeRepository;
import yong.petdoc.repository.user.UserRepository;
import yong.petdoc.service.post.dto.PostLikeEvent;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class PostService {

	private final PostRepository postRepository;
	private final UserRepository userRepository;
	private final PostLikeRepository postLikeRepository;

	private final ApplicationEventPublisher applicationEventPublisher;
	private final PostAsyncService postAsyncService;

	@Transactional
	public void createPost(PostCreateRequest request) {
		User writer = userRepository.findById(request.writerId())
			.orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));
		Post post = request.toEntity(writer);
		postRepository.save(post);
	}

	public PostDetailResponse getPost(Long postId) {
		Post post = postRepository.findById(postId)
			.orElseThrow(() -> new CustomException(ErrorCode.POST_NOT_FOUND));

		postAsyncService.increaseViewCount(postId);

		return PostDetailResponse.from(post);
	}

	public PageResponse<PostListResponse> getPosts(Pageable pageable) {
		Page<PostListResponse> posts = postRepository.findAll(pageable)
			.map(PostListResponse::from);
		return PageResponse.of(posts);
	}

	public PageResponse<PostListResponse> getPosts(Pageable pageable, PostSearchRequest request) {
		Page<PostListResponse> posts = postRepository.findPostsBySearchCondition(
			pageable,
			request.searchType(),
			request.keyword()
		).map(PostListResponse::from);
		return PageResponse.of(posts);
	}

	@Transactional
	public void likePost(Long postId, Long userId) {
		Post post = postRepository.findById(postId)
			.orElseThrow(() -> new CustomException(ErrorCode.POST_NOT_FOUND));
		User user = userRepository.findById(userId)
			.orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

		PostLike postLike = new PostLike(post, user);
		postLikeRepository.save(postLike);

		applicationEventPublisher.publishEvent(new PostLikeEvent(postId));
	}
}
