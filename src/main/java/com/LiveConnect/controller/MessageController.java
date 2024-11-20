package com.LiveConnect.controller;

import com.LiveConnect.dto.MessagePaginationDTOImp;
import com.LiveConnect.dto.interfaces.MessagePaginationDTO;
import com.LiveConnect.model.Message;
import com.LiveConnect.model.MessageType;
import com.LiveConnect.service.ChatGroupService;
import com.LiveConnect.service.MessageService;
import com.LiveConnect.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.List;

@CrossOrigin(origins = "*")
@Controller
public class MessageController {

    @Autowired
    private MessageService messageService;

    @Autowired
    private ChatGroupService chatGroupService;

    @Autowired
    private UserService userService;

    @MessageMapping("/chat.sendMessage/{chatId}")
    @SendTo("/topic/public/{chatId}")
    public Message sendMessage(@Payload Message message, SimpMessageHeaderAccessor headerAccessor) {
        try {
            message.setTimestamp(LocalDateTime.now());
            messageService.createMessage(message);
            return message;
        } catch(RuntimeException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

    @MessageMapping("/chat.addUser/{chatId}")
    @SendTo("/topic/public/{chatId}")
    public Message addUser(@Payload Message message, SimpMessageHeaderAccessor headerAccessor) {

        headerAccessor.getSessionAttributes().put("username", userService
                                             .findUserById(message.getSenderUserId()).get().getUsername());
        headerAccessor.getSessionAttributes().put("type", MessageType.JOIN);
        return message;
    }

    @GetMapping("/message/content/{chatId}")
    public ResponseEntity<List<Message>> findMessageByContentAndChatId(@PathVariable int chatId, @RequestBody String content) {
        try {
            return ResponseEntity.ok(messageService.findAllByChatIdAndContent(chatId, content));
        } catch(RuntimeException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

    @PostMapping("/message/{userId}")
    public ResponseEntity<List<MessagePaginationDTO>> findAllMessagesByUserIdAndChatId(@PathVariable int userId, @RequestBody MessagePaginationDTOImp paginationDTO) {
        try {
            return ResponseEntity.ok(messageService.findAllMessagesByUserIdAndChatIdAndOffset(userId, paginationDTO));
        } catch(RuntimeException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

    @PutMapping("/message/{id}")
    public ResponseEntity<Message> update(@PathVariable int id, @RequestBody Message message) {
        try {
            return ResponseEntity.ok(messageService.update(id, message));
        } catch(RuntimeException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

    @DeleteMapping("/message/{id}")
    public ResponseEntity<Message> delete(@PathVariable int id) {
        try {
            if(messageService.deleteById(id)) {
                return ResponseEntity.ok().build();
            }
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found!");
        } catch(RuntimeException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

}
