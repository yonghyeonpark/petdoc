package yong.petdoc.service;

import static org.assertj.core.api.Assertions.*;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import yong.petdoc.repository.postlike.PostLikeRepository;
import yong.petdoc.service.post.PostService;

@ActiveProfiles("test")
@SpringBootTest
public class PostServiceTest {

	@Autowired
	private PostService postService;

	@Autowired
	private PostLikeRepository postLikeRepository;

	@AfterEach
	void tearDown() {
		postLikeRepository.deleteAllInBatch();
	}

	@DisplayName("동시에 여러 좋아요 요청이 와도 데드락 발생 없이 PostLike 엔티티가 정상적으로 저장됩니다.")
	@Test
	void prevent_deadlock_under_concurrent_requests() throws InterruptedException {
		// given // when
		int threadCount = 30;
		ExecutorService executorService = Executors.newFixedThreadPool(10);
		CountDownLatch latch = new CountDownLatch(threadCount);

		Long postId = 1L;
		for (long i = 1; i <= threadCount; i++) {
			Long userId = i;
			executorService.submit(() -> {
				try {
					postService.likePost(postId, userId);
				} catch (Exception ignored) {
				} finally {
					latch.countDown();
				}
			});
		}
		latch.await();

		// then
		assertThat(postLikeRepository.findAll().size()).isEqualTo(threadCount);
	}
}
