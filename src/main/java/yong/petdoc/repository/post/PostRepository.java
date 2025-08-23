package yong.petdoc.repository.post;

import org.springframework.data.jpa.repository.JpaRepository;

import yong.petdoc.domain.post.Post;

public interface PostRepository extends JpaRepository<Post, Long>, PostCustomRepository {
}
