package co.istad.mobilebanking.feature.transaction;

import co.istad.mobilebanking.domain.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TransactionRepository extends JpaRepository<Transaction, Integer> {

}
