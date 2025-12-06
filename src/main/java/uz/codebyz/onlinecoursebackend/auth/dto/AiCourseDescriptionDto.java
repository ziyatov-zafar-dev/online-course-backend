package uz.codebyz.onlinecoursebackend.auth.dto;
public class AiCourseDescriptionDto {
    private String title;
    private String desc;

    public AiCourseDescriptionDto(String title, String desc) {
        this.title = title;
        this.desc = desc;
    }

    public String getTitle() { return title; }
    public String getDesc() { return desc; }
}
