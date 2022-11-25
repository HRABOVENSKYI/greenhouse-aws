package ua.lviv.iot.greenhouse.controllers;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import ua.lviv.iot.greenhouse.dto.auth.AuthenticationRequestDto;
import ua.lviv.iot.greenhouse.dto.user.UserRegistrationDto;
import ua.lviv.iot.greenhouse.enums.Role;
import ua.lviv.iot.greenhouse.models.User;
import ua.lviv.iot.greenhouse.security.JwtTokenProvider;
import ua.lviv.iot.greenhouse.services.UserService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

import static ua.lviv.iot.greenhouse.mappers.UserMapper.mapUserToUserDto;

@CrossOrigin(origins = "http://localhost:3000", allowCredentials = "true")
@Controller
@RequiredArgsConstructor
@RequestMapping("/user")
@Slf4j
public class UserController {

    private final AuthenticationManager authenticationManager;
    private final UserService userService;
    private final JwtTokenProvider jwtTokenProvider;

    @Value("${jwt.token.expiration}")
    private Long tokenExpiration;

    @PostMapping("/login")
    public ResponseEntity<?> authenticate(@RequestBody AuthenticationRequestDto request) {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                    request.getEmail(), request.getPassword()));
            log.info("User {} logged in successfully", request.getEmail());
        } catch (AuthenticationException e) {
            log.error("Authentication failed for user with the email {}", request.getEmail());
            return new ResponseEntity<>("Invalid email or password", HttpStatus.FORBIDDEN);
        }
        User user = userService.getByEmail(request.getEmail());
        Map<Object, Object> response = new HashMap<>();
        response.put("user", mapUserToUserDto(userService.getByEmail(request.getEmail())));
        response.put("token", jwtTokenProvider.createToken(user.getEmail(), user.getRole().name(), tokenExpiration));
        return ResponseEntity.ok(response);
    }

    @PostMapping("/register")
    public ResponseEntity<?> registration(@RequestBody UserRegistrationDto userRegistrationDto,
                                          BCryptPasswordEncoder bCryptPasswordEncoder) {
        String token = jwtTokenProvider.createToken(userRegistrationDto.getEmail(), Role.USER.name(), tokenExpiration);
        Map<Object, Object> response = new HashMap<>();
        response.put("user", mapUserToUserDto(userService.create(new User(
                userRegistrationDto.getEmail(),
                bCryptPasswordEncoder.encode(userRegistrationDto.getPassword())))));
        response.put("token", token);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/logout")
    public void logout(HttpServletRequest request, HttpServletResponse response) {
        SecurityContextLogoutHandler securityContextLogoutHandler = new SecurityContextLogoutHandler();
        securityContextLogoutHandler.logout(request, response, null);
    }

    @GetMapping("/validate-token")
    public ResponseEntity<Boolean> validate(@RequestHeader("token") String token) {
        return ResponseEntity.ok(jwtTokenProvider.validateToken(token));
    }
}
