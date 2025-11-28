package com.utetea.backend.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
@Slf4j
public class OtpService {
    
    private final JavaMailSender mailSender;
    
    // Store OTP with phone as key: phone -> OTP
    private final Map<String, String> otpStorage = new ConcurrentHashMap<>();
    
    // Store OTP expiry time: phone -> expiry timestamp
    private final Map<String, Long> otpExpiryStorage = new ConcurrentHashMap<>();
    
    private static final long OTP_VALIDITY_MINUTES = 5; // OTP valid for 5 minutes
    
    /**
     * Generate 6-digit OTP
     */
    public String generateOtp() {
        Random random = new Random();
        int otp = 100000 + random.nextInt(900000); // 6 digits
        return String.valueOf(otp);
    }
    
    /**
     * Send OTP to phone (via email for now, can integrate SMS later)
     */
    public void sendOtp(String phone, String email) {
        String otp = generateOtp();
        
        // Store OTP
        otpStorage.put(phone, otp);
        
        // Store expiry time (current time + 5 minutes)
        long expiryTime = System.currentTimeMillis() + TimeUnit.MINUTES.toMillis(OTP_VALIDITY_MINUTES);
        otpExpiryStorage.put(phone, expiryTime);
        
        // Send OTP via email
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom("watershoputetea@gmail.com");
            message.setTo(email);
            message.setSubject("UTE Tea - Mã OTP xác thực tài khoản");
            message.setText(
                "Xin chào,\n\n" +
                "Mã OTP của bạn là: " + otp + "\n\n" +
                "Mã này có hiệu lực trong " + OTP_VALIDITY_MINUTES + " phút.\n\n" +
                "Nếu bạn không yêu cầu mã này, vui lòng bỏ qua email này.\n\n" +
                "Trân trọng,\n" +
                "UTE Tea Team"
            );
            
            mailSender.send(message);
            log.info("OTP sent to phone: {} (email: {})", phone, email);
        } catch (Exception e) {
            log.error("Failed to send OTP email: {}", e.getMessage());
            throw new RuntimeException("Failed to send OTP email");
        }
    }
    
    /**
     * Verify OTP
     */
    public boolean verifyOtp(String phone, String otp) {
        // Check if OTP exists
        String storedOtp = otpStorage.get(phone);
        if (storedOtp == null) {
            log.warn("No OTP found for phone: {}", phone);
            return false;
        }
        
        // Check if OTP expired
        Long expiryTime = otpExpiryStorage.get(phone);
        if (expiryTime == null || System.currentTimeMillis() > expiryTime) {
            log.warn("OTP expired for phone: {}", phone);
            // Clean up expired OTP
            otpStorage.remove(phone);
            otpExpiryStorage.remove(phone);
            return false;
        }
        
        // Verify OTP
        boolean isValid = storedOtp.equals(otp);
        
        if (isValid) {
            // Clean up after successful verification
            otpStorage.remove(phone);
            otpExpiryStorage.remove(phone);
            log.info("OTP verified successfully for phone: {}", phone);
        } else {
            log.warn("Invalid OTP for phone: {}", phone);
        }
        
        return isValid;
    }
    
    /**
     * Clear OTP for a phone number
     */
    public void clearOtp(String phone) {
        otpStorage.remove(phone);
        otpExpiryStorage.remove(phone);
    }
}
