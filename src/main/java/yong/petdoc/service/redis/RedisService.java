package yong.petdoc.service.redis;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SetOperations;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public class RedisService {

    private static final long RECENT_VET_FACILITY_LIMIT = 10;

    private final RedisTemplate<String, String> stringRedisTemplate;
    private final SetOperations<String, String> setOps;
    private final ZSetOperations<String, String> zSetOps;

    public RedisService(RedisTemplate<String, String> stringRedisTemplate) {
        this.stringRedisTemplate = stringRedisTemplate;
        this.setOps = stringRedisTemplate.opsForSet();
        this.zSetOps = stringRedisTemplate.opsForZSet();
    }

    public void clear(String key) {
        stringRedisTemplate.delete(key);
    }

    public Long addToSet(String key, String value) {
        return setOps.add(key, value);
    }

    public Long removeFromSet(String key, String value) {
        return setOps.remove(key, value);
    }

    public Long getSizeOfSet(String key) {
        return setOps.size(key);
    }

    public Set<String> getMembersOfSet(String key) {
        return setOps.members(key);
    }

    public void addRecentFacility(String key, String facilityJson) {
        zSetOps.add(key, facilityJson, System.currentTimeMillis());

        Long size = zSetOps.size(key);
        if (size != null && size > RECENT_VET_FACILITY_LIMIT) {
            zSetOps.removeRange(key, 0, size - RECENT_VET_FACILITY_LIMIT - 1); // 오래된 시설부터 삭제
        }
    }

    public Set<String> getRecentVetFacilities(String key) {
        return zSetOps.reverseRange(key, 0, -1);// 최근 순으로 전체 조회
    }
}
