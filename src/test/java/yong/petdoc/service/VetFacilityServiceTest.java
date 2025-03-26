package yong.petdoc.service;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import yong.petdoc.service.vetfacility.VetFacilityService;

import static org.assertj.core.api.Assertions.assertThat;

@Transactional
@ActiveProfiles("test")
@SpringBootTest
public class VetFacilityServiceTest {

    @Autowired
    private VetFacilityService vetFacilityService;

    @Autowired
    private RedisTemplate<String, String> stringRedisTemplate;

    @AfterEach
    void clearRedis() {
        try (RedisConnection connection = stringRedisTemplate.getConnectionFactory().getConnection()) {
            connection.serverCommands().flushDb();
        }
    }

    @DisplayName("수의 시설을 ID로 조회하면 해당 정보를 반환한다.")
    @Test
    void getVetFacilityById() {
        // given
        Long vetFacilityId = 1L;
        String name = "서울동물병원";
        Long bookmarkCount = 0L;

        // when // then
        assertThat(vetFacilityService.getVetFacilityById(vetFacilityId))
                .extracting("id", "name", "bookmarkCount")
                .containsExactly(vetFacilityId, name, bookmarkCount);
    }
}
