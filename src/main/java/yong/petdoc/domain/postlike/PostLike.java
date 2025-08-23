package yong.petdoc.domain.postlike;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import yong.petdoc.domain.BaseTimeEntity;
import yong.petdoc.domain.post.Post;
import yong.petdoc.domain.user.User;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class PostLike extends BaseTimeEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne
	private Post post;

	@ManyToOne
	private User user;

	public PostLike(Post post, User user) {
		this.post = post;
		this.user = user;
	}
}
