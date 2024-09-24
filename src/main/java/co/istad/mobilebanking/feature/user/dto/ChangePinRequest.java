package co.istad.mobilebanking.feature.user.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record ChangePinRequest(
        @NotBlank(message="phone number is required")
        String phoneNumber,
        @NotBlank(message = "new pin is required")
        @Size(min=4, max=4)
        String newPin
) {
}
