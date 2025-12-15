package uz.codebyz.onlinecoursebackend.message.dto.response;


import uz.codebyz.onlinecoursebackend.message.entity.MessageType;

import java.time.LocalDateTime;
import java.util.UUID;

public class MessagePreviewDto {

    private UUID id;
    private String content;
    private MessageType messageType;
    private LocalDateTime createdAt;
    private Boolean isRead;

    public MessagePreviewDto() {
    }

    public MessagePreviewDto(
            UUID id,
            String content,
            MessageType messageType,
            LocalDateTime createdAt,
            Boolean isRead
    ) {
        this.id = id;
        this.content = content;
        this.messageType = messageType;
        this.createdAt = createdAt;
        this.isRead = isRead;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public MessageType getMessageType() {
        return messageType;
    }

    public void setMessageType(MessageType messageType) {
        this.messageType = messageType;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public Boolean getIsRead() {
        return isRead;
    }

    public void setIsRead(Boolean isRead) {
        this.isRead = isRead;
    }
}
