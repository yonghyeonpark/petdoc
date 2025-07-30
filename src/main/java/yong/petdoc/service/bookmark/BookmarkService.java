package yong.petdoc.service.bookmark;

import static yong.petdoc.constant.redis.RedisKey.*;
import static yong.petdoc.exception.ErrorCode.*;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import yong.petdoc.domain.bookmark.Bookmark;
import yong.petdoc.domain.user.User;
import yong.petdoc.domain.vetfacility.VetFacility;
import yong.petdoc.dto.request.bookmark.CreateBookmarkRequest;
import yong.petdoc.dto.request.bookmark.DeleteBookmarkRequest;
import yong.petdoc.dto.request.bookmark.GetMyBookmarksRequest;
import yong.petdoc.dto.response.bookmark.BookmarkResponse;
import yong.petdoc.dto.response.bookmark.MyBookmarksResponse;
import yong.petdoc.exception.CustomException;
import yong.petdoc.exception.ErrorCode;
import yong.petdoc.repository.bookmark.BookmarkRepository;
import yong.petdoc.repository.user.UserRepository;
import yong.petdoc.repository.vetfacility.VetFacilityRepository;
import yong.petdoc.service.redis.RedisService;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class BookmarkService {

	private final UserRepository userRepository;
	private final VetFacilityRepository vetFacilityRepository;
	private final BookmarkRepository bookmarkRepository;
	private final RedisService redisService;

	@Transactional
	public void createBookmark(Long vetFacilityId, CreateBookmarkRequest request) {
		Long userId = request.userId();
		String bookmarkSetKey = VET_FACILITY_BOOKMARK_PREFIX + vetFacilityId;
		String bookmarkUserIdValue = String.valueOf(userId);
		String targetIdValue = String.valueOf(vetFacilityId);

		// 해당 vetFacility에 즐겨찾기한 user 목록에 저장
		Long added = redisService.addToSet(bookmarkSetKey, bookmarkUserIdValue);

		// Redis에서 vetFacilityId(key)에 해당하는 userId(value)가 존재하는지 확인
		if (added == null || added == 0) {
			throw new CustomException(DUPLICATE_BOOKMARK);
		}

		// 즐겨찾기가 최소 1이상인 vetFacility 목록 관리
		redisService.addToSet(VET_FACILITY_BOOKMARK_TARGET_IDS, targetIdValue);

		// Redis는 따로 롤백되지 않으므로 직접 처리
		try {
			User user = userRepository.findById(userId)
				.orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));
			VetFacility vetFacility = vetFacilityRepository.findById(vetFacilityId)
				.orElseThrow(() -> new CustomException(ErrorCode.VET_FACILITY_NOT_FOUND));
			Bookmark bookmark = new Bookmark(user, vetFacility);
			bookmarkRepository.save(bookmark);
		} catch (RuntimeException e) {
			redisService.removeFromSet(bookmarkSetKey, bookmarkUserIdValue);
			redisService.removeFromSet(VET_FACILITY_BOOKMARK_TARGET_IDS, targetIdValue);
			throw e;
		}
	}

	@Transactional
	public void deleteBookmark(Long vetFacilityId, DeleteBookmarkRequest request) {
		Long userId = request.userId();
		String key = VET_FACILITY_BOOKMARK_PREFIX + vetFacilityId;
		String value = String.valueOf(userId);

		// Redis에서 삭제 대상이 존재하는지 확인
		Long removed = redisService.removeFromSet(key, value);
		if (removed == null || removed == 0) {
			throw new CustomException(BOOKMARK_NOT_FOUND);
		}

		// Redis는 따로 롤백되지 않으므로 직접 처리
		try {
			bookmarkRepository.deleteByUserIdAndVetFacilityId(userId, vetFacilityId);
		} catch (RuntimeException e) {
			redisService.addToSet(key, value);
			throw e;
		}
	}

	public MyBookmarksResponse getMyBookmarks(GetMyBookmarksRequest request, Pageable pageable) {
		Page<Bookmark> bookmarkPage = bookmarkRepository.findByUserId(request.userId(), pageable);
		List<BookmarkResponse> bookmarks = bookmarkPage.getContent().stream()
			.map(b -> BookmarkResponse.from(b.getId(), b.getVetFacility()))
			.toList();
		return new MyBookmarksResponse(
			bookmarks,
			bookmarkPage.getNumber(),
			bookmarkPage.getSize(),
			bookmarkPage.getTotalPages(),
			bookmarkPage.getTotalElements(),
			bookmarkPage.hasNext()
		);
	}

	public boolean isBookmarked(Long vetFacilityId, Long userId) {
		return bookmarkRepository.existsByVetFacilityIdAndUserId(vetFacilityId, userId);
	}
}
