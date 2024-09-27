package co.istad.mobilebanking.feature.transaction.dto;

import java.math.BigDecimal;

public record TopUpRequest(
        String senderActNo,
        String phoneNumber,
        BigDecimal amount,
        Boolean favorite

) {
}
