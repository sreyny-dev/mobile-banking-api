package co.istad.mobilebanking.feature.user.dto;

public record UserProfileResponse(
        String profileImg,
        String phoneNumber,
        String name,
        String gender,
        String dob,
        String email,
        Boolean isVerified,
        Boolean isDeleted
) {
}
