package uz.codebyz.onlinecoursebackend.message.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import uz.codebyz.onlinecoursebackend.message.entity.MessageType;

import java.util.UUID;

public class MessageFileCreateDto {

    /**
     * Fayl serverda allaqachon yuklangan joyi
     * (masalan: https://cdn.codebyz.online/chat/abc123.png)
     */
    @NotNull(message = "Fayl manzili majburiy")
    private String fileUrl;

    /**
     * Fayl turi
     * IMAGE | VIDEO | FILE
     */
    @NotNull(message = "Xabar turi majburiy")
    private MessageType messageType;

    /**
     * Caption (ixtiyoriy)
     */
    @Size(max = 2000, message = "Caption 2000 belgidan oshmasligi kerak")
    private String caption;

    /**
     * Reply qilinayotgan xabar ID (ixtiyoriy)
     */
    private UUID replyToMessageId;

    /**
     * Faylning original nomi
     */
    private String fileName;

    /**
     * Fayl hajmi (byte)
     */
    private Long fileSize;

    public MessageFileCreateDto() {
    }

    /* ================= GETTERS / SETTERS ================= */

    public String getFileUrl() {
        return fileUrl;
    }

    public void setFileUrl(String fileUrl) {
        this.fileUrl = fileUrl;
    }

    public MessageType getMessageType() {
        return messageType;
    }

    public void setMessageType(MessageType messageType) {
        this.messageType = messageType;
    }

    public String getCaption() {
        return caption;
    }

    public void setCaption(String caption) {
        this.caption = caption;
    }

    public UUID getReplyToMessageId() {
        return replyToMessageId;
    }

    public void setReplyToMessageId(UUID replyToMessageId) {
        this.replyToMessageId = replyToMessageId;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public Long getFileSize() {
        return fileSize;
    }

    public void setFileSize(Long fileSize) {
        this.fileSize = fileSize;
    }
}
