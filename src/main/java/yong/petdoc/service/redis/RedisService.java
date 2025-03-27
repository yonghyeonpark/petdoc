package yong.petdoc.service.redis;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SetOperations;
import org.springframework.stereotype.Service;

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

    public void removeFromSet(String key, String value) {
        setOps.remove(key, value);
    }

    public Long getSizeOfSet(String key) {
        return setOps.size(key);
    }
}
