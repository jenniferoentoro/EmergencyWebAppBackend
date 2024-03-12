package fontys.emergencywebapps.configurations.security.token;

public interface AccessTokenEncoder {
    String encode(AccessToken accessToken);
}
