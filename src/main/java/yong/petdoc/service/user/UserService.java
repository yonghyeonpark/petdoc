package yong.petdoc.service.user;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import yong.petdoc.dto.request.user.CreateUserRequest;
import yong.petdoc.repository.user.UserRepository;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class UserService {

	private final UserRepository userRepository;

	@Transactional
	public Long createUser(CreateUserRequest request) {
		return userRepository.save(request.toEntity())
			.getId();
	}
}
