package ua.org.code.view.dto.user;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserDto {
    private String login;
    private String email;
    private String firstName;
    private String lastName;
    private String birthday;
    private String role;
}
