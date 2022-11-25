package ua.lviv.iot.greenhouse.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ua.lviv.iot.greenhouse.enums.Role;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "user")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @NotBlank(message = "is required and must not be blank")
    @Size(max = 100, message = "must be up to 100 characters")
    @Column(name = "email", nullable = false)
    private String email;

    @NotBlank(message = "is required and must not be blank")
    @Size(max = 255, message = "must be up to 255 characters")
    @Column(name = "password", nullable = false)
    private String password;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "role", nullable = false)
    private Role role = Role.USER;

    @NotNull
    @Column(name = "is_banned", nullable = false)
    private Boolean isBanned = false;

    public User(String email, String password) {
        this.email = email;
        this.password = password;
    }
}
