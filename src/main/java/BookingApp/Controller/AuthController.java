package BookingApp.Controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import BookingApp.Dto.ForgotPasswordRequestDto;
import BookingApp.Dto.LoginRequestDto;
import BookingApp.Dto.RegisterRequestDto;
import BookingApp.Dto.ResetPasswordRequestDto;
import BookingApp.Dto.ResponseDto;
import BookingApp.Dto.VerifyOtpRequestDto;
import BookingApp.Service.AuthService;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<ResponseDto> register(@RequestBody RegisterRequestDto request) {
        return authService.register(request);
    }

    @PostMapping("/login")
    public ResponseEntity<ResponseDto> login(@RequestBody LoginRequestDto request) {
        return authService.login(request);
    }

    @PostMapping("/verify-otp")
    public ResponseEntity<ResponseDto> verifyOtp(
            @RequestBody VerifyOtpRequestDto request) {
        return authService.verifyOtp(request);
    }

    @PostMapping("/forgot-password")
    public ResponseEntity<ResponseDto> forgotPassword(
            @RequestBody ForgotPasswordRequestDto request) {
        return authService.forgotPassword(request);
    }

    @PostMapping("/reset-password")
    public ResponseEntity<ResponseDto> resetPassword(
            @RequestBody ResetPasswordRequestDto request) {
        return authService.resetPassword(request);
    }

}