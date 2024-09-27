package co.istad.mobilebanking.feature.transaction;

import co.istad.mobilebanking.feature.transaction.dto.*;

import java.util.List;

public interface TransactionService {

    TransactionResponse transfer(TransactionRequest transactionRequest);
    TopUpReponse mobileTopUp(TopUpRequest topUpRequest);
    TransactionResponse educationPayment(EducationPaymentRequest educationPaymentRequest);
    List<TransactionResponse> getTransactions(String actNo);

}
