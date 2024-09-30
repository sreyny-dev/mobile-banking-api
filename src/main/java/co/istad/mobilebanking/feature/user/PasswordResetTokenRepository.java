package co.istad.mobilebanking.feature.user;

import co.istad.mobilebanking.domain.PasswordReset;
import co.istad.mobilebanking.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PasswordResetTokenRepository extends JpaRepository<PasswordReset, Integer> {

    Optional<PasswordReset> findByToken(String token);
    void deleteByUser(User user);
}
