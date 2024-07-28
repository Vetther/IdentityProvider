package pl.owolny.identityprovider.web.register;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class RegisterDto {

    @NotEmpty(message = "{username.not.empty}")
    @NotNull(message = "Username is required")
    @Size(min = 5, max = 20, message = "{username.size}")
    private String username;

    @Email(message = "Email must be valid")
    @NotEmpty(message = "Email cannot be empty")
    @Size(min = 5, max = 100, message = "Email must be between 5 and 100 characters")
    private String email;

    @NotEmpty(message = "Password cannot be empty")
    @NotNull(message = "Password is required")
    @Size(min = 6, max = 44, message = "Password must be between 6 and 44 characters")
    private String password;

    @NotEmpty(message = "Password cannot be empty")
    @NotNull(message = "Password is required")
    @Size(min = 6, max = 44, message = "Password must be between 6 and 44 characters")
    private String confirmPassword;
}
