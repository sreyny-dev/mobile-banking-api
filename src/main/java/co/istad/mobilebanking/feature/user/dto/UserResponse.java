package co.istad.mobilebanking.feature.user.dto;

public record UserResponse(
        String name,
        String gender,
        String dob,
        String email,
        String phoneNumber,
        Boolean isDeleted

) {
}
