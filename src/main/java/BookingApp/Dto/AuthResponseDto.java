package BookingApp.Dto;

import lombok.Data;

@Data
public class AuthResponseDto {
    private String token;
    private String message;
}
