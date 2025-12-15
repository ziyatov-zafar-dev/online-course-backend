package uz.codebyz.onlinecoursebackend.message.entity;

public enum ChatStatus {

    ACTIVE(
            "Chat hozirda faol holatda ishlamoqda",
            "Chat is currently active and available"
    ),

    DELETED(
            "Chat tizimdan o‘chirilgan va yashirilgan",
            "Chat has been deleted and hidden from system"
    ),

    BLOCKED(
            "Chat xavfsizlik sababli vaqtincha bloklangan",
            "Chat has been temporarily blocked for security reasons"
    );

    private final String descUz;
    private final String descEn;

    ChatStatus(String descUz, String descEn) {
        this.descUz = descUz;
        this.descEn = descEn;
    }

    public String getDescUz() {
        return descUz;
    }

    public String getDescEn() {
        return descEn;
    }
}
