package yong.petdoc.service;

import org.awaitility.Awaitility;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SetOperations;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import yong.petdoc.constant.redis.RedisKeyPrefix;
import yong.petdoc.domain.bookmark.Bookmark;
import yong.petdoc.domain.bookmark.BookmarkRepository;
import yong.petdoc.exception.CustomException;
import yong.petdoc.service.bookmark.BookmarkService;
import yong.petdoc.web.bookmark.dto.request.CreateBookmarkRequest;
import yong.petdoc.web.bookmark.dto.request.DeleteBookmarkRequest;

import java.time.Duration;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static yong.petdoc.exception.ErrorCode.BOOKMARK_NOT_FOUND;
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

    @AfterEach
    void tearDown() {
        bookmarkRepository.deleteAllInBatch();
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

    @DisplayName("이미 즐겨찾기한 수의 시설에 즐겨찾기 요청을 하면 예외가 발생한다.")
    @Test
    void createBookmark_throwsException_whenDuplicateBookmark() {
        // given
        Long userId = 1L;
        Long vetFacilityId = 1L;
        String key = RedisKeyPrefix.VET_FACILITY_BOOKMARK + vetFacilityId;

        stringRedisTemplate.opsForSet().add(key, String.valueOf(userId));

        CreateBookmarkRequest request = new CreateBookmarkRequest(userId, vetFacilityId);

        // when // then
        assertThatThrownBy(() -> bookmarkService.createBookmark(request))
                .isInstanceOf(CustomException.class)
                .hasMessage(DUPLICATE_BOOKMARK.getMessage());
    }

    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    @DisplayName("여러 유저가 동시에 즐겨찾기를 요청해도 중복 없이 모두 정상 저장된다.")
    @Test
    void createBookmark_concurrentWithMultipleUsers() throws InterruptedException {
        //given // when
        Long vetFacilityId = 1L;
        int threadCount = 30;
        String key = RedisKeyPrefix.VET_FACILITY_BOOKMARK + vetFacilityId;
        ExecutorService executorService = Executors.newFixedThreadPool(10);
        CountDownLatch latch = new CountDownLatch(threadCount);

        for (int i = 1; i <= threadCount + 30; i++) {
            Long userId = (long) i;
            executorService.submit(() -> {
                try {
                    CreateBookmarkRequest request = new CreateBookmarkRequest(userId, vetFacilityId);
                    bookmarkService.createBookmark(request);
                } catch (Exception ignored) {
                } finally {
                    latch.countDown();
                }
            });
        }
        latch.await();

        // then
        Awaitility.await()
                .atMost(Duration.ofSeconds(1))
                .untilAsserted(() -> {
                    assertThat(stringRedisTemplate.opsForSet().size(key)).isEqualTo(30);
                    assertThat(bookmarkRepository.findAll().size()).isEqualTo(30);
                });
    }

    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    @DisplayName("동일한 유저가 동시에 즐겨찾기를 여러 번 요청해도 중복 저장되지 않는다.")
    @Test
    void createBookmark_concurrentWithSameUser() throws InterruptedException {
        //given // when
        Long userId = 1L;
        Long vetFacilityId = 1L;
        int threadCount = 30;
        String key = RedisKeyPrefix.VET_FACILITY_BOOKMARK + vetFacilityId;
        ExecutorService executorService = Executors.newFixedThreadPool(10);
        CountDownLatch latch = new CountDownLatch(threadCount);

        for (int i = 1; i <= threadCount; i++) {
            executorService.submit(() -> {
                try {
                    CreateBookmarkRequest request = new CreateBookmarkRequest(userId, vetFacilityId);
                    bookmarkService.createBookmark(request);
                } catch (Exception ignored) {
                } finally {
                    latch.countDown();
                }
            });
        }
        latch.await();

        // then
        Awaitility.await()
                .atMost(Duration.ofSeconds(1))
                .untilAsserted(() -> {
                    assertThat(stringRedisTemplate.opsForSet().size(key)).isEqualTo(1);
                    assertThat(bookmarkRepository.findAll().size()).isEqualTo(1);
                });
    }

    @DisplayName("즐겨찾기 삭제 시 Redis에서는 사용자 ID가, RDB에서는 엔티티가 제거된다.")
    @Test
    void deleteBookmark() {
        // given
        Long userId = 1L;
        Long vetFacilityId = 1L;
        String key = RedisKeyPrefix.VET_FACILITY_BOOKMARK + vetFacilityId;
        CreateBookmarkRequest createRequest = new CreateBookmarkRequest(userId, vetFacilityId);
        bookmarkService.createBookmark(createRequest);

        // when
        DeleteBookmarkRequest deleteRequest = new DeleteBookmarkRequest(userId, vetFacilityId);
        bookmarkService.deleteBookmark(deleteRequest);

        // then
        assertThat(stringRedisTemplate.opsForSet().size(key)).isEqualTo(0);
        assertThat(bookmarkRepository.findAll().size()).isEqualTo(0);
    }

    @DisplayName("즐겨찾기 하지 않은 수의 시설에 즐겨찾기 삭제 요청을 하면 예외가 발생한다.")
    @Test
    void deleteBookmark_throwsException_whenNotFoundBookmark() {
        // given
        Long userId = 1L;
        Long vetFacilityId = 1L;

        // when // then
        DeleteBookmarkRequest request = new DeleteBookmarkRequest(userId, vetFacilityId);
        assertThatThrownBy(() -> bookmarkService.deleteBookmark(request))
                .isInstanceOf(CustomException.class)
                .hasMessage(BOOKMARK_NOT_FOUND.getMessage());
    }
}