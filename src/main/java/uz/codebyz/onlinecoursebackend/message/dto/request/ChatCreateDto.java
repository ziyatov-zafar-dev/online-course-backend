package uz.codebyz.onlinecoursebackend.message.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import uz.codebyz.onlinecoursebackend.message.entity.ChatType;

import java.util.List;
import java.util.UUID;

public class ChatCreateDto {

    /**
     * PRIVATE yoki GROUP
     */
    @NotNull
    private ChatType type;

    /**
     * GROUP uchun majburiy
     */
    private String title;

    /**
     * Chatga qo‘shiladigan userlar
     * PRIVATE bo‘lsa → 1 ta user bo‘lishi kerak
     */
    @NotNull
    private List<UUID> memberIds;

    public ChatCreateDto() {
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

    public List<UUID> getMemberIds() {
        return memberIds;
    }

    public void setMemberIds(List<UUID> memberIds) {
        this.memberIds = memberIds;
    }
}
