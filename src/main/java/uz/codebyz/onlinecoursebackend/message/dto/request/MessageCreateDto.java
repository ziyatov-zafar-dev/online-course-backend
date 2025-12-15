package uz.codebyz.onlinecoursebackend.message.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import uz.codebyz.onlinecoursebackend.message.entity.MessageType;

import java.util.UUID;

public class MessageCreateDto {

    @NotBlank(message = "Xabar matni bo‘sh bo‘lishi mumkin emas")
    @Size(max = 5000, message = "Xabar uzunligi 5000 belgidan oshmasligi kerak")
    private String content;
    private UUID replyToMessageId;
    private MessageType messageType;

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public UUID getReplyToMessageId() {
        return replyToMessageId;
    }

    public void setReplyToMessageId(UUID replyToMessageId) {
        this.replyToMessageId = replyToMessageId;
    }

    public MessageType getMessageType() {
        return messageType;
    }

    public void setMessageType(MessageType messageType) {
        this.messageType = messageType;
    }
}
