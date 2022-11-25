package ua.lviv.iot.greenhouse.mappers;

import ua.lviv.iot.greenhouse.dto.user.UserDto;
import ua.lviv.iot.greenhouse.models.User;

public class UserMapper {

    private UserMapper() {
    }

    public static UserDto mapUserToUserDto(User user) {
        return new UserDto(
                user.getEmail(),
                user.getRole(),
                user.getIsBanned()
        );
    }
}
