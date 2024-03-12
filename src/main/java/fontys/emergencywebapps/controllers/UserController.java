package fontys.emergencywebapps.controllers;

import fontys.emergencywebapps.business.UserUseCases;
import fontys.emergencywebapps.controllers.dto.UserGetDTO;
import fontys.emergencywebapps.persistence.entities.User;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/user")
@CrossOrigin(origins = "*", maxAge = 3600)
@AllArgsConstructor
public class UserController {
    private final UserUseCases userService;

    @GetMapping
    public ResponseEntity<Iterable<UserGetDTO>> findAll() {
        Iterable<User> users = userService.getUsers();
        ArrayList<UserGetDTO> userGetResponses = new ArrayList<>();
        for (User user : users) {
            userGetResponses.add(UserGetDTO.builder()
                    .id(user.getId())
                    .firstName(user.getFirstName())
                    .lastName(user.getLastName())
                    .birthDate(user.getBirthDate())
                    .address(user.getAddress())
                    .identityCardNumber(user.getIdentityCardNumber())
                    .email(user.getEmail())
                    .username(user.getUsername())
                    .phoneNumber(user.getPhoneNumber())
                    .roles(user.getRoles())
                    .build());

        }
        return ResponseEntity.ok(userGetResponses);
    }

    @GetMapping("{username}")
    public ResponseEntity<UserGetDTO> findByUsername(@PathVariable("username") String username) {
        User user = userService.getUser(username);
        return ResponseEntity.ok(UserGetDTO.builder()
                .id(user.getId())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .birthDate(user.getBirthDate())
                .address(user.getAddress())
                .identityCardNumber(user.getIdentityCardNumber())
                .email(user.getEmail())
                .username(user.getUsername())
                .phoneNumber(user.getPhoneNumber())
                .roles(user.getRoles())
                .build());
    }

    @GetMapping("/email/{email}")
    public ResponseEntity<UserGetDTO> findByEmail(@PathVariable("email") String email) {
        User user = userService.getUserFromEmail(email);
        return ResponseEntity.ok(UserGetDTO.builder()
                .id(user.getId())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .birthDate(user.getBirthDate())
                .address(user.getAddress())
                .identityCardNumber(user.getIdentityCardNumber())
                .email(user.getEmail())
                .username(user.getUsername())
                .phoneNumber(user.getPhoneNumber())
                .roles(user.getRoles())
                .build());
    }

    @GetMapping("/me")
    public ResponseEntity<UserGetDTO> getMe(HttpServletRequest servletRequest) {
        User user = userService.getMe(servletRequest);
        return ResponseEntity.ok(UserGetDTO.builder()
                .id(user.getId())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .birthDate(user.getBirthDate())
                .address(user.getAddress())
                .identityCardNumber(user.getIdentityCardNumber())
                .email(user.getEmail())
                .username(user.getUsername())
                .phoneNumber(user.getPhoneNumber())
                .roles(user.getRoles())
                .build());
    }

    @GetMapping("/chatAdmins")
    public ResponseEntity<List<UserGetDTO>> getChatAdmins() {
        List<User> users = userService.getChatAdmins();
        ArrayList<UserGetDTO> userGetResponses = new ArrayList<>();
        for (User user : users) {
            userGetResponses.add(UserGetDTO.builder()
                    .id(user.getId())
                    .firstName(user.getFirstName())
                    .lastName(user.getLastName())
                    .birthDate(user.getBirthDate())
                    .address(user.getAddress())
                    .identityCardNumber(user.getIdentityCardNumber())
                    .email(user.getEmail())
                    .username(user.getUsername())
                    .phoneNumber(user.getPhoneNumber())
                    .roles(user.getRoles())
                    .build());

        }
        return ResponseEntity.ok(userGetResponses);
    }

    @GetMapping("/admins")
    public ResponseEntity<List<UserGetDTO>> getAdmins() {
        System.out.println("getAdmins");
        List<User> users = userService.getAdmins();
        ArrayList<UserGetDTO> userGetResponses = new ArrayList<>();
        for (User user : users) {
            userGetResponses.add(UserGetDTO.builder()
                    .id(user.getId())
                    .firstName(user.getFirstName())
                    .lastName(user.getLastName())
                    .birthDate(user.getBirthDate())
                    .address(user.getAddress())
                    .identityCardNumber(user.getIdentityCardNumber())
                    .email(user.getEmail())
                    .username(user.getUsername())
                    .phoneNumber(user.getPhoneNumber())
                    .roles(user.getRoles())
                    .build());

        }
        return ResponseEntity.ok(userGetResponses);
    }

    @GetMapping("/role/users")
    public ResponseEntity<List<UserGetDTO>> getUsers() {
        List<User> users = userService.getUsersRole();
        ArrayList<UserGetDTO> userGetResponses = new ArrayList<>();
        for (User user : users) {
            userGetResponses.add(UserGetDTO.builder()
                    .id(user.getId())
                    .firstName(user.getFirstName())
                    .lastName(user.getLastName())
                    .birthDate(user.getBirthDate())
                    .address(user.getAddress())
                    .identityCardNumber(user.getIdentityCardNumber())
                    .email(user.getEmail())
                    .username(user.getUsername())
                    .phoneNumber(user.getPhoneNumber())
                    .roles(user.getRoles())
                    .build());

        }
        return ResponseEntity.ok(userGetResponses);
    }
}
