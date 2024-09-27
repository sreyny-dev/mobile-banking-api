package co.istad.mobilebanking.feature.transaction;

import co.istad.mobilebanking.feature.transaction.dto.*;

import java.util.List;

public interface TransactionService {

    TransactionResponse transfer(TransactionRequest transactionRequest);
    TopUpReponse mobileTopUp(TopUpRequest topUpRequest);
    TransactionResponse educationPayment(EducationPaymentRequest educationPaymentRequest);
    List<TransactionResponse> getTransactions(String actNo);
    List<TransactionResponse> getAllTransactions();

    //view transaction between two accounts
    List<TransactionResponse> getTransactionsBetweenAccounts(String senderActNo, String receiverActNo);

    //find income
    List<TransactionResponse> getIncome(String actNo);

    //find expense
    List<TransactionResponse> getExpense(String actNo);

    void deleteTransactionById(Integer id);

}
