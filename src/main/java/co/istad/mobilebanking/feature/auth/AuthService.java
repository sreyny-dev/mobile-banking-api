package co.istad.mobilebanking.feature.auth;

import co.istad.mobilebanking.feature.auth.dto.RegisterRequest;

public interface AuthService {
    void register(RegisterRequest registerRequest);
}
