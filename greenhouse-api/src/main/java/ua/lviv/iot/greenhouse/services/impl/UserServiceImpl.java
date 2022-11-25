package ua.lviv.iot.greenhouse.services.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import ua.lviv.iot.greenhouse.dao.UserDao;
import ua.lviv.iot.greenhouse.models.User;
import ua.lviv.iot.greenhouse.services.UserService;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserDao userDao;

    @Override
    public User getByEmail(String email) {
        return userDao.findByEmail(email).orElseThrow(() -> new UsernameNotFoundException("User doesn't exists"));
    }

    @Override
    public User create(User user) {
        return userDao.save(user);
    }
}
