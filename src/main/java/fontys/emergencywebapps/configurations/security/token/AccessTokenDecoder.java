package fontys.emergencywebapps.configurations.security.token;

public interface AccessTokenDecoder {
    AccessToken decode(String accessTokenEncoded);
}
