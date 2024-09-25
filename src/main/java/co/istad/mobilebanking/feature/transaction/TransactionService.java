package co.istad.mobilebanking.feature.transaction;

import co.istad.mobilebanking.feature.transaction.dto.TransactionRequest;
import co.istad.mobilebanking.feature.transaction.dto.TransactionResponse;

public interface TransactionService {

    TransactionResponse transfer(TransactionRequest transactionRequest);


}
