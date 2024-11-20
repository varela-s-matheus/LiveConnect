package com.LiveConnect.service;

import com.LiveConnect.dto.MessagePaginationDTOImp;
import com.LiveConnect.dto.interfaces.MessagePaginationDTO;
import com.LiveConnect.model.Message;
import com.LiveConnect.repository.ChatGroupRepository;
import com.LiveConnect.repository.MessageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class MessageService {

    @Autowired
    private MessageRepository messageRepository;

    @Autowired
    private ChatGroupRepository chatGroupRepository;

    @Autowired
    private ChatService chatService;

    @Autowired
    private UserService userService;

    // Ok
    public List<Message> findAllByChatIdAndContent(int chatId, String content) {
        return messageRepository.findAllByChatGroupIdAndContentIgnoreCase(chatId, content);
    }

    // Ok
    public Optional<Message> findMessageById(Integer id) {
        Optional<Message> message = messageRepository.findById(id);

        return message;
    }

    // Ok
    public List<MessagePaginationDTO> findAllMessagesByUserIdAndChatIdAndOffset(int userId, MessagePaginationDTOImp paginationDTO) {
        return messageRepository.findAllMessagesByUserIdAndChatIdAndOffset(userId, paginationDTO.getChatGroupId(), paginationDTO.getOffset());
    }

    // Ok
    public Message createMessage(Message message) {
        return messageRepository.saveAndFlush(message);
    }

    // Ok
    public Message update(Integer id, Message message) {
        if (!messageRepository.existsById(id)) {
            return null;
        }
        message.setId(id);

        return messageRepository.save(message);
    }

    // OK
    public boolean deleteById(int id) {
        if (!messageRepository.existsById(id)) {
            return false;
        }
        messageRepository.deleteById(id);
        return true;
    }
}
