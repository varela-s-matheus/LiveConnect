package com.LiveConnect.controller;

import com.LiveConnect.model.ChatGroup;
import com.LiveConnect.service.ChatGroupService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/chatGroup")
public class ChatGroupController {

    private ChatGroupService chatGroupService;

    @GetMapping("/{id}")
    public ResponseEntity<List<ChatGroup>> findChatGroupByUserId(@PathVariable int id) {
        try {
            return ResponseEntity.ok(chatGroupService.findAllChatGroupByUserId(id));
        } catch(RuntimeException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

    @GetMapping("/name/{id}")
    public ResponseEntity<List<ChatGroup>> findChatGroupByUserId(@PathVariable int id, @RequestBody String groupName) {
        try {
            return ResponseEntity.ok(chatGroupService.findByIdAndName(id, groupName));
        } catch(RuntimeException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<ChatGroup> update(@PathVariable int id, @RequestBody ChatGroup chatGroup) {
        try {
            return ResponseEntity.ok(chatGroupService.update(id, chatGroup));
        } catch(RuntimeException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ChatGroup> delete(@PathVariable int id) {
        try {
            if(chatGroupService.deleteById(id)) {
                return ResponseEntity.ok().build();
            }
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "ChatGroup not found!");
        } catch(RuntimeException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }
}
