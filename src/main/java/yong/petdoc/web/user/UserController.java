package yong.petdoc.web.user;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;
import yong.petdoc.service.user.UserService;
import yong.petdoc.web.user.dto.request.CreateUserRequest;

import java.net.URI;

@RequiredArgsConstructor
@RequestMapping("/api/users")
@RestController
public class UserController {

    private final UserService userService;

    public ResponseEntity<Void> createUser(
            @RequestBody CreateUserRequest request,
            UriComponentsBuilder uriComponentsBuilder
    ) {
        Long userId = userService.createUser(request);
        URI location = uriComponentsBuilder
                .path("/api/users/{userId}")
                .buildAndExpand(userId)
                .toUri();
        return ResponseEntity
                .created(location)
                .build();
    }
}
