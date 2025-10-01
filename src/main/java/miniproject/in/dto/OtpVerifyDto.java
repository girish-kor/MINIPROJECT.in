package miniproject.in.dto;

import lombok.Data;

@Data
public class OtpVerifyDto {
    private String email;
    private String otp;
}
