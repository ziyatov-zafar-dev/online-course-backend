package uz.codebyz.onlinecoursebackend.certificate.ccertificateDto;

public class CertificateResponseDto {
    private String fileName;
    private String filePath;
    private String url;

    public CertificateResponseDto() {}

    public CertificateResponseDto(String fileName, String filePath, String url) {
        this.fileName = fileName;
        this.filePath = filePath;
        this.url = url;
    }

    public String getFileName() {
        return fileName;
    }

    public String getFilePath() {
        return filePath;
    }

    public String getUrl() {
        return url;
    }
}
