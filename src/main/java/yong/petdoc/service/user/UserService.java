package yong.petdoc.service.user;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import yong.petdoc.domain.user.UserRepository;
import yong.petdoc.web.user.dto.request.CreateUserRequest;

@RequiredArgsConstructor
@Transactional
@Service
public class UserService {

    private final UserRepository userRepository;

    public Long createUser(CreateUserRequest request) {
        return userRepository.save(request.toEntity())
                .getId();
    }
}
