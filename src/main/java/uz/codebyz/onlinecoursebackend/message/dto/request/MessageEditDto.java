package uz.codebyz.onlinecoursebackend.message.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class MessageEditDto {

    /**
     * Tahrirlangan xabar matni
     * (faqat TEXT message uchun)
     */
    @NotBlank(message = "Xabar matni bo‘sh bo‘lishi mumkin emas")
    @Size(max = 5000, message = "Xabar uzunligi 5000 belgidan oshmasligi kerak")
    private String content;

    public MessageEditDto() {
    }

    /* ================= GETTERS / SETTERS ================= */

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
