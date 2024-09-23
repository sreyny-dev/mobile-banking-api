package co.istad.mobilebanking.feature.account.dto;

import java.math.BigDecimal;

public record CreateAccountRequest(
        String phoneNumber,
        String actNo,
        String alias,
        BigDecimal balance
) {
}
