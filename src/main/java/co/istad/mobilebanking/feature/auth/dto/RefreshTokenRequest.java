package co.istad.mobilebanking.feature.auth.dto;

import jakarta.validation.constraints.NotBlank;

public record RefreshTokenRequest(
        @NotBlank(message = "Token is required")
        String refreshToken
) {
}
