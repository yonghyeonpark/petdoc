package yong.petdoc.domain.bookmark;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookmarkRepository extends JpaRepository<Bookmark, Long> {

    public void deleteByUserIdAndVetFacilityId(Long userId, Long vetFacilityId);

    Page<Bookmark> findByUserId(Long userId, Pageable pageable);

    boolean existsByVetFacilityIdAndUserId(Long vetFacilityId, Long userId);
}
