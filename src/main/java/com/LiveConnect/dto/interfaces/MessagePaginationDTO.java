package com.LiveConnect.dto.interfaces;


import java.time.LocalDateTime;

public interface MessagePaginationDTO {

    int getId();
    int getChatGroupId();
    int getSenderUserId();
    String getContent();
    String getUsername();
    LocalDateTime getTimestamp();
    int getOffset();
}