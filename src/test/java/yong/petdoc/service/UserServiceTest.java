package yong.petdoc.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import yong.petdoc.domain.user.UserRepository;
import yong.petdoc.service.user.UserService;
import yong.petdoc.web.user.dto.request.CreateUserRequest;

import static org.assertj.core.api.Assertions.assertThat;

@Transactional
@ActiveProfiles("test")
@SpringBootTest
public class UserServiceTest {

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @DisplayName("사용자 생성 시 DB에 저장되고 ID를 반환한다.")
    @Test
    void createUser() {
        // given
        String email = "test@test.com";
        String password = "password";
        String nickname = "tester";
        CreateUserRequest request = new CreateUserRequest(
                email,
                password,
                nickname,
                null
        );

        // when
        Long userId = userService.createUser(request);

        // then
        assertThat(userRepository.findById(userId).get())
                .extracting("email", "nickname")
                .containsExactly(email, nickname);
    }
}
