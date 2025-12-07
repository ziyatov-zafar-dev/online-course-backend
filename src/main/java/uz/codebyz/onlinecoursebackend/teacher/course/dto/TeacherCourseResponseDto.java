package uz.codebyz.onlinecoursebackend.teacher.course.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import uz.codebyz.onlinecoursebackend.admin.course.dto.AdminCourseResponseDto;
import uz.codebyz.onlinecoursebackend.admin.course.dto.AdminCourseSkillResponseDto;
import uz.codebyz.onlinecoursebackend.admin.module.dto.AdminModuleResponseDto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class TeacherCourseResponseDto {
    @Schema(description = "Kurs id")
    private UUID courseId;
    @Schema(description = "Kurs nomi")
    private String name;
    @Schema(description = "Kurs tavsifi")
    private String description;
    @Schema(description = "Kurs slug")
    private String slug;
    @Schema(description = "tartib raqami")
    private Integer orderNumber;
    @Schema(description = "telegram guruh silkasi")
    private String telegramGroupLink;
    @Schema(description = "telegram kanal silkasi")
    private String telegramChannelLink;
    @Schema(description = "telegram guruhi bo'lsa true aks holda false")
    private Boolean hasTelegramGroup;
    @Schema(description = "telegram kanali bo'lsa true akh holda false")
    private Boolean hasTelegramChannel;
    @Schema(description = "statusi")
    private Map<String, String> status;
    @Schema(description = "Chegirma qo'llangandan keyingi narx")

    private BigDecimal discountPrice;
    @Schema(description = "Chegirma foizi")

    private Integer discountPercent;
    @Schema(description = "Chegirma boshlanish vaqti")

    private LocalDateTime discountStartAt;
    @Schema(description = "Chegirma tugash vaqti")

    private LocalDateTime discountEndAt;
    private String imgName;
    private String imgUrl;
    private Long imgSize;
    private String imgSizeMB;
    private String promoCourseVideoUrl;
    private String promoCourseVideoFileName;
    private Long promoCourseVideoFileSize;
    private String promoCourseVideoFileSizeMB;
    private BigDecimal price;
    private BigDecimal finalPrice;
    private List<AdminModuleResponseDto> modules;
    private List<AdminCourseSkillResponseDto> skills;

    public String getImgSizeMB() {
        return imgSizeMB;
    }

    public void setImgSizeMB(String imgSizeMB) {
        this.imgSizeMB = imgSizeMB;
    }

    public String getPromoCourseVideoFileSizeMB() {
        return promoCourseVideoFileSizeMB;
    }

    public void setPromoCourseVideoFileSizeMB(String promoCourseVideoFileSizeMB) {
        this.promoCourseVideoFileSizeMB = promoCourseVideoFileSizeMB;
    }

    public UUID getCourseId() {
        return courseId;
    }

    public void setCourseId(UUID courseId) {
        this.courseId = courseId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }


    public String getSlug() {
        return slug;
    }

    public void setSlug(String slug) {
        this.slug = slug;
    }

    public Integer getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(Integer orderNumber) {
        this.orderNumber = orderNumber;
    }

    public String getTelegramGroupLink() {
        return telegramGroupLink;
    }

    public void setTelegramGroupLink(String telegramGroupLink) {
        this.telegramGroupLink = telegramGroupLink;
    }

    public String getTelegramChannelLink() {
        return telegramChannelLink;
    }

    public void setTelegramChannelLink(String telegramChannelLink) {
        this.telegramChannelLink = telegramChannelLink;
    }

    public Boolean getHasTelegramGroup() {
        return hasTelegramGroup;
    }

    public void setHasTelegramGroup(Boolean hasTelegramGroup) {
        this.hasTelegramGroup = hasTelegramGroup;
    }

    public Boolean getHasTelegramChannel() {
        return hasTelegramChannel;
    }

    public void setHasTelegramChannel(Boolean hasTelegramChannel) {
        this.hasTelegramChannel = hasTelegramChannel;
    }

    public Map<String, String> getStatus() {
        return status;
    }

    public List<AdminCourseSkillResponseDto> getSkills() {
        return skills;
    }

    public void setSkills(List<AdminCourseSkillResponseDto> skills) {
        this.skills = skills;
    }

    public void setStatus(Map<String, String> status) {
        this.status = status;
    }

    public BigDecimal getDiscountPrice() {
        return discountPrice;
    }

    public void setDiscountPrice(BigDecimal discountPrice) {
        this.discountPrice = discountPrice;
    }

    public Integer getDiscountPercent() {
        return discountPercent;
    }

    public void setDiscountPercent(Integer discountPercent) {
        this.discountPercent = discountPercent;
    }

    public LocalDateTime getDiscountStartAt() {
        return discountStartAt;
    }

    public void setDiscountStartAt(LocalDateTime discountStartAt) {
        this.discountStartAt = discountStartAt;
    }

    public LocalDateTime getDiscountEndAt() {
        return discountEndAt;
    }

    public void setDiscountEndAt(LocalDateTime discountEndAt) {
        this.discountEndAt = discountEndAt;
    }

    public String getImgName() {
        return imgName;
    }

    public void setImgName(String imgName) {
        this.imgName = imgName;
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

    public String getPromoCourseVideoUrl() {
        return promoCourseVideoUrl;
    }

    public void setPromoCourseVideoUrl(String promoCourseVideoUrl) {
        this.promoCourseVideoUrl = promoCourseVideoUrl;
    }

    public String getPromoCourseVideoFileName() {
        return promoCourseVideoFileName;
    }

    public void setPromoCourseVideoFileName(String promoCourseVideoFileName) {
        this.promoCourseVideoFileName = promoCourseVideoFileName;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public BigDecimal getFinalPrice() {
        return finalPrice;
    }

    public void setFinalPrice(BigDecimal finalPrice) {
        this.finalPrice = finalPrice;
    }

    public Long getPromoCourseVideoFileSize() {
        return promoCourseVideoFileSize;
    }

    public void setPromoCourseVideoFileSize(Long promoCourseVideoFileSize) {
        this.promoCourseVideoFileSize = promoCourseVideoFileSize;
    }

    public List<AdminModuleResponseDto> getModules() {
        return modules;
    }

    public void setModules(List<AdminModuleResponseDto> modules) {
        this.modules = modules;
    }
}
