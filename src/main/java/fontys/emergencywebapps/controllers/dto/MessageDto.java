package fontys.emergencywebapps.controllers.dto;

import fontys.emergencywebapps.persistence.entities.StatusMsg;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor

public class MessageDto {

    private String message;

    private StatusMsg statusMsg;

}
