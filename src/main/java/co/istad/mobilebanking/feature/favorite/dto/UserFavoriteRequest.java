package co.istad.mobilebanking.feature.favorite.dto;

import jakarta.validation.constraints.NotBlank;

public record UserFavoriteRequest(

        @NotBlank(message = "phone number is required")
        String userPhoneNumber
) {
}
