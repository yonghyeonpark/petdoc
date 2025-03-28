package yong.petdoc.service.bookmark;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import yong.petdoc.domain.bookmark.Bookmark;
import yong.petdoc.domain.bookmark.BookmarkRepository;
import yong.petdoc.domain.user.User;
import yong.petdoc.domain.user.UserRepository;
import yong.petdoc.domain.vetfacility.VetFacility;
import yong.petdoc.domain.vetfacility.VetFacilityRepository;
import yong.petdoc.exception.CustomException;
import yong.petdoc.exception.ErrorCode;
import yong.petdoc.service.redis.RedisService;
import yong.petdoc.web.bookmark.dto.request.CreateBookmarkRequest;
import yong.petdoc.web.bookmark.dto.request.DeleteBookmarkRequest;

import static yong.petdoc.constant.redis.RedisKey.VET_FACILITY_BOOKMARK_PREFIX;
import static yong.petdoc.constant.redis.RedisKey.VET_FACILITY_BOOKMARK_TARGET_IDS;
import static yong.petdoc.exception.ErrorCode.BOOKMARK_NOT_FOUND;
import static yong.petdoc.exception.ErrorCode.DUPLICATE_BOOKMARK;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class BookmarkService {

    private final UserRepository userRepository;
    private final VetFacilityRepository vetFacilityRepository;
    private final BookmarkRepository bookmarkRepository;
    private final RedisService redisService;

    @Transactional
    public Long createBookmark(CreateBookmarkRequest request) {
        Long userId = request.userId();
        Long vetFacilityId = request.vetFacilityId();
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
            return bookmarkRepository.save(bookmark)
                    .getId();
        } catch (RuntimeException e) {
            redisService.removeFromSet(bookmarkSetKey, bookmarkUserIdValue);
            redisService.removeFromSet(VET_FACILITY_BOOKMARK_TARGET_IDS, targetIdValue);
            throw e;
        }
    }

    @Transactional
    public void deleteBookmark(DeleteBookmarkRequest request) {
        Long userId = request.userId();
        Long vetFacilityId = request.vetFacilityId();
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
}
