package co.istad.mobilebanking.feature.transaction.dto;

import java.math.BigDecimal;

public record EducationPaymentRequest(

        String senderActNo,
        //I assume each school has their own actNo
        String receiverActNo,
        String studentId,
        BigDecimal amount

) {
}
