package uz.codebyz.onlinecoursebackend.homeworkfile.entity;

import jakarta.persistence.*;
import uz.codebyz.onlinecoursebackend.homework.entity.Homework;
import uz.codebyz.onlinecoursebackend.module.entity.Module;

import java.util.UUID;

@Entity
@Table(name = "homeworkfiles")
public class HomeworkFile {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(columnDefinition = "uuid")
    private UUID id;
    private String ext;
    private String fileName;
    @Column(columnDefinition = "TEXT")
    private String fileUrl;
    private Long fileSize;
    private Boolean active;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "homework_id")
    private Homework homework;


    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getExt() {
        return ext;
    }

    public void setExt(String ext) {
        this.ext = ext;
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

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public Homework getHomework() {
        return homework;
    }

    public void setHomework(Homework homework) {
        this.homework = homework;
    }
}
