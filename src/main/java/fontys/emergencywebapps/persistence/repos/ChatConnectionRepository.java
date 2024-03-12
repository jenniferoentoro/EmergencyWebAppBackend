package fontys.emergencywebapps.persistence.repos;

import fontys.emergencywebapps.persistence.entities.ChatConnection;
import fontys.emergencywebapps.persistence.entities.StatusChatConnection;
import fontys.emergencywebapps.persistence.entities.TypeConnection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ChatConnectionRepository extends JpaRepository<ChatConnection, Long> {
    ChatConnection findByUserIdAndStatusChatConnection(Long id, StatusChatConnection statusChatConnection);

    @Query("SELECT cc FROM ChatConnection cc JOIN cc.user u WHERE u.username = :username AND cc.statusChatConnection = :statusChatConnection")
    ChatConnection findByUsernameAndStatusChatConnectionCustom(@Param("username") String username, @Param("statusChatConnection") StatusChatConnection statusChatConnection);

    int countByStatusChatConnection(StatusChatConnection statusChatConnection);

    int countByTypeConnectionAndStatusChatConnection(TypeConnection typeConnection, StatusChatConnection statusChatConnection);

    @Query("SELECT s FROM ChatConnection s JOIN HelpIncident h ON s.id = h.chatConnection.id WHERE h.incidentCategory.id = :categoryId AND h.statusHelpIncident = 'OPEN'")
    List<ChatConnection> findAllByIncidentCategoryAndStatusOpen(@Param("categoryId") Long categoryId);
}
