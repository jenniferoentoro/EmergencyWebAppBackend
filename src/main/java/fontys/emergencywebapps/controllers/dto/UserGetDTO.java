package fontys.emergencywebapps.controllers.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import fontys.emergencywebapps.persistence.entities.UserRoles;

import java.util.Date;
import java.util.Set;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserGetDTO {
    private Long id;
    private String firstName;
    private String lastName;
    private Date birthDate;
    private String address;
    private String identityCardNumber;
    private String email;
    private String username;
    private String phoneNumber;
    private Set<UserRoles> roles;


}
