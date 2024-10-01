package co.istad.mobilebanking.feature.auth;

import co.istad.mobilebanking.feature.auth.dto.*;
import jakarta.mail.MessagingException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/auth")
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    void registerUser(@Valid @RequestBody RegisterRequest registerRequest) throws MessagingException {
        authService.register(registerRequest);
    }

    @PostMapping("/verify")
    @ResponseStatus(HttpStatus.CREATED)
    void verifyUser(@Valid @RequestBody VerifyRequest verifyRequest) {
        authService.verify(verifyRequest);
    }

    @PutMapping("/resend-code/{email}")
    @ResponseStatus(HttpStatus.CREATED)
    void resendCode(@PathVariable String email) throws MessagingException {
        authService.resendVerificationCode(email);
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/login")
    JwtResponse login(@Valid @RequestBody LoginRequest loginRequest) {
        return authService.login(loginRequest);
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/refresh-token")
    JwtResponse refreshToken(@Valid @RequestBody RefreshTokenRequest refreshTokenRequest) {
        return authService.refreshToken(refreshTokenRequest);
    }

}
