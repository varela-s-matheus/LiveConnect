package com.LiveConnect.repository;

import com.LiveConnect.model.ChatGroup;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChatGroupRepository extends JpaRepository<ChatGroup, Integer> {

    @Query(value = "select chat_groups.* from chat_group_participants participants " +
                    "inner join chat_groups on chat_groups.id = participants.chat_group_id " +
                    "where participants.user_id = :userId", nativeQuery = true)
    List<ChatGroup> findAllChatGroupByUserId(int userId);


    @Query(value = "select chat_groups.* from chat_group_participants participants " +
            "inner join chat_groups on chat_groups.id = participants.chat_group_id " +
            "where participants.user_id = :userId " +
            "and chat_groups.\"name\" like %:name%", nativeQuery = true)
    List<ChatGroup> findAllByIdAndName(int userId, String name);

    @Query(value =
            "select distinct chat_groups.* " +
            "from chat_groups " +
            "left join chat_group_participants participants " +
            "       on chat_groups.id = participants.chat_group_id " +
            "       and participants.user_id = :userId " +
            "where chat_groups.\"name\" like %:chatGroupName% " +
            "and participants.user_id is null "
            , nativeQuery = true)
    List<ChatGroup> findAllChatGroupByNameLike(String chatGroupName, int userId);

}
