package com.LiveConnect.repository;

import com.LiveConnect.model.ChatGroupParticipant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ChatGroupParticipantRepository extends JpaRepository<ChatGroupParticipant, Integer> {

}
