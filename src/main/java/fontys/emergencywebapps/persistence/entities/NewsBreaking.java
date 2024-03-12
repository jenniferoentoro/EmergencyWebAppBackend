package fontys.emergencywebapps.persistence.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "news_breaking")
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class NewsBreaking {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @ManyToOne
    private News news;

    private String title;
}
