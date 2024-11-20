package com.LiveConnect.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Entity
@NoArgsConstructor
@Table(name = "chats")
public class Chat {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private int groupId;

    private int userId1;

    private int userId2;

    public Chat(int userId1, int userId2) {
        this.userId1 = userId1;
        this.userId2 = userId2;
    }
}
