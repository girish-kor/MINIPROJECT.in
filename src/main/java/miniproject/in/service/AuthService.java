package miniproject.in.service;

import lombok.RequiredArgsConstructor;
import miniproject.in.model.OtpToken;
import miniproject.in.model.User;
import miniproject.in.repository.OtpTokenRepository;
import miniproject.in.repository.UserRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final OtpTokenRepository otpTokenRepository;
    private final UserRepository userRepository;
    private final EmailService emailService;
    private final JwtService jwtService;

    @Value("${app.otp.expiration}")
    private Long otpExpiration;

    private final SecureRandom random = new SecureRandom();

    public void requestOtp(String email) {
        String otp = generateOtp();
        long now = System.currentTimeMillis();

        // Delete existing OTP for this email
        otpTokenRepository.deleteByEmail(email);

        // Create new OTP token
        OtpToken otpToken = OtpToken.builder().email(email).otp(otp).createdAt(now)
                .expiresAt(now + otpExpiration).build();

        otpTokenRepository.save(otpToken);
        emailService.sendOtp(email, otp);
    }

    public String verifyOtp(String email, String otp) {
        Optional<OtpToken> otpTokenOpt = otpTokenRepository.findByEmail(email);

        if (otpTokenOpt.isEmpty()) {
            throw new RuntimeException("OTP not found");
        }

        OtpToken otpToken = otpTokenOpt.get();

        if (System.currentTimeMillis() > otpToken.getExpiresAt()) {
            otpTokenRepository.deleteByEmail(email);
            throw new RuntimeException("OTP expired");
        }

        if (!otpToken.getOtp().equals(otp)) {
            throw new RuntimeException("Invalid OTP");
        }

        // OTP verified, delete it
        otpTokenRepository.deleteByEmail(email);

        // Create or get user
        User user = userRepository.findByEmail(email)
                .orElse(User.builder().email(email).createdAt(System.currentTimeMillis()).build());
        userRepository.save(user);

        return jwtService.generateToken(email);
    }

    private String generateOtp() {
        return String.format("%06d", random.nextInt(1000000));
    }
}
