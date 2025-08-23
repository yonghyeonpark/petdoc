package yong.petdoc.dto.response.post;

import java.time.LocalDateTime;

import yong.petdoc.domain.post.Post;
import yong.petdoc.domain.user.User;

public record PostListResponse(
	Long writerId,
	String writerNickname,
	Long postId,
	String title,
	LocalDateTime createdAt,
	Integer views,
	Integer likes
) {

	public static PostListResponse from(Post post) {
		User writer = post.getUser();
		return new PostListResponse(
			writer.getId(),
			writer.getNickname(),
			post.getId(),
			post.getTitle(),
			post.getCreatedAt(),
			post.getViews(),
			post.getLikes()
		);
	}
}
