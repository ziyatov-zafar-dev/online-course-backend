package uz.codebyz.onlinecoursebackend.message.entity;

/**
 * Chat ichidagi foydalanuvchi roli
 */
public enum ChatMemberRole {

    OWNER(
            "OWNER",
            "Chat egasi. Barcha huquqlarga ega, chatni o‘chira oladi."
    ),

    ADMIN(
            "ADMIN",
            "Chat administratori. Foydalanuvchilarni qo‘shish/o‘chirish va " +
                    "xabarlarni boshqarish huquqiga ega."
    ),

    MEMBER(
            "MEMBER",
            "Oddiy chat a’zosi. Faqat xabar yuborish va o‘qish huquqiga ega."
    );

    private final String code;
    private final String description;

    ChatMemberRole(String code, String description) {
        this.code = code;
        this.description = description;
    }

    public String getCode() {
        return code;
    }

    public String getDescription() {
        return description;
    }
}
