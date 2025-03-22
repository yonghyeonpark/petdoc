package yong.petdoc.web.user.dto.request;

import yong.petdoc.domain.user.User;

public record CreateUserRequest(
        String email,
        String password,
        String nickname,
        String profileImage
) {

    public User toEntity() {
        return new User(
                email,
                password,
                nickname,
                profileImage()
        );
    }
}