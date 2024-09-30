package co.istad.mobilebanking.feature.auth;

import co.istad.mobilebanking.feature.auth.dto.RegisterRequest;
import co.istad.mobilebanking.feature.auth.dto.VerifyRequest;
import jakarta.mail.MessagingException;

public interface AuthService {
    void register(RegisterRequest registerRequest) throws MessagingException;
    void verify(VerifyRequest verifyRequest);
    void resendVerificationCode(String email) throws MessagingException;


}
