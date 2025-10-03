package yong.petdoc.repository.bookmark;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import yong.petdoc.domain.bookmark.Bookmark;

public interface BookmarkRepository extends JpaRepository<Bookmark, Long> {

	Page<Bookmark> findByUserId(Long userId, Pageable pageable);

	boolean existsByVetFacilityIdAndUserId(Long vetFacilityId, Long userId);

	Optional<Bookmark> findByVetFacilityIdAndUserId(Long vetFacilityId, Long userId);
}
