package co.istad.mobilebanking.feature.transaction;

import co.istad.mobilebanking.domain.Account;
import co.istad.mobilebanking.domain.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface TransactionRepository extends JpaRepository<Transaction, Integer> {

    @Query("SELECT t from Transaction t WHERE t.sender=: account OR t.receiver=:account")
    List<Transaction> findAllByAccount(@Param("account")Account account);

}
