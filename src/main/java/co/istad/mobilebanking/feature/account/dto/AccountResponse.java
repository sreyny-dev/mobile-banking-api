package co.istad.mobilebanking.feature.account.dto;

import java.math.BigDecimal;

public record AccountResponse(
        String actNo,
        String alias,
        BigDecimal balance,
        BigDecimal transferLimit,
        Boolean isHidden,
        Boolean isDeleted
) {
}
