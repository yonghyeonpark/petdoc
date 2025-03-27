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

import static yong.petdoc.constant.redis.RedisKeyPrefix.VET_FACILITY_BOOKMARK;
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
        String key = VET_FACILITY_BOOKMARK + vetFacilityId;
        String value = String.valueOf(userId);

        // Redis에서 vetFacilityId(key)에 해당하는 userId(value)가 존재하는지 확인
        Long added = redisService.addToSet(key, value);
        if (added == null || added == 0) {
            throw new CustomException(DUPLICATE_BOOKMARK);
        }

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
            redisService.removeFromSet(key, value);
            throw e;
        }
    }
}
