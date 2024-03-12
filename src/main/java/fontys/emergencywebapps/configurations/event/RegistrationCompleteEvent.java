package fontys.emergencywebapps.configurations.event;

import fontys.emergencywebapps.controllers.dto.UserGetDTO;
import lombok.Getter;
import lombok.Setter;
import org.springframework.context.ApplicationEvent;

import java.io.Serializable;

@Setter
@Getter
public class RegistrationCompleteEvent extends ApplicationEvent implements Serializable {
    private transient UserGetDTO user;
    private String appUrl;

    public RegistrationCompleteEvent(UserGetDTO user, String appUrl) {
        super(user);
        this.user = user;
        this.appUrl = appUrl;
    }
}