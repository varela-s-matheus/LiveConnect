package com.LiveConnect.service;

import com.LiveConnect.model.Chat;
import com.LiveConnect.repository.ChatRepository;
import com.LiveConnect.repository.MessageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ChatService {

    @Autowired
    private ChatRepository chatRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private MessageRepository messageRepository;

    // Ok
    public List<Chat> findAllByUserId(int userId) {
        return chatRepository.findAllByUserId1OrUserId2(userId, userId);
    }

//    public List<Chat> findByUserIdAndName(int userId, String name) {
//        return chatRepository.findByUserIdAndName(userId, name);
//    }

    // Ok
    public Optional<Chat> findChatById(Integer id) {
        Optional<Chat> chat = chatRepository.findById(id);

        if (chat.isPresent()) {
            return chat;
        }
        return null;
    }

    // Ok
    public Optional<Chat> findChatById(int chatId) {
        return chatRepository.findById(chatId);
    }

    public Chat createChat(Chat chat) {
        return chatRepository.saveAndFlush(chat);
    }

    // Ok
    public Chat update(Integer id, Chat chat) {
        if (!chatRepository.existsById(id)) {
            return null;
        }
        chat.setId(id);

        return chatRepository.save(chat);
    }

    // OK
    public boolean deleteById(int id) {
        if (!chatRepository.existsById(id)) {
            return false;
        }
        chatRepository.deleteById(id);
        return true;
    }

}
