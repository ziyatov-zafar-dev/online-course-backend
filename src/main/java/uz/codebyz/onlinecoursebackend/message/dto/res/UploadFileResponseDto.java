package uz.codebyz.onlinecoursebackend.message.dto.res;

import uz.codebyz.onlinecoursebackend.message.entity.FileType;

public class UploadFileResponseDto {
    private String imgUrl;
    private Long imgSize;
    private String imgSizeMB;
    private String imgName;
    private FileType fileType;


    public UploadFileResponseDto(String imgUrl, Long imgSize, String imgSizeMB, String imgName, FileType fileType) {
        this.imgUrl = imgUrl;
        this.imgSize = imgSize;
        this.imgSizeMB = imgSizeMB;
        this.imgName = imgName;
        this.fileType = fileType;
    }

    public UploadFileResponseDto() {
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public Long getImgSize() {
        return imgSize;
    }

    public void setImgSize(Long imgSize) {
        this.imgSize = imgSize;
    }

    public String getImgSizeMB() {
        return imgSizeMB;
    }

    public void setImgSizeMB(String imgSizeMB) {
        this.imgSizeMB = imgSizeMB;
    }

    public String getImgName() {
        return imgName;
    }

    public void setImgName(String imgName) {
        this.imgName = imgName;
    }

    public FileType getFileType() {
        return fileType;
    }

    public void setFileType(FileType fileType) {
        this.fileType = fileType;
    }
}
