package com.utetea.backend.controller;
//VU VAN THONG 23162098
import com.utetea.backend.dto.ApiResponse;
import com.utetea.backend.dto.LoginRequest;
import com.utetea.backend.dto.LoginResponse;
import com.utetea.backend.dto.RegisterRequest;
import com.utetea.backend.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
@Tag(name = "🔐 Authentication", description = "Đăng ký, đăng nhập, JWT token")
public class AuthController {
    
    private final AuthService authService;
    
    @GetMapping("/health")
    @Operation(summary = "Health Check", description = "Kiểm tra service có hoạt động không")
    public ResponseEntity<ApiResponse<String>> health() {
        return ResponseEntity.ok(ApiResponse.success("Auth service is running"));
    }
    
    @PostMapping("/register")
    @Operation(summary = "Đăng ký tài khoản", description = "Tạo tài khoản USER mới với username, phone, password")
    public ResponseEntity<ApiResponse<LoginResponse>> register(@Valid @RequestBody RegisterRequest request) {
        LoginResponse response = authService.register(request);
        return ResponseEntity.ok(ApiResponse.success("Registration successful", response));
    }
    
    @PostMapping("/register-with-otp")
    @Operation(summary = "Đăng ký với OTP", description = "Bước 1: Đăng ký và gửi OTP qua email")
    public ResponseEntity<ApiResponse<String>> registerWithOtp(@Valid @RequestBody RegisterRequest request) {
        authService.registerWithOtp(request);
        return ResponseEntity.ok(ApiResponse.success("OTP sent to your email. Please verify to complete registration."));
    }
    
    @PostMapping("/verify-otp")
    @Operation(summary = "Xác thực OTP", description = "Bước 2: Xác thực OTP để hoàn tất đăng ký")
    public ResponseEntity<ApiResponse<LoginResponse>> verifyOtp(@Valid @RequestBody com.utetea.backend.dto.OtpRequest request) {
        LoginResponse response = authService.verifyOtpAndActivate(request.getPhone(), request.getOtp());
        return ResponseEntity.ok(ApiResponse.success("Account activated successfully", response));
    }
    
    @PostMapping("/resend-otp")
    @Operation(summary = "Gửi lại OTP", description = "Gửi lại mã OTP nếu hết hạn")
    public ResponseEntity<ApiResponse<String>> resendOtp(@RequestParam String phone) {
        authService.resendOtp(phone);
        return ResponseEntity.ok(ApiResponse.success("OTP resent successfully"));
    }
    
    @PostMapping("/login")
    @Operation(summary = "Đăng nhập", description = "Login với username/phone + password, trả về JWT token")
    public ResponseEntity<ApiResponse<LoginResponse>> login(@Valid @RequestBody LoginRequest request) {
        LoginResponse response = authService.login(request);
        return ResponseEntity.ok(ApiResponse.success("Login successful", response));
    }
}
