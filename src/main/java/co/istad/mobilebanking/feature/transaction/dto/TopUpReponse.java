package co.istad.mobilebanking.feature.transaction.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record TopUpReponse(
        String senderActNo,
        String phoneNumber,
        BigDecimal amount,
        LocalDateTime transactionAt,
        Boolean status,
        Boolean isDeleted
) {
}
