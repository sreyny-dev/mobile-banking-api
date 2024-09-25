package co.istad.mobilebanking.mapper;

import co.istad.mobilebanking.domain.Transaction;
import co.istad.mobilebanking.feature.transaction.dto.TransactionRequest;
import co.istad.mobilebanking.feature.transaction.dto.TransactionResponse;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface TransferMapper {

    Transaction fromTransactionRequest(TransactionRequest transactionRequest);
    TransactionResponse toTransactionResponse(Transaction transaction);

}
