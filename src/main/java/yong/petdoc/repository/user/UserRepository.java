package yong.petdoc.repository.user;

import org.springframework.data.jpa.repository.JpaRepository;

import yong.petdoc.domain.user.User;

public interface UserRepository extends JpaRepository<User, Long> {
}
