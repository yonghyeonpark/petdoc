package yong.petdoc.repository.postlike;

import org.springframework.data.jpa.repository.JpaRepository;

import yong.petdoc.domain.postlike.PostLike;

public interface PostLikeRepository extends JpaRepository<PostLike, Long> {
}
