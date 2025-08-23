package yong.petdoc.dto.response.post;

import java.time.LocalDateTime;

import yong.petdoc.domain.post.Post;
import yong.petdoc.domain.user.User;

public record PostDetailResponse(
	Long writerId,
	String writerNickname,
	Long postId,
	String title,
	String content,
	LocalDateTime createdAt,
	Integer views,
	Integer likes
) {

	public static PostDetailResponse from(Post post) {
		User writer = post.getUser();
		return new PostDetailResponse(
			writer.getId(),
			writer.getNickname(),
			post.getId(),
			post.getTitle(),
			post.getContent(),
			post.getCreatedAt(),
			post.getViews(),
			post.getLikes()
		);
	}
}
