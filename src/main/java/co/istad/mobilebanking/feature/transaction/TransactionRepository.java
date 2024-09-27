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

    @Query("SELECT t FROM Transaction t WHERE (t.sender.actNo = ?1 AND t.receiver.actNo = ?2) OR (t.sender.actNo = ?2 AND t.receiver.actNo = ?1)")
    List<Transaction> findAllBetweenAccounts(String senderActNo, String receiverActNo);

    @Query("SELECT t from Transaction t WHERE t.receiver.actNo =?1")
    List<Transaction> findIncome(String actNo);

    @Query("SELECT t from Transaction t WHERE t.sender.actNo = ?1")
    List<Transaction> findExpense(String actNo);

}
