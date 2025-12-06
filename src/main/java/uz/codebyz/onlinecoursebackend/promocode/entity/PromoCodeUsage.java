package uz.codebyz.onlinecoursebackend.promocode.entity;

import jakarta.persistence.*;
import uz.codebyz.onlinecoursebackend.course.entity.Course;
import uz.codebyz.onlinecoursebackend.student.entity.Student;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;
/**
 * PromoCodeUsage — promo kod qachon, kim tomonidan va qaysi kursga ishlatilganini yozib boruvchi jadval.
 * Statistikalar va chek loglari uchun xizmat qiladi.
 */
@Entity
@Table(name = "promo_code_usages")
public class PromoCodeUsage {

    /**
     * Promo qo‘llanishining unikal identifikatori.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(columnDefinition = "uuid")
    private UUID id;

    /**
     * Qaysi promo kod ishlatilgan.
     */
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "promo_code_id", nullable = false)
    private PromoCode promoCode;

    /**
     * Promo kodni qaysi student ishlatgan.
     */
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "student_id", nullable = false)
    private Student student;

    /**
     * Promo kod qaysi kursga nisbatan qo‘llangan.
     */
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "course_id", nullable = false)
    private Course course;

    /**
     * Promo kod ishlatilgan vaqt.
     */
    private LocalDateTime usedAt;

    /**
     * Promo kod qo‘llanganda berilgan chegirma summasi.
     * Real vaqt hisobida qayd etiladi.
     */
    @Column(precision = 19, scale = 2)
    private BigDecimal discountAmount;

    // getter-setterlar...

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public PromoCode getPromoCode() {
        return promoCode;
    }

    public void setPromoCode(PromoCode promoCode) {
        this.promoCode = promoCode;
    }

    public Student getStudent() {
        return student;
    }

    public void setStudent(Student student) {
        this.student = student;
    }

    public Course getCourse() {
        return course;
    }

    public void setCourse(Course course) {
        this.course = course;
    }

    public LocalDateTime getUsedAt() {
        return usedAt;
    }

    public void setUsedAt(LocalDateTime usedAt) {
        this.usedAt = usedAt;
    }

    public BigDecimal getDiscountAmount() {
        return discountAmount;
    }

    public void setDiscountAmount(BigDecimal discountAmount) {
        this.discountAmount = discountAmount;
    }
}

