package BookingApp.Service;

import BookingApp.Dto.*;
import BookingApp.Entity.User;
import BookingApp.Exception.DataExitsException;
import BookingApp.Exception.DataNotFoundException;
import BookingApp.Repository.UserRepository;
import BookingApp.Util.JwtUtil;
import BookingApp.Util.OtpUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final RedisTemplate<String, Object> redisTemplate;
    private final JavaMailSender mailSender;

    @Override
    public ResponseEntity<ResponseDto> register(RegisterRequestDto request) {

    if (userRepository.findByEmail(request.getEmail()).isPresent()) {
        throw new DataExitsException("Email already registered");
    }

    String role = request.getRole();
    if (role == null) {
        throw new RuntimeException("Role is required");
    }

    role = role.toUpperCase();
    if (!role.startsWith("ROLE_")) {
        role = "ROLE_" + role;
    }

    if (!role.equals("ROLE_ADMIN") && !role.equals("ROLE_USER")) {
        throw new RuntimeException("Invalid role");
    }

    String otp = OtpUtil.generateOtp();

    OtpUserCacheDto cache = new OtpUserCacheDto();
    cache.setName(request.getName());
    cache.setEmail(request.getEmail());
    cache.setPassword(passwordEncoder.encode(request.getPassword()));
    cache.setRole(role);
    cache.setOtp(otp);

    redisTemplate.opsForValue()
            .set(request.getEmail(), cache, 5, TimeUnit.MINUTES);

    sendOtpMail(request.getEmail(), otp);

    return ResponseEntity.ok(
            new ResponseDto("OTP sent to email", cache.getName())
    );
}


    @Override
    public ResponseEntity<ResponseDto> login(LoginRequestDto request) {

        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new DataNotFoundException("User not found"));

        if (!user.isVerified()) {
            throw new DataNotFoundException("Email not verified");
        }

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new DataNotFoundException("Invalid credentials");
        }

        String token = jwtUtil.generateToken(user.getEmail(), user.getRole());

        return ResponseEntity.ok(
                new ResponseDto("Login successfully", token)
        );
    }

    public ResponseEntity<ResponseDto> verifyOtp(VerifyOtpRequestDto request) {

        OtpUserCacheDto cache =
                (OtpUserCacheDto) redisTemplate.opsForValue().get(request.getEmail());

        if (cache == null || !cache.getOtp().equals(request.getOtp())) {
            throw new DataNotFoundException("Invalid or expired OTP");
        }

        User user = new User();
        user.setName(cache.getName());
        user.setEmail(cache.getEmail());
        user.setPassword(cache.getPassword());
        user.setRole(cache.getRole());
        user.setVerified(true);

        userRepository.save(user);
        redisTemplate.delete(request.getEmail());

        return ResponseEntity.ok(
                new ResponseDto("Email verified successfully", user)
        );
    }

    private void sendOtpMail(String toEmail, String otp) {

        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(toEmail);
        message.setSubject("OTP Verification");
        message.setText(
                "Your OTP is: " + otp +
                "\nThis OTP is valid for 5 minutes."
        );

        mailSender.send(message);
    }

    public ResponseEntity<ResponseDto> forgotPassword(ForgotPasswordRequestDto request) {

    User user = userRepository.findByEmail(request.getEmail())
            .orElseThrow(() -> new RuntimeException("Email not registered"));

    if (!user.isVerified()) {
        throw new RuntimeException("Email not verified");
    }

    String otp = OtpUtil.generateOtp();

    redisTemplate.opsForValue()
            .set("RESET_" + request.getEmail(), otp, 5, TimeUnit.MINUTES);

    sendOtpMail(request.getEmail(), otp);

    return ResponseEntity.ok(
            new ResponseDto("OTP sent for password reset", null)
    );
}

public ResponseEntity<ResponseDto> resetPassword(ResetPasswordRequestDto request) {

    String key = "RESET_" + request.getEmail();
    String cachedOtp = (String) redisTemplate.opsForValue().get(key);

    if (cachedOtp == null || !cachedOtp.equals(request.getOtp())) {
        throw new RuntimeException("Invalid or expired OTP");
    }

    User user = userRepository.findByEmail(request.getEmail())
            .orElseThrow(() -> new RuntimeException("User not found"));

    user.setPassword(passwordEncoder.encode(request.getNewPassword()));
    userRepository.save(user);

    redisTemplate.delete(key);

    return ResponseEntity.ok(
            new ResponseDto("Password reset successful", null)
    );
}

}
