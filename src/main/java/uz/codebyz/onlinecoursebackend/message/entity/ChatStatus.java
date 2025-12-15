package uz.codebyz.onlinecoursebackend.message.entity;

/**
 * Chat holati
 */
public enum ChatStatus {

    ACTIVE(
            "ACTIVE",
            "Chat faol holatda. Xabar yuborish va qabul qilish mumkin."
    ),

    BLOCKED(
            "BLOCKED",
            "Chat vaqtincha bloklangan. Xabar yuborish taqiqlanadi, " +
                    "lekin ma’lumotlar saqlanib qoladi."
    ),

    DELETED(
            "DELETED",
            "Chat o‘chirilgan holatda. Foydalanuvchilarga ko‘rinmaydi, " +
                    "lekin DB da soft-delete sifatida saqlanadi."
    );

    private final String code;
    private final String description;

    ChatStatus(String code, String description) {
        this.code = code;
        this.description = description;
    }

    /**
     * Enum kodi
     */
    public String getCode() {
        return code;
    }

    /**
     * To‘liq tavsif
     */
    public String getDescription() {
        return description;
    }
}
