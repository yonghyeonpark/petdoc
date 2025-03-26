package yong.petdoc.service;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SetOperations;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import yong.petdoc.constant.redis.RedisKeyPrefix;
import yong.petdoc.domain.bookmark.Bookmark;
import yong.petdoc.domain.bookmark.BookmarkRepository;
import yong.petdoc.exception.CustomException;
import yong.petdoc.service.bookmark.BookmarkService;
import yong.petdoc.web.bookmark.dto.request.CreateBookmarkRequest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static yong.petdoc.exception.ErrorCode.DUPLICATE_BOOKMARK;

@Transactional
@ActiveProfiles("test")
@SpringBootTest
public class BookmarkServiceTest {

    @Autowired
    private BookmarkService bookmarkService;

    @Autowired
    private BookmarkRepository bookmarkRepository;

    @Autowired
    private RedisTemplate<String, String> stringRedisTemplate;

    @AfterEach
    void clearRedis() {
        try (RedisConnection connection = stringRedisTemplate.getConnectionFactory().getConnection()) {
            connection.serverCommands().flushDb();
        }
    }

    @DisplayName("즐겨찾기 생성 시 Redis에는 수의 시설에 대한 사용자 ID가, RDB에는 엔티티가 저장된다.")
    @Test
    void createBookmark() {
        // given
        Long userId = 1L;
        Long vetFacilityId = 1L;
        CreateBookmarkRequest request = new CreateBookmarkRequest(userId, vetFacilityId);
        String key = RedisKeyPrefix.VET_FACILITY_BOOKMARK + vetFacilityId;
        SetOperations<String, String> ops = stringRedisTemplate.opsForSet();

        // when
        Long bookmarkId = bookmarkService.createBookmark(request);

        // then
        assertThat(ops.isMember(key, String.valueOf(userId))).isTrue();
        assertThat(ops.size(key)).isEqualTo(1);

        Bookmark bookmark = bookmarkRepository.findById(bookmarkId).get();
        assertThat(bookmark.getUser().getId()).isEqualTo(userId);
        assertThat(bookmark.getVetFacility().getId()).isEqualTo(vetFacilityId);
    }

    @DisplayName("이미 즐겨찾기한 경우 예외를 던진다.")
    @Test
    void createBookmark_throwsException_whenDuplicateBookmark() {
        // given
        Long userId = 1L;
        Long vetFacilityId = 1L;
        String key = RedisKeyPrefix.VET_FACILITY_BOOKMARK + vetFacilityId;
        SetOperations<String, String> ops = stringRedisTemplate.opsForSet();

        ops.add(key, String.valueOf(userId));

        CreateBookmarkRequest request = new CreateBookmarkRequest(userId, vetFacilityId);

        // when // then
        assertThatThrownBy(() -> bookmarkService.createBookmark(request))
                .isInstanceOf(CustomException.class)
                .hasMessage(DUPLICATE_BOOKMARK.getMessage());
    }
}
