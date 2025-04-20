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
import yong.petdoc.service.vetfacility.dto.RecentVetFacilityDto;
import yong.petdoc.web.vetfacility.dto.request.GetRecentVetFacilitiesRequest;
import yong.petdoc.web.vetfacility.dto.request.GetVetFacilityRequest;
import yong.petdoc.web.vetfacility.dto.response.VetFacilityResponse;

import java.util.List;

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
        Long userId = 1L;
        Long vetFacilityId = 1L;
        String name = "서울동물병원";
        Long bookmarkCount = 0L;

        // when
        GetVetFacilityRequest request = new GetVetFacilityRequest(userId);
        VetFacilityResponse response = vetFacilityService.getVetFacilityById(vetFacilityId, request);

        // then
        assertThat(response)
                .extracting("id", "name", "bookmarkCount")
                .containsExactly(vetFacilityId, name, bookmarkCount);
    }

    @DisplayName("최근 조회한 수의 시설 목록을 조회하면 해당 정보를 반환한다.")
    @Test
    void getRecentVetFacilities() {
        // given
        Long userId = 1L;
        for (long i = 1; i <= 10; i++) {
            vetFacilityService.getVetFacilityById(i, new GetVetFacilityRequest(userId));
        }

        // when
        GetRecentVetFacilitiesRequest request = new GetRecentVetFacilitiesRequest(userId);
        List<RecentVetFacilityDto> recentVetFacilities = vetFacilityService.getRecentVetFacilities(request);

        // then
        assertThat(recentVetFacilities.size()).isEqualTo(10);
    }

    @DisplayName("최근 조회 목록이 10개를 초과하면, 가장 오래된 수의 시설들은 제외되고 10개만 유지된다.")
    @Test
    void getRecentVetFacilities_discardsOldest_whenExceedLimit() {
        // given
        Long userId = 1L;
        Long vetFacilityId = 1L;
        for (long i = 0; i < 5; i++) {
            vetFacilityService.getVetFacilityById(vetFacilityId, new GetVetFacilityRequest(userId));
        }

        // when
        GetRecentVetFacilitiesRequest request = new GetRecentVetFacilitiesRequest(userId);
        List<RecentVetFacilityDto> recentVetFacilities = vetFacilityService.getRecentVetFacilities(request);

        // then
        assertThat(recentVetFacilities.size()).isEqualTo(1);
    }

    @DisplayName("동일한 수의 시설을 여러 번 조회해도 최근 조회 목록에 중복으로 저장되지 않는다.")
    @Test
    void getRecentVetFacilities_avoidsDuplicate_whenRepeatedAccess() {
        // given
        Long userId = 1L;
        for (long i = 1; i <= 12; i++) {
            vetFacilityService.getVetFacilityById(i, new GetVetFacilityRequest(userId));
        }

        // when
        GetRecentVetFacilitiesRequest request = new GetRecentVetFacilitiesRequest(userId);
        List<RecentVetFacilityDto> recentVetFacilities = vetFacilityService.getRecentVetFacilities(request);

        // then
        assertThat(recentVetFacilities.size()).isEqualTo(10);
    }
}
