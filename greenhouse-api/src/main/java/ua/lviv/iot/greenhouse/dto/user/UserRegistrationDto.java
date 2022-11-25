package ua.lviv.iot.greenhouse.dto.user;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UserRegistrationDto {
    private String email;
    private String password;
}
