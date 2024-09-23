package co.istad.mobilebanking.feature.user.dto;

import jakarta.validation.constraints.NotBlank;

public record UpdateUserRequest(
        @NotBlank(message = "Phone number is required")
        String phoneNumber,
        @NotBlank(message="new name is required")
        String name
) {
}
