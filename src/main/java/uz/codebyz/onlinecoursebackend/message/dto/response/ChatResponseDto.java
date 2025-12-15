package uz.codebyz.onlinecoursebackend.message.dto.response;


import uz.codebyz.onlinecoursebackend.message.entity.Chat;
import uz.codebyz.onlinecoursebackend.message.entity.ChatType;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public class ChatResponseDto {

    private UUID id;
    private ChatType type;

    /**
     * GROUP bo‘lsa — nomi bo‘ladi
     * PRIVATE bo‘lsa — null
     */
    private String title;

    /**
     * Chat a’zolari
     */
    private List<ChatMemberResponseDto> members;

    /**
     * Oxirgi xabar (preview)
     */
    private MessagePreviewDto lastMessage;

    private LocalDateTime createdAt;

    public ChatResponseDto() {
    }

    public ChatResponseDto(Chat chat) {
        this.id = chat.getId();
        this.type = chat.getType();
        this.title = chat.getTitle();
        this.createdAt = chat.getCreatedAt();
    }

    /* ================= GETTERS / SETTERS ================= */

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public ChatType getType() {
        return type;
    }

    public void setType(ChatType type) {
        this.type = type;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<ChatMemberResponseDto> getMembers() {
        return members;
    }

    public void setMembers(List<ChatMemberResponseDto> members) {
        this.members = members;
    }

    public MessagePreviewDto getLastMessage() {
        return lastMessage;
    }

    public void setLastMessage(MessagePreviewDto lastMessage) {
        this.lastMessage = lastMessage;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
