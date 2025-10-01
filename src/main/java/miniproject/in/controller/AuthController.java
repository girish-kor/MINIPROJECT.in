package miniproject.in.controller;

import lombok.RequiredArgsConstructor;
import miniproject.in.dto.ApiResponse;
import miniproject.in.dto.AuthResponse;
import miniproject.in.dto.OtpRequestDto;
import miniproject.in.dto.OtpVerifyDto;
import miniproject.in.service.AuthService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/request-otp")
    public ResponseEntity<ApiResponse> requestOtp(@RequestBody OtpRequestDto request) {
        try {
            authService.requestOtp(request.getEmail());
            return ResponseEntity.ok(
                    ApiResponse.builder().success(true).message("OTP sent to your email").build());
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.builder().success(false).message(e.getMessage()).build());
        }
    }

    @PostMapping("/verify-otp")
    public ResponseEntity<ApiResponse> verifyOtp(@RequestBody OtpVerifyDto request) {
        try {
            String token = authService.verifyOtp(request.getEmail(), request.getOtp());
            AuthResponse authResponse = AuthResponse.builder().token(token)
                    .email(request.getEmail()).message("Login successful").build();

            return ResponseEntity.ok(ApiResponse.builder().success(true)
                    .message("OTP verified successfully").data(authResponse).build());
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.builder().success(false).message(e.getMessage()).build());
        }
    }
}
