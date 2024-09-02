package pl.owolny.identityprovider.domain.otp;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.Random;
import java.util.concurrent.TimeUnit;

@Slf4j
@Service
public class OTPService {

    private final StringRedisTemplate redisTemplate;

    private static final String OTP_PREFIX = "otp:";
    private static final String SEND_ATTEMPTS_PREFIX = "send_attempts:";
    private static final String VERIFY_ATTEMPTS_PREFIX = "verify_attempts:";
    private static final int MAX_SEND_ATTEMPTS = 2;
    private static final long SEND_ATTEMPTS_PERIOD_SECONDS = 60;
    private static final int OTP_LENGTH = 6;
    private static final int OTP_EXPIRATION_SECONDS = 5 * 60;
    private static final int MAX_VERIFY_ATTEMPTS = 5;

    private final Random random;

    public OTPService(StringRedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
        this.random = new Random();
    }

    public OTP generateOTP(String email) {
        incrementSendAttempts(email);

        String otpKey = OTP_PREFIX + email;
        String existingOTP = redisTemplate.opsForValue().get(otpKey);
        if (existingOTP != null) {
            log.info("OTP already generated for email {}", email);
            return OTP.builder()
                    .email(email)
                    .otpCode(existingOTP)
                    .build();
        }

        String otpCode = generateNumericOTP();
        redisTemplate.opsForValue().set(otpKey, otpCode, Duration.ofSeconds(OTP_EXPIRATION_SECONDS));
        resetVerifyAttempts(email);

        log.info("OTP generated for email {}", email);
        return OTP.builder()
                .email(email)
                .otpCode(otpCode)
                .build();
    }

    public boolean isVerified(String email, String otpCode) {
        String otpKey = OTP_PREFIX + email;
        String storedOTP = redisTemplate.opsForValue().get(otpKey);

        if (storedOTP != null && storedOTP.equals(otpCode)) {
            redisTemplate.delete(otpKey);
            resetVerifyAttempts(email);
            return true;
        }

        incrementVerifyAttempts(email);
        return false;
    }

    private void incrementSendAttempts(String email) {
        incrementValue(email, SEND_ATTEMPTS_PREFIX, SEND_ATTEMPTS_PERIOD_SECONDS);
    }

    private void incrementVerifyAttempts(String email) {
        incrementValue(email, VERIFY_ATTEMPTS_PREFIX, OTP_EXPIRATION_SECONDS);
    }

    public boolean isSendAttemptsLimitExceeded(String email) {
        String sendAttemptsKey = SEND_ATTEMPTS_PREFIX + email;
        Long attempts = redisTemplate.opsForValue().increment(sendAttemptsKey, 0);
        return attempts != null && attempts >= MAX_SEND_ATTEMPTS;
    }

    public boolean isVerifyAttemptsLimitExceeded(String email) {
        String verifyAttemptsKey = VERIFY_ATTEMPTS_PREFIX + email;
        Long attempts = redisTemplate.opsForValue().increment(verifyAttemptsKey, 0);
        return attempts != null && attempts >= MAX_VERIFY_ATTEMPTS;
    }

    private void resetVerifyAttempts(String email) {
        String verifyAttemptsKey = VERIFY_ATTEMPTS_PREFIX + email;
        redisTemplate.delete(verifyAttemptsKey);
    }

    private String generateNumericOTP() {
        StringBuilder otp = new StringBuilder(OTPService.OTP_LENGTH);
        for (int i = 0; i < OTPService.OTP_LENGTH; i++) {
            otp.append(random.nextInt(10));
        }
        return otp.toString();
    }

    private void incrementValue(String email, String prefix, long expirationSeconds) {
        String verifyAttemptsKey = prefix + email;
        Long attempts = redisTemplate.opsForValue().increment(verifyAttemptsKey, 1);
        if (attempts == null) {
            redisTemplate.opsForValue().set(verifyAttemptsKey, "1", Duration.ofSeconds(expirationSeconds));
            return;
        }
        if (attempts == 1) {
            redisTemplate.expire(verifyAttemptsKey, expirationSeconds, TimeUnit.SECONDS);
        }
    }
}