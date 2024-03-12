package fontys.emergencywebapps.persistence.entities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Entity
@Table(name = "news")
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class News {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "title")
    private String title;

    @Column(name = "description")
    private String description;

    @Column(nullable = false, columnDefinition = "DATE")
    private Date date;

    @Column(name = "image")
    private String image;

    @ManyToOne
    @JsonIgnoreProperties({"incidentReports"})
    private IncidentCategory incidentCategory;
}
