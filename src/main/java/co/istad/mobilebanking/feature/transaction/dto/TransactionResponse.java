package co.istad.mobilebanking.feature.transaction.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record TransactionResponse(
        String senderActNo,
        String receiverActNo,
        BigDecimal amount,
        String remark,
        Boolean status,
        LocalDateTime transactionAt,
        String transactionType
) {
}
