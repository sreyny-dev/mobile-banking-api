package co.istad.mobilebanking.feature.user.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;
import java.time.LocalDate;

public record CreateUserRequest(
        @NotBlank(message = "name is required")
        String name,
        @NotBlank(message ="gender is required")
        String gender,
        @NotNull(message = "dte of birth is required")
        LocalDate dob,
        @NotBlank(message = "phone number is required")
        String phoneNumber,
        @NotBlank(message = "email is required")
        String email,
        @NotBlank(message = "password is required")
        @Size(min=4, max=20)
        String password,
        @NotBlank(message="confirm password is required")
        String confirmPassword,
        @NotBlank(message = "pin is required")
        String pin,

        String nationalCardId,
        String studentCardId,
        String university,

        String country,
        String khanOrDistrict,
        String sangkatOrCommune,
        String village,
        String street,


        String mainSourceOfIncome,
        BigDecimal monthlyIncomeRange,
        String companyName,
        String position,

        String profilePicture


){
}
