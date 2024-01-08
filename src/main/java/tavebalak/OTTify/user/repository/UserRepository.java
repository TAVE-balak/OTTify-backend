package tavebalak.OTTify.user.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tavebalak.OTTify.common.constant.SocialType;
import tavebalak.OTTify.user.entity.User;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmailAndSocialType(String email, SocialType socialType);

    Optional<User> findByEmail(String email);
}
