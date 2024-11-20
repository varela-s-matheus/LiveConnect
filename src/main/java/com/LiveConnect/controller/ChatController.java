package com.LiveConnect.controller;

import com.LiveConnect.model.ChatGroup;
import com.LiveConnect.model.ChatGroupParticipant;
import com.LiveConnect.service.ChatGroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;
//import org.springframework.web.bind.annotation.RestController;

//@SecurityRequirement(name = "bearer-key")
@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/chat")
public class ChatController {

    @Autowired
    private ChatGroupService chatGroupService;

    @PostMapping("/createChat/{userId}")
    public ResponseEntity<ChatGroup> add(@RequestBody ChatGroup chatGroup, @PathVariable int userId) {
        try {
            return ResponseEntity.ok(chatGroupService.createChatGroup(userId, chatGroup));
        } catch(RuntimeException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

    @GetMapping("/{userId}")
    public ResponseEntity<List<ChatGroup>> findAllChatsByUserId(@PathVariable int userId) {
        try {
            return ResponseEntity.ok(chatGroupService.findAllChatGroupByUserId(userId));
        } catch(RuntimeException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

    @PostMapping("/search/{userId}")
    public ResponseEntity<List<ChatGroup>> findAllChatsByName(@PathVariable int userId, @RequestBody ChatGroup chatGroup) {
        try {
            return ResponseEntity.ok(chatGroupService.findAllChatsByName("%" + chatGroup.getName() + "%", userId));
        } catch(RuntimeException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

    @PostMapping("/subscribe/{userId}")
    public ResponseEntity<Optional<ChatGroup>> createSubscribe(@PathVariable int userId, @RequestBody ChatGroup chatGroup) {
        try {
            return ResponseEntity.ok(chatGroupService.createSubscribe(new ChatGroupParticipant(chatGroup.getId(), userId)));
        } catch(RuntimeException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

}

// Funcionando...
//    @Autowired
//    private MessageService messageService;
//
//    @Autowired
//    private UserService userService;
//
//    @MessageMapping("/chat.sendMessage")
//    @SendTo("/topic/public")
//    public Message sendMessage(@Payload Message message) {
//        try {
//            message.setTimestamp(LocalDateTime.now());
//            messageService.sendMessage(message.getReceiverUserId(), message);
//            return message;
//        } catch(RuntimeException e) {
//            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
//        }
//    }
//
//    @MessageMapping("/chat.addUser")
//    @SendTo("/topic/public")
//    public Message addUser(
//            @Payload Message message,
//            SimpMessageHeaderAccessor headerAccessor
//    ) {
//        headerAccessor.getSessionAttributes().put("username", userService.findUserById(message.getSenderUserId()).get().getUsername());
//        return message;
//    }


//    @MessageMapping("/sendGroupMessage")
//    @SendTo("/topic/group/{chatGroupId}")
//    public Message sendGroupMessage(@Payload Message message) {
//        try {
//            message.setTimestamp(LocalDateTime.now());
//
//            if (message.getChatGroupId() == 0) {
//                throw new RuntimeException("Grupo não encontrado");
//            }
//
//            messageService.sendGroupMessage(message);
//            return message;
//        } catch (RuntimeException e) {
//            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
//        }
//    }
