package com.LiveConnect.repository;

import com.LiveConnect.dto.interfaces.MessagePaginationDTO;
import com.LiveConnect.model.Message;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MessageRepository extends JpaRepository<Message, Integer> {

    List<Message> findByContent(String content);

    @Query(value = """
                   select * from (
                   		SELECT me.id,\s
                   		       me.content,\s
                   		       me.sender_user_id,\s
                   		       me.chat_group_id,\s
                   		       users.username,\s
                   		       me.timestamp,
                   		       :offset + 25 as offset
                   		FROM messages me
                   		INNER JOIN chat_group_participants part\s
                   		    ON part.chat_group_id = me.chat_group_id
                   		INNER JOIN users\s
                   		    ON users.id = me.sender_user_id
                   		WHERE part.user_id = :userId\s
                   		  AND me.chat_group_id = :chatId
                   		ORDER BY me.timestamp DESC
                   		LIMIT 25 OFFSET :offset
                   	) as mes\s
                   	order by mes.timestamp ASC
                """, nativeQuery = true)
    List<MessagePaginationDTO> findAllMessagesByUserIdAndChatIdAndOffset(
            @Param("userId") int userId, @Param("chatId") int chatId, @Param("offset") int offset);

    List<Message> findAllByChatGroupIdAndContentIgnoreCase(int chatId, String content);
}
