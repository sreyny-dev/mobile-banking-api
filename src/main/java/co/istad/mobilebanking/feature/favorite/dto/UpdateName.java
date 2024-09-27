package co.istad.mobilebanking.feature.favorite.dto;

import jakarta.validation.constraints.NotBlank;

public record UpdateName(
        @NotBlank(message = "actNo is required")
        String actNo,
        @NotBlank(message = "new name is required")
        String name
) {
}
