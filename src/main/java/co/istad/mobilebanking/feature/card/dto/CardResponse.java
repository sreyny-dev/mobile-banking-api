package co.istad.mobilebanking.feature.card.dto;

public record CardResponse(
        String cardHolderName,
        String cardNumber,
        String cvv
) {
}
