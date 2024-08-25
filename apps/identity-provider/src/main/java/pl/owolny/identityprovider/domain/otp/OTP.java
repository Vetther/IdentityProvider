package pl.owolny.identityprovider.domain.otp;

import lombok.Builder;

@Builder
public record OTP(String email, String otpCode) {
}
