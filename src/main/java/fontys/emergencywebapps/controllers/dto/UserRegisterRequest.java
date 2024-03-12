package fontys.emergencywebapps.controllers.dto;


import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.Set;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserRegisterRequest {

    @NotBlank(message = "First name is required")
    @Size(min = 3, max = 100, message = "First name must be between 3 and 100 characters long")
    private String firstName;
    @NotBlank(message = "Last name is required")
    @Size(min = 3, max = 100, message = "Last name must be between 3 and 100 characters long")
    private String lastName;
    @NotNull(message = "Birth date is required")
    private Date birthDate;
    @NotBlank(message = "Address is required")
    @Size(min = 3, max = 1000, message = "Address must be between 3 and 1000 characters long")
    private String address;
    @NotBlank(message = "Identity card number is required")
    @Pattern(regexp = "^.{9}$", message = "Identity card number must be exactly 9 characters long")
    private String identityCardNumber;
    @NotBlank(message = "Email is required")
    @Email(message = "Email must be valid")
    private String email;
    @NotBlank(message = "Username is required")
    @Size(min = 3, max = 100, message = "Username must be between 3 and 100 characters long")
    private String username;
    @NotBlank(message = "Phone number is required")
    @Size(min = 9, max = 10, message = "Phone number must be between 9 and 10 characters long")
    private String phoneNumber;
    @NotBlank(message = "Password is required")
    @Size(min = 8, max = 100, message = "Password must be between 3 and 100 characters long")
    private String password;
    @NotBlank(message = "Confirm password is required")
    private String confirmPassword;
    @NotNull(message = "Roles are required")
    private Set<String> roles;


}
