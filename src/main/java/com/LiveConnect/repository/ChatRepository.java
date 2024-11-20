package com.LiveConnect.repository;

import com.LiveConnect.model.Chat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChatRepository extends JpaRepository<Chat, Integer> {

    List<Chat> findAllByUserId1OrUserId2(int userId1, int userId2);

//    List<Chat> findAllByUserId1OrUserId2AndLike(int userId, String name);
}
