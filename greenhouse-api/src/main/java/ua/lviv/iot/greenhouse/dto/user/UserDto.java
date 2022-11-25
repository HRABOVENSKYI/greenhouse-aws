package ua.lviv.iot.greenhouse.dto.user;

import lombok.AllArgsConstructor;
import lombok.Data;
import ua.lviv.iot.greenhouse.enums.Role;

@Data
@AllArgsConstructor
public class UserDto {
    private String email;
    private Role role;
    private boolean isBanned;
}
