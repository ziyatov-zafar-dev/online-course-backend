package uz.codebyz.onlinecoursebackend.message.entity;

import java.util.List;

public enum FileType {

    IMAGE(
            "IMAGE",
            "Oddiy rasm fayli (jpg, png, webp va hokazo)",
            List.of(
                    "image/jpeg",
                    "image/png",
                    "image/webp",
                    "image/gif",
                    "image/bmp",
                    "image/svg+xml"
            )
    ),

    VIDEO(
            "VIDEO",
            "Oddiy video fayli (mp4, mkv va hokazo)",
            List.of(
                    "video/mp4",
                    "video/mpeg",
                    "video/quicktime",
                    "video/x-matroska",
                    "video/webm",
                    "video/3gpp"
            )
    ),

    FILE(
            "FILE",
            "Oddiy hujjat yoki boshqa fayl turi",
            List.of(
                    "application/pdf",
                    "application/msword",
                    "application/vnd.openxmlformats-officedocument.wordprocessingml.document",
                    "application/vnd.ms-excel",
                    "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet",
                    "application/zip",
                    "application/x-rar-compressed",
                    "text/plain"
            )
    );
    private String name;
    private final String description;
    private final List<String> mimeTypes;

    FileType(String name,String description, List<String> mimeTypes) {
        this.name = name;
        this.description = description;
        this.mimeTypes = mimeTypes;
    }

    public String getDescription() {
        return description;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<String> getMimeTypes() {
        return mimeTypes;
    }

    /**
     * MIME type bo‘yicha FileType aniqlash
     */
    public static FileType fromMimeType(String mimeType) {
        for (FileType type : values()) {
            if (type.getMimeTypes().contains(mimeType)) {
                return type;
            }
        }
        throw new IllegalArgumentException("Noma'lum MIME type: " + mimeType);
    }
}
