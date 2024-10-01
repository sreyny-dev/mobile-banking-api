package co.istad.mobilebanking.feature.auth;

import co.istad.mobilebanking.feature.auth.dto.*;
import jakarta.mail.MessagingException;

public interface AuthService {
    void register(RegisterRequest registerRequest) throws MessagingException;
    void verify(VerifyRequest verifyRequest);
    void resendVerificationCode(String email) throws MessagingException;
    JwtResponse login(LoginRequest loginRequest);
    JwtResponse refreshToken(RefreshTokenRequest refreshToken);

}
