package uz.codebyz.onlinecoursebackend.message.dto.req;

import java.util.UUID;

public class CreateMessageRequestDto {
    private UUID replyMessageId;
    private UUID chatId;
    private UUID senderId;
    private String caption;
    private boolean hasFile;
    private String imgUrl;
    private Long imgSize;
    private Long imgSizeMB;
    private String imgName;

    public UUID getReplyMessageId() {
        return replyMessageId;
    }

    public void setReplyMessageId(UUID replyMessageId) {
        this.replyMessageId = replyMessageId;
    }

    public UUID getChatId() {
        return chatId;
    }

    public void setChatId(UUID chatId) {
        this.chatId = chatId;
    }

    public UUID getSenderId() {
        return senderId;
    }

    public void setSenderId(UUID senderId) {
        this.senderId = senderId;
    }

    public String getCaption() {
        return caption;
    }

    public void setCaption(String caption) {
        this.caption = caption;
    }

    public boolean isHasFile() {
        return hasFile;
    }

    public void setHasFile(boolean hasFile) {
        this.hasFile = hasFile;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public Long getImgSize() {
        return imgSize;
    }

    public void setImgSize(Long imgSize) {
        this.imgSize = imgSize;
    }

    public Long getImgSizeMB() {
        return imgSizeMB;
    }

    public void setImgSizeMB(Long imgSizeMB) {
        this.imgSizeMB = imgSizeMB;
    }

    public String getImgName() {
        return imgName;
    }

    public void setImgName(String imgName) {
        this.imgName = imgName;
    }
}
