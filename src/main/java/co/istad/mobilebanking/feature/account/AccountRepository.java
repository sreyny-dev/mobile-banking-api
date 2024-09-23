package co.istad.mobilebanking.feature.account;

import co.istad.mobilebanking.domain.Account;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccountRepository extends JpaRepository<Account, Integer> {

}
