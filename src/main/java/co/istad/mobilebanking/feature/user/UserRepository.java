package co.istad.mobilebanking.feature.user;

import co.istad.mobilebanking.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
    Boolean existsByPhoneNumber(String phoneNumber);
    Boolean existsByEmail(String email);
    Boolean existsByNationalCardId(String nationalCardId);
    Boolean existsByStudentCardId(String studentCardId);
    Optional<User> findByEmail(String email);
    Optional<User> findByPhoneNumber(String phoneNumber);
}
