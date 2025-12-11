package yong.petdoc.domain.post;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import yong.petdoc.domain.BaseTimeEntity;
import yong.petdoc.domain.user.User;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class Post extends BaseTimeEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String title;
	private String content;
	private Integer viewCount;
	private Integer likeCount;

	@ManyToOne(fetch = FetchType.LAZY)
	private User user;

	public Post(
		String title,
		String content,
		User user
	) {
		this.title = title;
		this.content = content;
		this.user = user;
		this.viewCount = 0;
		this.likeCount = 0;
	}

	public void increaseViewCount() {
		viewCount++;
	}

	public void increaseLikeCount() {
		likeCount++;
	}
}
