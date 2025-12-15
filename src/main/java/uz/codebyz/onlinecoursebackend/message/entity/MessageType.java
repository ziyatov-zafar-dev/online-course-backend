package uz.codebyz.onlinecoursebackend.message.entity;

import java.util.List;

/**
 * Xabar turi va ruxsat etilgan MIME type lar
 * ❗ Hech qaysi MIME type 2 ta turga kirmaydi
 */
public enum MessageType {

    /* ================= TEXT ================= */

    TEXT(
            "TEXT",
            "Oddiy matnli xabar",
            List.of(
                    "text/plain",
                    "text/markdown",
                    "text/html",
                    "text/csv",
                    "text/rtf"
            )
    ),

    /* ================= IMAGE ================= */

    IMAGE(
            "IMAGE",
            "Rasm yuborilgan xabar",
            List.of(
                    "image/jpeg",
                    "image/jpg",
                    "image/png",
                    "image/gif",
                    "image/webp",
                    "image/bmp",
                    "image/tiff",
                    "image/svg+xml",
                    "image/x-icon",
                    "image/heic",
                    "image/heif",
                    "image/avif"
            )
    ),

    /* ================= VIDEO ================= */

    VIDEO(
            "VIDEO",
            "Video yuborilgan xabar",
            List.of(
                    "video/mp4",
                    "video/mpeg",
                    "video/ogg",
                    "video/webm",
                    "video/quicktime",   // mov
                    "video/x-msvideo",   // avi
                    "video/x-ms-wmv",
                    "video/x-flv",
                    "video/3gpp",
                    "video/3gpp2",
                    "video/x-matroska"   // mkv
            )
    ),

    /* ================= AUDIO ================= *//*

    AUDIO(
            "AUDIO",
            "Audio yoki voice xabar",
            List.of(
                    "audio/mpeg",        // mp3
                    "audio/wav",
                    "audio/ogg",
                    "audio/webm",
                    "audio/aac",
                    "audio/flac",
                    "audio/x-wav",
                    "audio/x-ms-wma",
                    "audio/mp4",
                    "audio/3gpp"
            )
    ),*/

    /* ================= FILE ================= */

    FILE(
            "FILE",
            "Hujjat yoki boshqa fayl",
            List.of(
                    /* ===== DOCUMENTS ===== */
                    "application/pdf",
                    "application/msword",
                    "application/vnd.ms-word",
                    "application/vnd.openxmlformats-officedocument.wordprocessingml.document",

                    "application/vnd.ms-excel",
                    "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet",

                    "application/vnd.ms-powerpoint",
                    "application/vnd.openxmlformats-officedocument.presentationml.presentation",

                    "application/rtf",
                    "application/vnd.oasis.opendocument.text",
                    "application/vnd.oasis.opendocument.spreadsheet",
                    "application/vnd.oasis.opendocument.presentation",

                    /* ===== ARCHIVES ===== */
                    "application/zip",
                    "application/x-zip-compressed",
                    "application/x-rar-compressed",
                    "application/x-7z-compressed",
                    "application/x-tar",
                    "application/gzip",
                    "application/x-bzip2",
                    "application/x-xz",

                    /* ===== DATA / CONFIG ===== */
                    "application/json",
                    "application/xml",
                    "application/yaml",
                    "application/x-yaml",
                    "application/sql",

                    /* ===== EXEC / BIN ===== */
                    "application/octet-stream",
                    "application/java-archive",
                    "application/x-msdownload",
                    "application/x-iso9660-image",

                    /* ===== FONTS ===== */
                    "font/ttf",
                    "font/otf",
                    "font/woff",
                    "font/woff2"
            )
    ),

    /* ================= SYSTEM ================= */

    SYSTEM(
            "SYSTEM",
            "Tizim tomonidan yaratilgan xabar",
            List.of()
    );

    private final String code;
    private final String description;
    private final List<String> allowedMimeTypes;

    MessageType(String code, String description, List<String> allowedMimeTypes) {
        this.code = code;
        this.description = description;
        this.allowedMimeTypes = allowedMimeTypes;
    }

    public String getCode() {
        return code;
    }

    public String getDescription() {
        return description;
    }

    public List<String> getAllowedMimeTypes() {
        return allowedMimeTypes;
    }

    /**
     * MIME type shu MessageType uchun ruxsat etilganmi
     */
    public boolean isAllowed(String mimeType) {
        return mimeType != null && allowedMimeTypes.contains(mimeType);
    }
}
