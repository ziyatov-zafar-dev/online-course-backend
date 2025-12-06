package uz.codebyz.onlinecoursebackend.course.entity;

import jakarta.persistence.*;
import uz.codebyz.onlinecoursebackend.category.entity.Category;
import uz.codebyz.onlinecoursebackend.helper.CurrentTime;
import uz.codebyz.onlinecoursebackend.module.entity.Module;
import uz.codebyz.onlinecoursebackend.promocode.entity.PromoCode;
import uz.codebyz.onlinecoursebackend.skill.entity.Skill;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "courses")
public class Course {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(columnDefinition = "uuid")
    private UUID id;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "category_id", nullable = false)
    private Category category;
/*
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "teacher_id", nullable = false)
    private Teacher teacher;
*/
    private String name;
    private String description;
    private Boolean active;
    private Boolean deleted;
    @Column(unique = true, nullable = false)
    private String slug;
    private Integer orderNumber;
    private String telegramGroupLink;
    private String telegramChannelLink;
    private Boolean hasTelegramGroup;
    private Boolean hasTelegramChannel;
    @Enumerated(EnumType.STRING)
    private CourseStatus status;
    @Column(precision = 19, scale = 2,nullable = true)
    private BigDecimal discountPrice;
    @Column(precision = 19, scale = 2,nullable = true)
    private BigDecimal price;


    @Column(precision = 19, scale = 2,nullable = true)
    private BigDecimal finalPrice;
    private Integer discountPercent;
    private LocalDateTime discountStartAt;
    private LocalDateTime discountEndAt;
    private String imgName;
    @Column(columnDefinition = "TEXT")
    private String imgUrl;
    private Long imgSize;
    @Column(columnDefinition = "TEXT")
    private String promoCourseVideoUrl;
    private String promoCourseVideoFileName;
    private Long promoCourseVideoFileSize;
//    @Column(nullable = false)
    private LocalDateTime created;
//    @Column(nullable = false)
    private LocalDateTime updated;
    @OneToMany(mappedBy = "course", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<PromoCode> promoCodes;
    @OneToMany(mappedBy = "course", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<Skill> skills;
    @OneToMany(mappedBy = "course", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<Module>modules;

    public List<PromoCode> getPromoCodes() {
        return promoCodes;
    }

    public void setPromoCodes(List<PromoCode> promoCodes) {
        this.promoCodes = promoCodes;
    }

   /* public Teacher getTeacher() {
        return teacher;
    }

    public void setTeacher(Teacher teacher) {
        this.teacher = teacher;
    }*/

    public List<Module> getModules() {
        return modules;
    }

    public void setModules(List<Module> modules) {
        this.modules = modules;
    }

    public List<Skill> getSkills() {
        return skills;
    }

    public void setSkills(List<Skill> skills) {
        this.skills = skills;
    }

    public BigDecimal getFinalPrice() {
        return finalPrice;
    }

    public void setFinalPrice(BigDecimal finalPrice) {
        this.finalPrice = finalPrice;
    }

    public LocalDateTime getCreated() {
        return created;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public LocalDateTime getUpdated() {
        return updated;
    }

    public void setUpdatedAt() {
        this.updated = CurrentTime.currentTime();
    }

    public void setUpdated(LocalDateTime updatedAt) {
        this.updated =updatedAt ;
    }

    public Long getPromoCourseVideoFileSize() {
        return promoCourseVideoFileSize;
    }

    public void setPromoCourseVideoFileSize(Long promoCourseVideoFileSize) {
        this.promoCourseVideoFileSize = promoCourseVideoFileSize;
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

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public Boolean getDeleted() {
        return deleted;
    }

    public void setDeleted(Boolean deleted) {
        this.deleted = deleted;
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

    public String getImgName() {
        return imgName;
    }

    public void setImgName(String imgName) {
        this.imgName = imgName;
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

    public CourseStatus getStatus() {
        return status;
    }

    public void setStatus(CourseStatus status) {
        this.status = status;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public void setCreatedAt() {
        this.created = CurrentTime.currentTime();
    }
    public void setCreated(LocalDateTime createdAt) {
        this.created = createdAt;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }
}
