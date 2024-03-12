package fontys.emergencywebapps.persistence.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.Date;

@Entity
@Table(name = "report_status")
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor

public class IncidentReportStatus {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "status_name")
    @Enumerated(EnumType.STRING)
    private Status status;

    @NotNull
    @Column(name = "status_description")
    private String description;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "incident_report_id")
    @ToString.Exclude
    private IncidentReport incidentReport;

    private Date date = new Date();
}