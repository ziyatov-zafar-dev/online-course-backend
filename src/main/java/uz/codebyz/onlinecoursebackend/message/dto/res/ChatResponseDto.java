package uz.codebyz.onlinecoursebackend.message.dto.res;

import java.util.List;
import java.util.UUID;

public class ChatResponseDto {
    private UUID chatId;
    private UUID user1Id;
    private UUID user2Id;

    public List<MessageResponseDto> getMessages() {
        return messages;
    }

    public void setMessages(List<MessageResponseDto> messages) {
        this.messages = messages;
    }

    public UUID getUser2Id() {
        return user2Id;
    }

    public void setUser2Id(UUID user2Id) {
        this.user2Id = user2Id;
    }

    public UUID getUser1Id() {
        return user1Id;
    }

    public void setUser1Id(UUID user1Id) {
        this.user1Id = user1Id;
    }

    public UUID getChatId() {
        return chatId;
    }

    public void setChatId(UUID chatId) {
        this.chatId = chatId;
    }

    private List<MessageResponseDto> messages;

}
