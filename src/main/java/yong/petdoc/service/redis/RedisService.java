package yong.petdoc.service.redis;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SetOperations;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public class RedisService {

    private final RedisTemplate<String, String> stringRedisTemplate;
    private final SetOperations<String, String> setOps;

    public RedisService(RedisTemplate<String, String> stringRedisTemplate) {
        this.stringRedisTemplate = stringRedisTemplate;
        this.setOps = stringRedisTemplate.opsForSet();
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
}
