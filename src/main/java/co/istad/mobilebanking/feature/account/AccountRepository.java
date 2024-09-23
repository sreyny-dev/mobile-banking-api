package co.istad.mobilebanking.feature.account;

import co.istad.mobilebanking.domain.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface AccountRepository extends JpaRepository<Account, Integer> {
    Optional<Account> findByActNo(String actNo);
    Boolean existsByActNo(String actNo);
    @Query("SELECT a FROM Account a JOIN UserAccount ua ON a.id = ua.account.id " +
            "JOIN User u ON ua.user.id = u.id WHERE u.phoneNumber = :phoneNumber")
    List<Account> findAccountsByPhoneNumber(String phoneNumber);
}
