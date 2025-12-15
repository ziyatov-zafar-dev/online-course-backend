package uz.codebyz.onlinecoursebackend.message.dto.response;

import uz.codebyz.onlinecoursebackend.message.entity.ChatMember;
import uz.codebyz.onlinecoursebackend.message.entity.ChatMemberRole;

import java.util.UUID;

public class ChatMemberResponseDto {

    private UUID userId;
    private String username;
    private String fullName;

    public ChatMemberResponseDto() {
    }

    public ChatMemberResponseDto(UUID userId, String username, String fullName) {
        this.userId = userId;
        this.username = username;
        this.fullName = fullName;

    }

    public ChatMemberResponseDto(ChatMember chatMember) {

    }

    public UUID getUserId() {
        return userId;
    }

    public void setUserId(UUID userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    private ChatMemberRole role;
    private String joinedAt;

    public ChatMemberRole getRole() {
        return role;
    }

    public void setRole(ChatMemberRole role) {
        this.role = role;
    }

    public String getJoinedAt() {
        return joinedAt;
    }

    public void setJoinedAt(String joinedAt) {
        this.joinedAt = joinedAt;
    }
}
