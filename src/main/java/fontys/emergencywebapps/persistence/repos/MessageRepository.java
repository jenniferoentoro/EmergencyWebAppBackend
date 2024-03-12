package fontys.emergencywebapps.persistence.repos;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import fontys.emergencywebapps.persistence.entities.Message;

import java.util.List;

public interface MessageRepository extends JpaRepository<Message, Long> {

    @Query("SELECT m FROM Message m " +
            "JOIN m.chatConnection c " +
            "WHERE c.id IN (" +
            "  SELECT cc.id FROM ChatConnection cc " +
            "  WHERE cc.user.username = :userId " +
            "  AND cc.statusChatConnection = 'ONGOING')")
    List<Message> findMessagesByUserIdAndOngoingChatConnection(@Param("userId") String userId);

}
