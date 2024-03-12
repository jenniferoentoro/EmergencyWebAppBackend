package fontys.emergencywebapps.persistence.entities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "tutorial")
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Tutorial {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "title")
    private String title;

    @Column(name = "description")
    private String description;

    @Column(name = "link_to_video")
    private String linkToVideo;

    @Column(name = "video_file")
    private String videoFile;

    @ManyToOne
    @JsonIgnoreProperties({"incidentReports"})
    private IncidentCategory incidentCategory;

}
