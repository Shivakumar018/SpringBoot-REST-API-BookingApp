package BookingApp.Service;


import org.springframework.http.ResponseEntity;

import BookingApp.Dto.LoginRequestDto;
import BookingApp.Dto.RegisterRequestDto;
import BookingApp.Dto.ResponseDto;

public interface AuthService {
    ResponseEntity<ResponseDto> register(RegisterRequestDto request);

    ResponseEntity<ResponseDto> login(LoginRequestDto request);

    
}
