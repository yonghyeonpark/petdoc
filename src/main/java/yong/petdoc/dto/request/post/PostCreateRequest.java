package yong.petdoc.dto.request.post;

import yong.petdoc.domain.post.Post;
import yong.petdoc.domain.user.User;

public record PostCreateRequest(
	Long writerId,
	String title,
	String content
) {

	public Post toEntity(User writer) {
		return new Post(
			title,
			content,
			writer
		);
	}
}
