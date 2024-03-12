package fontys.emergencywebapps.persistence.entities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
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
public class IncidentReport implements Serializable {
    private static final Long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JsonIgnoreProperties({"incidentReports"})
    private IncidentCategory incidentCategory;

    @Column(name = "incident_title", length = 100, nullable = false)
    private String title;

    @Column(name = "incident_desc", length = 1000, nullable = false)
    private String description;

    @Column(name = "incident_photo_url", length = 100, nullable = false)
    private String photoUrl;

    @Column(name = "longitude", nullable = false)
    private Double longitude;

    @Column(name = "latitude", nullable = false)
    private Double latitude;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "incident_date")
    private Date date = new Date();

    @ManyToOne
    private User user;

    @Column(name = "status", nullable = false)
    private Boolean statusFixed = false;



}
