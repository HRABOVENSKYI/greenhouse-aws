package ua.lviv.iot.greenhouse.dto.auth;

import lombok.Data;

@Data
public class AuthenticationRequestDto {
    private String email;
    private String password;
}
