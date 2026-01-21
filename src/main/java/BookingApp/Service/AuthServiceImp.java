package BookingApp.Service;

import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import BookingApp.Dto.LoginRequestDto;
import BookingApp.Dto.RegisterRequestDto;
import BookingApp.Dto.ResponseDto;
import BookingApp.Entity.User;
import BookingApp.Exception.DataNotFoundException;
import BookingApp.Repository.UserRepository;
import BookingApp.Util.JwtUtil;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthServiceImp implements AuthService{
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    @Override
    public ResponseEntity<ResponseDto> register(RegisterRequestDto request) {

        User user = new User();
        user.setName(request.getName());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRole("ROLE_" + request.getRole().toUpperCase());

        userRepository.save(user);

        return ResponseEntity.status(200).body(new ResponseDto(null, "User registered successfully"));
    }

    @Override
    public ResponseEntity<ResponseDto> login(LoginRequestDto request) {

        User user = userRepository.findByEmail(request.getEmail()).orElseThrow(() -> new DataNotFoundException("Invalid credentials"));
        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new RuntimeException("Invalid credentials");
        }
        String token = jwtUtil.generateToken(user.getEmail(), user.getRole());
        return ResponseEntity.status(200).body(new ResponseDto(token, "Login successful"));
    }
}
