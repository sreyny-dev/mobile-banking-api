package co.istad.mobilebanking.feature.user.dto;

import jakarta.validation.constraints.NotBlank;

public record ChangePasswordRequest(
        @NotBlank(message = "phone number is required")
        String phoneNumber,
        @NotBlank(message = "old password is required")
        String oldPassword,
        @NotBlank(message = "new password is required")
        String newPassword,
        @NotBlank(message = "new password is required")
        String confirmNewPassword
) {
}
