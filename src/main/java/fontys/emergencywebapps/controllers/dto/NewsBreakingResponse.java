package fontys.emergencywebapps.controllers.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class NewsBreakingResponse {

    private Long id;
    private Long newsId;

    private String title;

}
