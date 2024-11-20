package com.LiveConnect.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MessagePaginationDTOImp {

    private int id;

    private int chatGroupId;

    private int senderUserId;

    private String content;

    private String username;

    private LocalDateTime timestamp;

    private int offset = 0;

    public MessagePaginationDTOImp(int chatGroupId, String content, String username, LocalDateTime timestamp, int offset) {
        this.chatGroupId = chatGroupId;
        this.content = content;
        this.username = username;
        this.timestamp = timestamp;
        this.offset = offset;
    }

}
