package co.istad.mobilebanking.feature.accounType;

import co.istad.mobilebanking.domain.AccountType;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface AccountTypeRepository extends CrudRepository<AccountType, Integer> {

    Optional<AccountType> findByAlias(String alias);
    Boolean existsByAlias(String alias);
}
