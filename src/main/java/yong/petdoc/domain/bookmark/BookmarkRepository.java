package yong.petdoc.domain.bookmark;

import org.springframework.data.jpa.repository.JpaRepository;

public interface BookmarkRepository extends JpaRepository<Bookmark, Long> {

    public void deleteByUserIdAndVetFacilityId(Long userId, Long vetFacilityId);
}
