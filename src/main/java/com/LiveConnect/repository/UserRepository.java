package com.LiveConnect.repository;

import com.LiveConnect.model.Chat;
import com.LiveConnect.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {

    Optional<User> findByUsername(String username);

    boolean existsByUsername(String username);

    @Query(value =
            "SELECT c.id AS chat_id, NULL AS chat_group_id, u1.username AS user1, u2.username AS user2, NULL AS group_name," +
            "NULL AS group_description, MAX(m.timestamp) AS last_message_time" +
            "FROM chats c" +
            "   INNER JOIN users u1 ON c.user1_id = u1.id" +
            "   INNER JOIN users u2 ON c.user2_id = u2.id" +
            "   LEFT  JOIN messages m ON c.id = m.chat_id" +
            "WHERE c.user1_id = :userId OR c.user2_id = :userId" +
            "GROUP BY c.id, u1.username, u2.username" +
            "UNION" +
            "SELECT NULL AS chat_id, cg.id AS chat_group_id, NULL AS user1, NULL AS user2, cg.name AS group_name," +
            "cg.description AS group_description, MAX(m.timestamp) AS last_message_time" +
            "FROM chat_groups cg" +
            "   INNER JOIN chat_group_participants cgp ON cg.id = cgp.chat_group_id" +
            "   LEFT  JOIN messages m ON cg.id = m.chat_group_id" +
            "WHERE cgp.user_id = :userId" +
            "GROUP BY cg.id, cg.name, cg.description" +
            "ORDER BY last_message_time DESC NULLS LAST;"
            , nativeQuery = true)
    List<Chat> findAllChatsAndChatGroupsByUserId(int userId);
}
