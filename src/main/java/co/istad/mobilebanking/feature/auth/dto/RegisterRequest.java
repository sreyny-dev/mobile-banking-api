package co.istad.mobilebanking.feature.auth.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.time.LocalDate;

public record RegisterRequest(
        @NotBlank(message="name is required")
        String name,
        @NotBlank (message="gender is required")
        String gender,
        @NotBlank (message="dob is required")
        LocalDate dob,
        @NotBlank (message="phone number is required")
        String phoneNumber,
        @NotBlank (message="email is required")
        @Email
        String email,
        @NotBlank (message="password is required")
        String password,
        @NotBlank (message="confirm pw is required")
        String confirmPassword,
        @NotNull(message="pin is required")
        Integer pin,
        String studentCardId,
        String nationalCardId,
        String country,
        String khanOrDistrict,
        String sangkatOrCommune,
        String village,
        String street,
        String mainSourceOfIncome,
        BigDecimal monthlyIncomeRange,
        String companyName,
        String position,
        String employeeType,
        String profilePicture
) {
}
