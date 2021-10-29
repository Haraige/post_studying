package ua.org.code.view.dto.user;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import ua.org.code.view.dto.annotations.Date;
import ua.org.code.view.dto.annotations.EqualPasswords;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

@Getter
@Setter
@EqualPasswords(baseField = "password", matchField = "repeatPassword", message = "Passwords not matching")
@ToString
@XmlRootElement
public class CreateEditUserDto {
    @NotNull
    @NotBlank
    private String login;

    @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)[a-zA-Z\\d]{8,}$|^$",
            message = "Password not require to pattern (Minimum eight characters,"
                    + " at least one uppercase letter, one lowercase letter and one number)")
    private String password;

    private String repeatPassword;

    @NotNull
    @NotBlank
    @Pattern(regexp = "(?:[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*|"
            + "\"(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21\\x23-\\x5b\\x5d-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])"
            + "*\")@(?:(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?|\\"
            + "[(?:(?:(2(5[0-5]|[0-4][0-9])|1[0-9][0-9]|[1-9]?[0-9]))\\.){3}(?:(2(5[0-5]|[0-4][0-9])|1[0-9][0-9]|[1-9]?[0-9])|[a-z0-9-]*[a-z0-9]"
            + ":(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21-\\x5a\\x53-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])+)])", message = "Email incorrect")
    private String email;

    @NotNull
    @NotBlank
    @Size(min = 2, message = "Minimal first name size is 2")
    private String firstName;

    @NotNull
    @NotBlank
    @Size(min = 2, message = "Minimal last name size is 2")
    private String lastName;

    @NotNull
    @NotBlank
    @Date(message = "Incorrect date")
    private String birthday;

    @NotNull
    @NotBlank
    private String role;
}
