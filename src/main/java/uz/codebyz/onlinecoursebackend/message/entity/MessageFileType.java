package uz.codebyz.onlinecoursebackend.message.entity;

/**
 * Message ga biriktirilgan fayl turi
 */
public enum MessageFileType {

    IMAGE(
            "IMAGE",
            "Rasm fayl"
    ),

    VIDEO(
            "VIDEO",
            "Video fayl"
    ),

    FILE(
            "FILE",
            "Hujjat yoki boshqa fayl"
    );

    private final String code;
    private final String description;

    MessageFileType(String code, String description) {
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
