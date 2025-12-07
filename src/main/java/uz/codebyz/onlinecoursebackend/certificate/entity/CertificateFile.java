package uz.codebyz.onlinecoursebackend.certificate.entity;

import jakarta.persistence.*;
import uz.codebyz.onlinecoursebackend.helper.CertificateGenerator;

import java.util.UUID;

@Entity
@Table(name = "certificate_files")
public class CertificateFile {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(columnDefinition = "uuid")
    private UUID id;

    private String fileName;
    private String fileUrl;

    // bayt ko‘rinishida
    private Long fileSize;
    @Enumerated(EnumType.STRING)
    private CertificateGenerator.CertificateType fileType;

    // agar string ko‘rinish kerak bo‘lsa (masalan: "2.5 MB")
    private String fileSizeMB;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "certificate_id")
    private Certificate certificate;
    // getter/setter

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getFileUrl() {
        return fileUrl;
    }

    public void setFileUrl(String fileUrl) {
        this.fileUrl = fileUrl;
    }

    public Long getFileSize() {
        return fileSize;
    }

    public void setFileSize(Long fileSize) {
        this.fileSize = fileSize;
    }

    public String getFileSizeMB() {
        return fileSizeMB;
    }

    public void setFileSizeMB(String fileSizeMB) {
        this.fileSizeMB = fileSizeMB;
    }

    public Certificate getCertificate() {
        return certificate;
    }

    public void setCertificate(Certificate certificate) {
        this.certificate = certificate;
    }

    public CertificateGenerator.CertificateType getFileType() {
        return fileType;
    }

    public void setFileType(CertificateGenerator.CertificateType fileType) {
        this.fileType = fileType;
    }
}
