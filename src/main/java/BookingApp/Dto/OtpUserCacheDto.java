package BookingApp.Dto;

import lombok.Data;
import java.io.Serializable;

@Data
public class OtpUserCacheDto {
    private static final long serialVersionUID = 1L;
    private String name;
    private String email;
    private String password;
    private String role;
    private String otp;
}