package com.utetea.backend.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OtpRequest {
    
    @NotBlank(message = "Phone is required")
    private String phone;
    
    @NotBlank(message = "OTP is required")
    private String otp;
}
