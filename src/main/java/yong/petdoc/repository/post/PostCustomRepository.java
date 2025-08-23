package yong.petdoc.repository.post;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import yong.petdoc.domain.post.Post;

public interface PostCustomRepository {

	Page<Post> findPostsBySearchCondition(Pageable pageable, String searchType, String keyword);
}
