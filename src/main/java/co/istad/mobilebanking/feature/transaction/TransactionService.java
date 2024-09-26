package co.istad.mobilebanking.feature.transaction;

import co.istad.mobilebanking.feature.transaction.dto.*;

public interface TransactionService {

    TransactionResponse transfer(TransactionRequest transactionRequest);
    TopUpReponse mobileTopUp(TopUpRequest topUpRequest);
    TransactionResponse educationPayment(EducationPaymentRequest educationPaymentRequest);

}
