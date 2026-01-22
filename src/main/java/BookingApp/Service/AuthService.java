package BookingApp.Service;


import org.springframework.http.ResponseEntity;

import BookingApp.Dto.ForgotPasswordRequestDto;
import BookingApp.Dto.LoginRequestDto;
import BookingApp.Dto.RegisterRequestDto;
import BookingApp.Dto.ResetPasswordRequestDto;
import BookingApp.Dto.ResponseDto;
import BookingApp.Dto.VerifyOtpRequestDto;

public interface AuthService {
    ResponseEntity<ResponseDto> register(RegisterRequestDto request);

    ResponseEntity<ResponseDto> login(LoginRequestDto request);

    ResponseEntity<ResponseDto> verifyOtp(VerifyOtpRequestDto request);

    ResponseEntity<ResponseDto> forgotPassword(ForgotPasswordRequestDto request);

    ResponseEntity<ResponseDto> resetPassword(ResetPasswordRequestDto request);

    
}
