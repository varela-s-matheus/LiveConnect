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

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/chat")
public class ChatGroupController {

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
