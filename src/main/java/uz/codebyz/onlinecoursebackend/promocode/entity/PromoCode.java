package uz.codebyz.onlinecoursebackend.promocode.entity;

import jakarta.persistence.*;
import uz.codebyz.onlinecoursebackend.course.entity.Course;
import uz.codebyz.onlinecoursebackend.user.User;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

/**
 * PromoCode — kurslarga yoki barcha kurslarga qo‘llanadigan promo kodlar jadvali.
 * Chegirma foizi yoki fixed amount ko‘rinishida berilishi mumkin.
 * Har bir promo kod qaysi o‘qituvchiga tegishli ekanligi ham saqlanadi.
 */
@Entity
@Table(name = "promo_codes")
public class PromoCode {

    /**
     * Promo kodning unikal identifikatori.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(columnDefinition = "uuid")
    private UUID id;

    /**
     * Promo kod qaysi kurs uchun berilgan.
     * Agar isAll = true bo‘lsa, course null bo‘lishi mumkin.
     */
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "course_id", nullable = false)
    private Course course;

    /**
     * Promo kodni kim yaratgan — odatda teacher yoki admin user.
     */
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    /**
     * Promo kodning o‘zi (masalan: ZAFAR10 yoki NEWYEAR20).
     * Unikal bo‘lishi shart.
     */
    @Column(unique = true, nullable = false)
    private String code;

    /**
     * Foizli chegirma (masalan: 10 → 10%).
     * fixedAmount bilan bir vaqtda ishlatilmaydi.
     */
    private Integer discountPercent;

    /**
     * Aniq pul bo‘yicha chegirma (fixed amount).
     * Masalan: 20000 → 20 000 so‘mlik chegirma.
     */
    @Column(precision = 19, scale = 2)
    private BigDecimal fixedAmount;

    /**
     * Promo kod umumiy necha marta ishlatilishi mumkin (limiti).
     */
    private Integer maxUsage;

    /**
     * Hozirga qadar promo kod nechta marta ishlatilgan.
     */
    private Integer userCount;

    /**
     * Promo kod qachondan boshlab aktiv bo‘ladi.
     */
    private LocalDateTime validFrom;

    /**
     * Promo kod qachongacha aktiv bo‘ladi.
     */
    private LocalDateTime validUntil;

    /**
     * Promo kodning hozirgi holati: active = true → ishlaydi.
     */
    private Boolean active;

    /**
     * Promo kod yaratilgan vaqt.
     */
    private LocalDateTime created;

    /**
     * Promo kod oxirgi marta o‘zgartirilgan vaqt.
     */
    private LocalDateTime updated;

    @OneToMany(mappedBy = "promoCode", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<PromoCodeUsage> promoCodeUsages;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public Course getCourse() {
        return course;
    }

    public void setCourse(Course course) {
        this.course = course;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Integer getDiscountPercent() {
        return discountPercent;
    }

    public void setDiscountPercent(Integer discountPercent) {
        this.discountPercent = discountPercent;
    }

    public BigDecimal getFixedAmount() {
        return fixedAmount;
    }

    public void setFixedAmount(BigDecimal fixedAmount) {
        this.fixedAmount = fixedAmount;
    }

    public Integer getMaxUsage() {
        return maxUsage;
    }

    public void setMaxUsage(Integer maxUsage) {
        this.maxUsage = maxUsage;
    }

    public Integer getUserCount() {
        return userCount;
    }

    public void setUserCount(Integer userCount) {
        this.userCount = userCount;
    }

    public LocalDateTime getValidFrom() {
        return validFrom;
    }

    public void setValidFrom(LocalDateTime validFrom) {
        this.validFrom = validFrom;
    }

    public LocalDateTime getValidUntil() {
        return validUntil;
    }

    public void setValidUntil(LocalDateTime validUntil) {
        this.validUntil = validUntil;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public LocalDateTime getCreated() {
        return created;
    }

    public void setCreated(LocalDateTime created) {
        this.created = created;
    }

    public LocalDateTime getUpdated() {
        return updated;
    }

    public void setUpdated(LocalDateTime updated) {
        this.updated = updated;
    }

    public List<PromoCodeUsage> getPromoCodeUsages() {
        return promoCodeUsages;
    }

    public void setPromoCodeUsages(List<PromoCodeUsage> promoCodeUsages) {
        this.promoCodeUsages = promoCodeUsages;
    }
}

