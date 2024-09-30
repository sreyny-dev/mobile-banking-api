package co.istad.mobilebanking.feature.auth;

import co.istad.mobilebanking.domain.EmailVerification;
import co.istad.mobilebanking.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface EmailVerificationRepository extends JpaRepository<EmailVerification, Integer> {

    Optional<EmailVerification> findByUser(User user);

}
