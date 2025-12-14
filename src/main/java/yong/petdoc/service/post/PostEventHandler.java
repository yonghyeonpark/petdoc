package yong.petdoc.service.post;

import org.springframework.dao.TransientDataAccessException;
import org.springframework.retry.annotation.Retryable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.event.TransactionalEventListener;

import lombok.RequiredArgsConstructor;
import yong.petdoc.domain.post.Post;
import yong.petdoc.exception.CustomException;
import yong.petdoc.exception.ErrorCode;
import yong.petdoc.repository.post.PostRepository;
import yong.petdoc.service.post.dto.PostLikeEvent;

@RequiredArgsConstructor
@Component
public class PostEventHandler {

	private final PostRepository postRepository;

	@TransactionalEventListener
	@Async
	@Retryable(retryFor = {TransientDataAccessException.class})
	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public void increaseLikeCount(PostLikeEvent postLikeEvent) {
		Post post = postRepository.findById(postLikeEvent.postId())
			.orElseThrow(() -> new CustomException(ErrorCode.POST_NOT_FOUND));

		post.increaseLikeCount();
	}
}
