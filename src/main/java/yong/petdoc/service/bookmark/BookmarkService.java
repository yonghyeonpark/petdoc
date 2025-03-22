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
import yong.petdoc.web.bookmark.dto.request.CreateBookmarkRequest;

@RequiredArgsConstructor
@Transactional
@Service
public class BookmarkService {

    private final UserRepository userRepository;
    private final VetFacilityRepository vetFacilityRepository;
    private final BookmarkRepository bookmarkRepository;

    public Long createBookmark(CreateBookmarkRequest request) {
        User user = userRepository.findById(request.userid())
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));
        VetFacility vetFacility = vetFacilityRepository.findById(request.vetFacilityId())
                .orElseThrow(() -> new CustomException(ErrorCode.VET_FACILITY_NOT_FOUND));
        return bookmarkRepository.save(
                new Bookmark(user, vetFacility)
        ).getId();
    }
}
