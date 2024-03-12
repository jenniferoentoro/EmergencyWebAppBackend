package fontys.emergencywebapps.controllers.dto;

import fontys.emergencywebapps.persistence.entities.TypeConnection;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ChatConnectionOngoing {


    @Enumerated(EnumType.STRING)
    private TypeConnection typeConnection;


}

