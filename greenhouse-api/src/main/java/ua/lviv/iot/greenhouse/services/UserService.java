package ua.lviv.iot.greenhouse.services;

import ua.lviv.iot.greenhouse.models.User;

public interface UserService {
    User getByEmail(String email);

    User create(User user);
}
