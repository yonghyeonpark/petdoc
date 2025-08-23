package yong.petdoc.service.post;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import yong.petdoc.domain.post.Post;
import yong.petdoc.exception.CustomException;
import yong.petdoc.exception.ErrorCode;
import yong.petdoc.repository.post.PostRepository;

@RequiredArgsConstructor
@Service
public class PostAsyncService {

	private final PostRepository postRepository;

	@Async
	@Transactional
	public void increaseViews(Long postId) {
		Post post = postRepository.findById(postId)
			.orElseThrow(() -> new CustomException(ErrorCode.POST_NOT_FOUND));

		post.incrementViews();
	}

	@Async
	@Transactional
	public void increaseLikes(Long postId) {
		Post post = postRepository.findById(postId)
			.orElseThrow(() -> new CustomException(ErrorCode.POST_NOT_FOUND));

		post.incrementLikes();
	}
}
