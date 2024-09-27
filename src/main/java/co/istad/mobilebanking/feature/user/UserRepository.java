package co.istad.mobilebanking.feature.user;

import co.istad.mobilebanking.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
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

    @Query("SELECT ua.user FROM UserAccount ua JOIN ua.account a WHERE a.actNo = :actNo")
    Optional<User> findByUesrActNo(@Param("actNo") String actNo);
}


