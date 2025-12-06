package uz.codebyz.onlinecoursebackend.admin.homework.dto;

import uz.codebyz.onlinecoursebackend.homeworkfile.entity.HomeworkFile;

import java.util.UUID;

public class AdminHomeworkFileResponseDto {
    private UUID id;
    private String fileName;
    private String fileUrl;
    private Long fileSize;
    private String fileSizeMB;

    public String getFileSizeMB() {
        return fileSizeMB;
    }

    public void setFileSizeMB(String fileSizeMB) {
        this.fileSizeMB = fileSizeMB;
    }

    public AdminHomeworkFileResponseDto(UUID id, String fileName, String fileUrl, Long fileSize, String fileSizeMB) {
        this.id = id;
        this.fileName = fileName;
        this.fileUrl = fileUrl;
        this.fileSize = fileSize;
        this.fileSizeMB = fileSizeMB;
    }

    public AdminHomeworkFileResponseDto() {
    }

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
}
