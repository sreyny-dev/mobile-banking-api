package co.istad.mobilebanking.mapper;

import co.istad.mobilebanking.domain.Transaction;
import co.istad.mobilebanking.feature.transaction.dto.TransactionRequest;
import co.istad.mobilebanking.feature.transaction.dto.TransactionResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface TransferMapper {

    Transaction fromTransactionRequest(TransactionRequest transactionRequest);

    @Mapping(source = "sender.actNo", target="senderActNo")
    @Mapping(source="receiver.actNo", target="receiverActNo")
    TransactionResponse toTransactionResponse(Transaction transaction);

    List<TransactionResponse> toTransactionResponseList(List<Transaction> transaction);

}
