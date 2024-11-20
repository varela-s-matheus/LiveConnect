package com.LiveConnect.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Entity
@NoArgsConstructor
@Table(name = "chat_group_participants")
public class ChatGroupParticipant {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private int chatGroupId;

    private int userId;

    public ChatGroupParticipant(int chatGroupId, int userId) {
        this.chatGroupId = chatGroupId;
        this.userId = userId;
    }
}
