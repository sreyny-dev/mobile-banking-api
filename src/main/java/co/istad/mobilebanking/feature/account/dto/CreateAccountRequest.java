package co.istad.mobilebanking.feature.account.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record CreateAccountRequest(
        @NotBlank(message = "Phone number is required")
        String phoneNumber,
        @NotBlank(message = "actNo is required")
        String actNo,
        @NotBlank(message = "alias is required")
        String alias,
        @NotBlank(message = "accountTypeAlias is required")
        String accountTypeAlias,
        @NotNull(message = "balance is required")
        BigDecimal balance
) {
}
