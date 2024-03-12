package fontys.emergencywebapps.business;

import fontys.emergencywebapps.controllers.dto.*;
import fontys.emergencywebapps.persistence.entities.User;

public interface AuthenticationUseCases {
    UserAuthenticationResponse register(UserRegisterRequest request);
    UserAuthenticationResponse authenticate(UserLoginRequest request);

    UserAuthenticationResponse authenticateCompare(UserLoginRequest request);

    UserAuthenticationResponse registerUser(UserRegisterForUserRequest request);

    User userAskVerificationToken(UserVerificationRequest request);


}
