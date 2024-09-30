package co.istad.mobilebanking.feature.auth.dto;

public record VerifyRequest (

        String email,
        String verificationCode

){
}
