package co.istad.mobilebanking.mapper;

import co.istad.mobilebanking.domain.Transaction;
import co.istad.mobilebanking.feature.transaction.dto.EducationPaymentRequest;
import co.istad.mobilebanking.feature.transaction.dto.TopUpReponse;
import co.istad.mobilebanking.feature.transaction.dto.TopUpRequest;
import co.istad.mobilebanking.feature.transaction.dto.TransactionResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface PaymentMapper {


   Transaction fromTopUpRequest(TopUpRequest topUpRequest);

    @Mapping(source = "sender.actNo", target="senderActNo")
    TopUpReponse toTopUpResponse(Transaction transaction);

    Transaction fromEducationPaymentRequest(EducationPaymentRequest educationPaymentRequest);

    @Mapping(source = "sender.actNo", target="senderActNo")
    @Mapping(source="receiver.actNo", target="receiverActNo")
    TransactionResponse toTransactionResponse(Transaction transaction);




}
