package fontys.emergencywebapps.persistence.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class HelpIncident implements Serializable {

    private static final Long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private ChatConnection chatConnection;

    @ManyToOne
    private IncidentCategory incidentCategory;

    private String longitude;

    private String latitude;

    @ManyToOne
    private User helper;

    @Enumerated(EnumType.STRING)
    private StatusHelpIncident statusHelpIncident;

    private Date dateStarted;

    private Date dateEnded;



}
