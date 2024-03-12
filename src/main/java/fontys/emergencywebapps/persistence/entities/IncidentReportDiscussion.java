package fontys.emergencywebapps.persistence.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.Date;

@Entity
@Table(name = "report_discussion")
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor

public class IncidentReportDiscussion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "discussion_text")
    private String description;

    @ManyToOne
    @JoinColumn(name = "incident_report_id")
    @ToString.Exclude
    private IncidentReport incidentReport;

    @ManyToOne
    @JoinColumn(name = "user_id")
    @ToString.Exclude
    private User user;

    private Date date = new Date();
}
