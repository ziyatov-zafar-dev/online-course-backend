package uz.codebyz.onlinecoursebackend.message.entity;

/**
 * Chat turi
 */
public enum ChatType {

    PRIVATE(
            "PRIVATE",
            "Ikki foydalanuvchi o‘rtasidagi shaxsiy chat. " +
                    "Faqat 2 ta participant bo‘ladi, title majburiy emas."
    ),

    GROUP(
            "GROUP",
            "Bir nechta foydalanuvchilar qatnashadigan guruh chat. " +
                    "Title majburiy, description mavjud bo‘lishi mumkin."
    );

    private final String code;
    private final String description;

    ChatType(String code, String description) {
        this.code = code;
        this.description = description;
    }

    /**
     * Enum kodi (DB da saqlanadigan qiymat)
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
