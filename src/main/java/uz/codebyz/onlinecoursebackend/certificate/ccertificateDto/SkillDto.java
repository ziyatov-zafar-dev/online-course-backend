package uz.codebyz.onlinecoursebackend.certificate.ccertificateDto;

public class SkillDto {
    private String name;
    private String iconUrl; // uploads/icons/java.png

    public SkillDto() {}

    public SkillDto(String name, String iconUrl) {
        this.name = name;
        this.iconUrl = iconUrl;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIconUrl() {
        return iconUrl;
    }

    public void setIconUrl(String iconUrl) {
        this.iconUrl = iconUrl;
    }
}
