package com.LiveConnect.service;

import com.LiveConnect.model.ChatGroup;
import com.LiveConnect.model.ChatGroupParticipant;
import com.LiveConnect.repository.ChatGroupParticipantRepository;
import com.LiveConnect.repository.ChatGroupRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ChatGroupService {

    @Autowired
    private ChatGroupRepository chatGroupRepository;

    @Autowired
    private ChatGroupParticipantRepository chatGroupParticipantRepository;


    public List<ChatGroup> findAllChatGroupByUserId(int userId) {
        return chatGroupRepository.findAllChatGroupByUserId(userId);
    }

    public List<ChatGroup> findByIdAndName(int userId, String name) {
        return chatGroupRepository.findAllByIdAndName(userId, name);
    }


    public Optional<ChatGroup> findChatGroupById(Integer id) {
        Optional<ChatGroup> chatGroup = chatGroupRepository.findById(id);

        if (chatGroup.isPresent()) {
            return chatGroup;
        }
        return null;
    }


    public Optional<ChatGroup> findChatGroupByChatGroupId(int chatGroupId) {
        return chatGroupRepository.findById(chatGroupId);
    }

    public ChatGroup createChatGroup(int userId, ChatGroup chatGroup) {
        ChatGroup group = chatGroupRepository.saveAndFlush(chatGroup);
        ChatGroupParticipant groupParticipant = new ChatGroupParticipant(group.getId(),userId);
        chatGroupParticipantRepository.saveAndFlush(groupParticipant);
        return group;
    }


    public ChatGroup update(Integer id, ChatGroup chatGroup) {
        if (!chatGroupRepository.existsById(id)) {
            return null;
        }
        chatGroup.setId(id);

        return chatGroupRepository.save(chatGroup);
    }


    public boolean deleteById(int id) {
        if (!chatGroupRepository.existsById(id)) {
            return false;
        }
        chatGroupRepository.deleteById(id);
        return true;
    }

    public List<ChatGroup> findAllChatsByName(String chatGroupName, int userId) {
        return chatGroupRepository.findAllChatGroupByNameLike(chatGroupName, userId);
    }

    public Optional<ChatGroup> createSubscribe(ChatGroupParticipant participant) {
        if(chatGroupRepository.existsById(participant.getChatGroupId())) {
            chatGroupParticipantRepository.saveAndFlush(participant);
            return chatGroupRepository.findById(participant.getChatGroupId());
        }
        return Optional.empty();
    }
}
