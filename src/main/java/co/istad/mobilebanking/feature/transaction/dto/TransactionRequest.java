package co.istad.mobilebanking.feature.transaction.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

public record TransactionRequest(
        @NotBlank(message = "senderActNo is required")
        String senderActNo,
        @NotBlank(message = "receiverActNo is required")
        String receiverActNo,
        @NotNull(message="amount is required!")
        @Positive
        BigDecimal amount,
        String remark,
        Boolean favorite
) {
}
