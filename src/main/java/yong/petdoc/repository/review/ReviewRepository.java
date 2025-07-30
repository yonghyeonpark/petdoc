package yong.petdoc.repository.review;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import yong.petdoc.domain.review.Review;

public interface ReviewRepository extends JpaRepository<Review, Long> {

	List<Review> findByVetFacilityId(Long vetFacilityId);

	Review findByVetFacilityIdAndUserId(Long vetFacilityId, Long userId);

	void deleteByVetFacilityIdAndUserId(Long vetFacilityId, Long userId);

	Page<Review> findByUserId(Long userId, Pageable pageable);
}
