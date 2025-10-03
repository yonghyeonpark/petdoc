package yong.petdoc.service.bookmark;

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

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class BookmarkService {

	private final UserRepository userRepository;
	private final VetFacilityRepository vetFacilityRepository;
	private final BookmarkRepository bookmarkRepository;

	@Transactional
	public void createBookmark(Long vetFacilityId, CreateBookmarkRequest request) {
		Long userId = request.userId();

		User user = userRepository.findById(userId)
			.orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));
		VetFacility vetFacility = vetFacilityRepository.findById(vetFacilityId)
			.orElseThrow(() -> new CustomException(ErrorCode.VET_FACILITY_NOT_FOUND));

		if (bookmarkRepository.existsByVetFacilityIdAndUserId(vetFacilityId, userId)) {
			throw new CustomException(DUPLICATE_BOOKMARK);
		}
		Bookmark bookmark = new Bookmark(user, vetFacility);
		bookmarkRepository.save(bookmark);
	}

	@Transactional
	public void deleteBookmark(Long vetFacilityId, DeleteBookmarkRequest request) {
		Long userId = request.userId();

		Bookmark bookmark = bookmarkRepository.findByVetFacilityIdAndUserId(vetFacilityId, userId)
			.orElseThrow(() -> new CustomException(BOOKMARK_NOT_FOUND));
		bookmarkRepository.delete(bookmark);
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
