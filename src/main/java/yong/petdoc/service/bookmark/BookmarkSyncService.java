package yong.petdoc.service.bookmark;

import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import yong.petdoc.domain.vetfacility.VetFacility;
import yong.petdoc.domain.vetfacility.VetFacilityRepository;
import yong.petdoc.service.redis.RedisService;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static yong.petdoc.constant.redis.RedisKey.VET_FACILITY_BOOKMARK_PREFIX;
import static yong.petdoc.constant.redis.RedisKey.VET_FACILITY_BOOKMARK_TARGET_IDS;

@RequiredArgsConstructor
@Service
public class BookmarkSyncService {

    private final RedisService redisService;
    private final VetFacilityRepository vetFacilityRepository;

    @Scheduled(cron = "* */10 * * * *")
    @Transactional
    public void syncBookmarkCount() {
        Set<String> idStrings = redisService.getMembersOfSet(VET_FACILITY_BOOKMARK_TARGET_IDS);
        if (idStrings.isEmpty()) return;

        Set<Long> ids = idStrings.stream()
                .map(Long::parseLong)
                .collect(Collectors.toSet());

        List<VetFacility> vetFacilities = vetFacilityRepository.findAllById(ids);
        for (VetFacility vetFacility : vetFacilities) {
            Long vetFacilityId = vetFacility.getId();
            String key = VET_FACILITY_BOOKMARK_PREFIX + vetFacilityId;
            Long bookmarkCount = redisService.getSizeOfSet(key);
            if (bookmarkCount == 0) {
                redisService.removeFromSet(VET_FACILITY_BOOKMARK_TARGET_IDS, String.valueOf(vetFacilityId));
            }
            vetFacility.asyncBookmarkCount(bookmarkCount);
        }
    }
}
