package uz.codebyz.onlinecoursebackend.message.entity;

import jakarta.persistence.*;

import java.util.UUID;

@Entity
@Table(name = "message_files")
public class MessageFile {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(columnDefinition = "uuid")
    private UUID id;

    /**
     * Qaysi message ga tegishli
     */
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "message_id", nullable = false)
    private Message message;

    /**
     * Fayl saqlangan URL yoki path
     * (S3, CDN, local storage)
     */
    @Column(name = "file_url", nullable = false, columnDefinition = "TEXT")
    private String fileUrl;
    @Column(name = "fileName", nullable = false, columnDefinition = "TEXT")
    private String fileName;



    /**
     * IMAGE | VIDEO | FILE | AUDIO
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "file_type", length = 20)
    private MessageFileType fileType;

    /**
     * Fayl hajmi (byte)
     */
    @Column(name = "file_size")
    private Long fileSize;

    /* ==========================
       GETTERS & SETTERS
       ========================== */

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public Message getMessage() {
        return message;
    }

    public void setMessage(Message message) {
        this.message = message;
    }

    public String getFileUrl() {
        return fileUrl;
    }

    public void setFileUrl(String fileUrl) {
        this.fileUrl = fileUrl;
    }

    public MessageFileType getFileType() {
        return fileType;
    }

    public void setFileType(MessageFileType fileType) {
        this.fileType = fileType;
    }

    public Long getFileSize() {
        return fileSize;
    }

    public void setFileSize(Long fileSize) {
        this.fileSize = fileSize;
    }
    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }}
