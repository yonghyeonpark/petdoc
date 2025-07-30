package yong.petdoc.repository.bookmark;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import yong.petdoc.domain.bookmark.Bookmark;

public interface BookmarkRepository extends JpaRepository<Bookmark, Long> {

	public void deleteByUserIdAndVetFacilityId(Long userId, Long vetFacilityId);

	Page<Bookmark> findByUserId(Long userId, Pageable pageable);

	boolean existsByVetFacilityIdAndUserId(Long vetFacilityId, Long userId);
}
