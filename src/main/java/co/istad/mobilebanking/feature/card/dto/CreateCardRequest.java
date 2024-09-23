package co.istad.mobilebanking.feature.card.dto;

import jakarta.validation.constraints.NotBlank;

public record CreateCardRequest(
        @NotBlank(message ="account number is required")
        String actNo,
        @NotBlank(message = "card number is required")
        String cardNumber,
        @NotBlank(message = "card type is required")
        String cardTypeAlias,
        @NotBlank(message = "card holder name is required")
        String cardHolderName,
        @NotBlank(message = "cvv number is required")
        String cvv

) {

}
