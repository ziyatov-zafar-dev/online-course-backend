/*
Keling, to'liq kodlarni professional darajada yozamiz. Lombokdan voz kechamiz, barcha getter/setterlarni qo'lda yozamiz.

        **1. Entity fayllari:**

        **PaymentStatus.java**
        ```java
package uz.codebyz.onlinecoursebackend.payment.entity;

public enum PaymentStatus {
    PENDING("Kutilmoqda", "Pending"),
    SUCCESS("Muvaffaqiyatli", "Success"),
    FAILED("Muvaffaqiyatsiz", "Failed"),
    CANCELLED("Bekor qilindi", "Cancelled"),
    REFUNDED("Qaytarildi", "Refunded");

    private final String uz;
    private final String en;

    PaymentStatus(String uz, String en) {
        this.uz = uz;
        this.en = en;
    }

    public String getUz() {
        return uz;
    }

    public String getEn() {
        return en;
    }
}
```

        **PaymentProvider.java**
        ```java
package uz.codebyz.onlinecoursebackend.payment.entity;

public enum PaymentProvider {
    PAYME("Payme", "Payme"),
    CLICK("Click", "Click"),
    UZUM("Uzum Bank", "Uzum Bank"),
    PAYLOV("Paylov", "Paylov"),
    HAMKOR("Hamkor bank", "Hamkor bank"),
    PAYPAL("PayPal", "PayPal"),
    OTHER("Boshqa", "Other");

    private final String uz;
    private final String en;

    PaymentProvider(String uz, String en) {
        this.uz = uz;
        this.en = en;
    }

    public String getUz() {
        return uz;
    }

    public String getEn() {
        return en;
    }
}
```

        **SubscriptionPeriod.java**
        ```java
package uz.codebyz.onlinecoursebackend.payment.entity;

public enum SubscriptionPeriod {
    MONTHLY(1, "1 oylik", "1 month"),
    QUARTERLY(3, "3 oylik", "3 months"),
    SEMI_ANNUAL(6, "6 oylik", "6 months"),
    ANNUAL(12, "1 yillik", "1 year"),
    BIENNIAL(24, "2 yillik", "2 years"),
    TRIENNIAL(36, "3 yillik", "3 years");

    private final int months;
    private final String uz;
    private final String en;

    SubscriptionPeriod(int months, String uz, String en) {
        this.months = months;
        this.uz = uz;
        this.en = en;
    }

    public int getMonths() {
        return months;
    }

    public String getUz() {
        return uz;
    }

    public String getEn() {
        return en;
    }
}
```

        **SubscriptionStatus.java**
        ```java
package uz.codebyz.onlinecoursebackend.payment.entity;

public enum SubscriptionStatus {
    ACTIVE("Faol", "Active"),
    EXPIRED("Muddati tugagan", "Expired"),
    CANCELLED("Bekor qilingan", "Cancelled"),
    PENDING("Kutilmoqda", "Pending");

    private final String uz;
    private final String en;

    SubscriptionStatus(String uz, String en) {
        this.uz = uz;
        this.en = en;
    }

    public String getUz() {
        return uz;
    }

    public String getEn() {
        return en;
    }
}
```

        **CoursePaymentStatus.java**
        ```java
package uz.codebyz.onlinecoursebackend.payment.entity;

public enum CoursePaymentStatus {
    PAID("To'langan", "Paid"),
    REFUNDED("Qaytarilgan", "Refunded"),
    PENDING("Kutilmoqda", "Pending");

    private final String uz;
    private final String en;

    CoursePaymentStatus(String uz, String en) {
        this.uz = uz;
        this.en = en;
    }

    public String getUz() {
        return uz;
    }

    public String getEn() {
        return en;
    }
}
```

        **SubscriptionPlan.java**
        ```java
package uz.codebyz.onlinecoursebackend.payment.entity;

import jakarta.persistence.*;
        import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "subscription_plans")
public class SubscriptionPlan {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(columnDefinition = "uuid")
    private UUID id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private SubscriptionPeriod period;

    @Column(precision = 19, scale = 2, nullable = false)
    private BigDecimal price;

    @Column(nullable = false)
    private String nameUz;

    @Column(nullable = false)
    private String nameEn;

    @Column(columnDefinition = "TEXT")
    private String descriptionUz;

    @Column(columnDefinition = "TEXT")
    private String descriptionEn;

    @Column(nullable = false)
    private boolean active = true;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    @Column(nullable = false)
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public SubscriptionPeriod getPeriod() {
        return period;
    }

    public void setPeriod(SubscriptionPeriod period) {
        this.period = period;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public String getNameUz() {
        return nameUz;
    }

    public void setNameUz(String nameUz) {
        this.nameUz = nameUz;
    }

    public String getNameEn() {
        return nameEn;
    }

    public void setNameEn(String nameEn) {
        this.nameEn = nameEn;
    }

    public String getDescriptionUz() {
        return descriptionUz;
    }

    public void setDescriptionUz(String descriptionUz) {
        this.descriptionUz = descriptionUz;
    }

    public String getDescriptionEn() {
        return descriptionEn;
    }

    public void setDescriptionEn(String descriptionEn) {
        this.descriptionEn = descriptionEn;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
}
```

        **TeacherSubscription.java**
        ```java
package uz.codebyz.onlinecoursebackend.payment.entity;

import jakarta.persistence.*;
        import uz.codebyz.onlinecoursebackend.teacher.entity.Teacher;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "teacher_subscriptions")
public class TeacherSubscription {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(columnDefinition = "uuid")
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "teacher_id", nullable = false)
    private Teacher teacher;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "subscription_plan_id", nullable = false)
    private SubscriptionPlan subscriptionPlan;

    @Column(nullable = false)
    private LocalDateTime startDate;

    @Column(nullable = false)
    private LocalDateTime endDate;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private SubscriptionStatus status;

    @OneToOne(mappedBy = "teacherSubscription", cascade = CascadeType.ALL)
    private Payment payment;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public Teacher getTeacher() {
        return teacher;
    }

    public void setTeacher(Teacher teacher) {
        this.teacher = teacher;
    }

    public SubscriptionPlan getSubscriptionPlan() {
        return subscriptionPlan;
    }

    public void setSubscriptionPlan(SubscriptionPlan subscriptionPlan) {
        this.subscriptionPlan = subscriptionPlan;
    }

    public LocalDateTime getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDateTime startDate) {
        this.startDate = startDate;
    }

    public LocalDateTime getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDateTime endDate) {
        this.endDate = endDate;
    }

    public SubscriptionStatus getStatus() {
        return status;
    }

    public void setStatus(SubscriptionStatus status) {
        this.status = status;
    }

    public Payment getPayment() {
        return payment;
    }

    public void setPayment(Payment payment) {
        this.payment = payment;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
```

        **CoursePayment.java**
        ```java
package uz.codebyz.onlinecoursebackend.payment.entity;

import jakarta.persistence.*;
        import uz.codebyz.onlinecoursebackend.course.entity.Course;
import uz.codebyz.onlinecoursebackend.student.entity.Student;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "course_payments")
public class CoursePayment {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(columnDefinition = "uuid")
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "student_id", nullable = false)
    private Student student;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "course_id", nullable = false)
    private Course course;

    @Column(precision = 19, scale = 2, nullable = false)
    private BigDecimal amount;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private CoursePaymentStatus status;

    @OneToOne(mappedBy = "coursePayment", cascade = CascadeType.ALL)
    private Payment payment;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
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

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public CoursePaymentStatus getStatus() {
        return status;
    }

    public void setStatus(CoursePaymentStatus status) {
        this.status = status;
    }

    public Payment getPayment() {
        return payment;
    }

    public void setPayment(Payment payment) {
        this.payment = payment;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
```

        **Payment.java**
        ```java
package uz.codebyz.onlinecoursebackend.payment.entity;

import jakarta.persistence.*;
        import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "payments")
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(columnDefinition = "uuid")
    private UUID id;

    @Column(unique = true, nullable = false)
    private String transactionId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private PaymentProvider provider;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private PaymentStatus status;

    @Column(precision = 19, scale = 2, nullable = false)
    private BigDecimal amount;

    @Column(length = 3)
    private String currency = "UZS";

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(unique = true)
    private String providerTransactionId;

    @Column(columnDefinition = "JSON")
    private String providerResponse;

    @OneToOne
    @JoinColumn(name = "teacher_subscription_id")
    private TeacherSubscription teacherSubscription;

    @OneToOne
    @JoinColumn(name = "course_payment_id")
    private CoursePayment coursePayment;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    @Column(nullable = false)
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
        if (transactionId == null) {
            transactionId = "TXN-" + System.currentTimeMillis();
        }
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    public PaymentProvider getProvider() {
        return provider;
    }

    public void setProvider(PaymentProvider provider) {
        this.provider = provider;
    }

    public PaymentStatus getStatus() {
        return status;
    }

    public void setStatus(PaymentStatus status) {
        this.status = status;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getProviderTransactionId() {
        return providerTransactionId;
    }

    public void setProviderTransactionId(String providerTransactionId) {
        this.providerTransactionId = providerTransactionId;
    }

    public String getProviderResponse() {
        return providerResponse;
    }

    public void setProviderResponse(String providerResponse) {
        this.providerResponse = providerResponse;
    }

    public TeacherSubscription getTeacherSubscription() {
        return teacherSubscription;
    }

    public void setTeacherSubscription(TeacherSubscription teacherSubscription) {
        this.teacherSubscription = teacherSubscription;
    }

    public CoursePayment getCoursePayment() {
        return coursePayment;
    }

    public void setCoursePayment(CoursePayment coursePayment) {
        this.coursePayment = coursePayment;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
}
```

        **2. Repository fayllari:**

        **PaymentRepository.java**
        ```java
package uz.codebyz.onlinecoursebackend.payment.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import uz.codebyz.onlinecoursebackend.payment.entity.Payment;
import uz.codebyz.onlinecoursebackend.payment.entity.PaymentStatus;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface PaymentRepository extends JpaRepository<Payment, UUID> {
    Optional<Payment> findByTransactionId(String transactionId);

    Optional<Payment> findByProviderTransactionId(String providerTransactionId);

    List<Payment> findByStatus(PaymentStatus status);

    List<Payment> findByProviderAndStatus(String provider, PaymentStatus status);

    @Query("SELECT p FROM Payment p WHERE p.createdAt BETWEEN :startDate AND :endDate")
    List<Payment> findPaymentsBetweenDates(@Param("startDate") LocalDateTime startDate,
                                           @Param("endDate") LocalDateTime endDate);

    @Query("SELECT p FROM Payment p WHERE p.teacherSubscription.id = :subscriptionId")
    Optional<Payment> findByTeacherSubscriptionId(@Param("subscriptionId") UUID subscriptionId);

    @Query("SELECT p FROM Payment p WHERE p.coursePayment.id = :coursePaymentId")
    Optional<Payment> findByCoursePaymentId(@Param("coursePaymentId") UUID coursePaymentId);

    boolean existsByTransactionId(String transactionId);
}
```

        **SubscriptionPlanRepository.java**
        ```java
package uz.codebyz.onlinecoursebackend.payment.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import uz.codebyz.onlinecoursebackend.payment.entity.SubscriptionPlan;
import uz.codebyz.onlinecoursebackend.payment.entity.SubscriptionPeriod;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface SubscriptionPlanRepository extends JpaRepository<SubscriptionPlan, UUID> {
    List<SubscriptionPlan> findByActiveTrue();

    List<SubscriptionPlan> findByPeriod(SubscriptionPeriod period);

    Optional<SubscriptionPlan> findByPeriodAndActiveTrue(SubscriptionPeriod period);

    @Query("SELECT sp FROM SubscriptionPlan sp WHERE sp.active = true ORDER BY sp.period ASC")
    List<SubscriptionPlan> findAllActiveOrderByPeriod();

    boolean existsByPeriodAndActiveTrue(SubscriptionPeriod period);

    @Query("SELECT sp FROM SubscriptionPlan sp WHERE LOWER(sp.nameUz) LIKE LOWER(CONCAT('%', :search, '%')) OR LOWER(sp.nameEn) LIKE LOWER(CONCAT('%', :search, '%'))")
    List<SubscriptionPlan> searchByName(@Param("search") String search);
}
```

        **TeacherSubscriptionRepository.java**
        ```java
package uz.codebyz.onlinecoursebackend.payment.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import uz.codebyz.onlinecoursebackend.payment.entity.SubscriptionStatus;
import uz.codebyz.onlinecoursebackend.payment.entity.TeacherSubscription;
import uz.codebyz.onlinecoursebackend.teacher.entity.Teacher;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface TeacherSubscriptionRepository extends JpaRepository<TeacherSubscription, UUID> {
    List<TeacherSubscription> findByTeacher(Teacher teacher);

    Optional<TeacherSubscription> findByTeacherAndStatus(Teacher teacher, SubscriptionStatus status);

    List<TeacherSubscription> findByStatus(SubscriptionStatus status);

    @Query("SELECT ts FROM TeacherSubscription ts WHERE ts.status = 'ACTIVE' AND ts.endDate < :currentDate")
    List<TeacherSubscription> findExpiredSubscriptions(@Param("currentDate") LocalDateTime currentDate);

    @Query("SELECT ts FROM TeacherSubscription ts WHERE ts.teacher.id = :teacherId AND ts.status = 'ACTIVE'")
    Optional<TeacherSubscription> findActiveSubscriptionByTeacherId(@Param("teacherId") Long teacherId);

    @Query("SELECT ts FROM TeacherSubscription ts WHERE ts.teacher.id = :teacherId ORDER BY ts.createdAt DESC")
    List<TeacherSubscription> findByTeacherIdOrderByCreatedAtDesc(@Param("teacherId") Long teacherId);

    boolean existsByTeacherAndStatus(Teacher teacher, SubscriptionStatus status);
}
```

        **CoursePaymentRepository.java**
        ```java
package uz.codebyz.onlinecoursebackend.payment.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import uz.codebyz.onlinecoursebackend.course.entity.Course;
import uz.codebyz.onlinecoursebackend.payment.entity.CoursePayment;
import uz.codebyz.onlinecoursebackend.payment.entity.CoursePaymentStatus;
import uz.codebyz.onlinecoursebackend.student.entity.Student;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface CoursePaymentRepository extends JpaRepository<CoursePayment, UUID> {
    List<CoursePayment> findByStudent(Student student);

    List<CoursePayment> findByCourse(Course course);

    Optional<CoursePayment> findByStudentAndCourse(Student student, Course course);

    List<CoursePayment> findByStatus(CoursePaymentStatus status);

    @Query("SELECT cp FROM CoursePayment cp WHERE cp.student.id = :studentId AND cp.course.id = :courseId")
    Optional<CoursePayment> findByStudentIdAndCourseId(@Param("studentId") UUID studentId,
                                                       @Param("courseId") UUID courseId);

    @Query("SELECT COUNT(cp) > 0 FROM CoursePayment cp WHERE cp.student.id = :studentId AND cp.course.id = :courseId AND cp.status = 'PAID'")
    boolean existsPaidPayment(@Param("studentId") UUID studentId,
                              @Param("courseId") UUID courseId);

    @Query("SELECT cp FROM CoursePayment cp WHERE cp.student.id = :studentId AND cp.status = 'PAID'")
    List<CoursePayment> findPaidCoursesByStudentId(@Param("studentId") UUID studentId);
}
```

        **3. DTO fayllari:**

        **src/main/java/uz/codebyz/onlinecoursebackend/payment/dto/request/PaymentRequest.java**
        ```java
package uz.codebyz.onlinecoursebackend.payment.dto.request;

import jakarta.validation.constraints.*;
        import uz.codebyz.onlinecoursebackend.payment.entity.PaymentProvider;
import java.math.BigDecimal;

public class PaymentRequest {

    @NotNull(message = "Provider majburiy")
    private PaymentProvider provider;

    @NotNull(message = "Miqdor majburiy")
    @DecimalMin(value = "0.01", message = "Miqdor 0.01 dan katta bo'lishi kerak")
    private BigDecimal amount;

    @Size(max = 3, message = "Valyuta kod 3 belgidan oshmasligi kerak")
    private String currency;

    @Size(max = 500, message = "Tavsif 500 belgidan oshmasligi kerak")
    private String description;

    private String teacherSubscriptionId;

    private String coursePaymentId;

    public PaymentProvider getProvider() {
        return provider;
    }

    public void setProvider(PaymentProvider provider) {
        this.provider = provider;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getTeacherSubscriptionId() {
        return teacherSubscriptionId;
    }

    public void setTeacherSubscriptionId(String teacherSubscriptionId) {
        this.teacherSubscriptionId = teacherSubscriptionId;
    }

    public String getCoursePaymentId() {
        return coursePaymentId;
    }

    public void setCoursePaymentId(String coursePaymentId) {
        this.coursePaymentId = coursePaymentId;
    }
}
```

        **src/main/java/uz/codebyz/onlinecoursebackend/payment/dto/request/CreateSubscriptionPlanRequest.java**
        ```java
package uz.codebyz.onlinecoursebackend.payment.dto.request;

import jakarta.validation.constraints.*;
        import uz.codebyz.onlinecoursebackend.payment.entity.SubscriptionPeriod;
import java.math.BigDecimal;

public class CreateSubscriptionPlanRequest {

    @NotNull(message = "Davr majburiy")
    private SubscriptionPeriod period;

    @NotNull(message = "Narx majburiy")
    @DecimalMin(value = "0.01", message = "Narx 0.01 dan katta bo'lishi kerak")
    private BigDecimal price;

    @NotBlank(message = "O'zbekcha nom majburiy")
    @Size(min = 2, max = 100, message = "O'zbekcha nom 2-100 belgi oralig'ida bo'lishi kerak")
    private String nameUz;

    @NotBlank(message = "Inglizcha nom majburiy")
    @Size(min = 2, max = 100, message = "Inglizcha nom 2-100 belgi oralig'ida bo'lishi kerak")
    private String nameEn;

    @Size(max = 1000, message = "O'zbekcha tavsif 1000 belgidan oshmasligi kerak")
    private String descriptionUz;

    @Size(max = 1000, message = "Inglizcha tavsif 1000 belgidan oshmasligi kerak")
    private String descriptionEn;

    private boolean active = true;

    public SubscriptionPeriod getPeriod() {
        return period;
    }

    public void setPeriod(SubscriptionPeriod period) {
        this.period = period;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public String getNameUz() {
        return nameUz;
    }

    public void setNameUz(String nameUz) {
        this.nameUz = nameUz;
    }

    public String getNameEn() {
        return nameEn;
    }

    public void setNameEn(String nameEn) {
        this.nameEn = nameEn;
    }

    public String getDescriptionUz() {
        return descriptionUz;
    }

    public void setDescriptionUz(String descriptionUz) {
        this.descriptionUz = descriptionUz;
    }

    public String getDescriptionEn() {
        return descriptionEn;
    }

    public void setDescriptionEn(String descriptionEn) {
        this.descriptionEn = descriptionEn;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }
}
```

        **src/main/java/uz/codebyz/onlinecoursebackend/payment/dto/request/TeacherSubscriptionRequest.java**
        ```java
package uz.codebyz.onlinecoursebackend.payment.dto.request;

import jakarta.validation.constraints.*;

public class TeacherSubscriptionRequest {

    @NotNull(message = "O'qituvchi ID majburiy")
    private Long teacherId;

    @NotNull(message = "Obuna rejasi ID majburiy")
    private String subscriptionPlanId;

    @Size(max = 500, message = "Tavsif 500 belgidan oshmasligi kerak")
    private String description;

    public Long getTeacherId() {
        return teacherId;
    }

    public void setTeacherId(Long teacherId) {
        this.teacherId = teacherId;
    }

    public String getSubscriptionPlanId() {
        return subscriptionPlanId;
    }

    public void setSubscriptionPlanId(String subscriptionPlanId) {
        this.subscriptionPlanId = subscriptionPlanId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
```

        **src/main/java/uz/codebyz/onlinecoursebackend/payment/dto/request/CoursePaymentRequest.java**
        ```java
package uz.codebyz.onlinecoursebackend.payment.dto.request;

import jakarta.validation.constraints.*;

public class CoursePaymentRequest {

    @NotNull(message = "O'quvchi ID majburiy")
    private String studentId;

    @NotNull(message = "Kurs ID majburiy")
    private String courseId;

    @Size(max = 500, message = "Tavsif 500 belgidan oshmasligi kerak")
    private String description;

    public String getStudentId() {
        return studentId;
    }

    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }

    public String getCourseId() {
        return courseId;
    }

    public void setCourseId(String courseId) {
        this.courseId = courseId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
```

        **src/main/java/uz/codebyz/onlinecoursebackend/payment/dto/response/PaymentResponse.java**
        ```java
package uz.codebyz.onlinecoursebackend.payment.dto.response;

import uz.codebyz.onlinecoursebackend.payment.entity.PaymentProvider;
import uz.codebyz.onlinecoursebackend.payment.entity.PaymentStatus;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public class PaymentResponse {

    private UUID id;
    private String transactionId;
    private PaymentProvider provider;
    private PaymentStatus status;
    private BigDecimal amount;
    private String currency;
    private String description;
    private String providerTransactionId;
    private String teacherSubscriptionId;
    private String coursePaymentId;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public PaymentResponse() {
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    public PaymentProvider getProvider() {
        return provider;
    }

    public void setProvider(PaymentProvider provider) {
        this.provider = provider;
    }

    public PaymentStatus getStatus() {
        return status;
    }

    public void setStatus(PaymentStatus status) {
        this.status = status;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getProviderTransactionId() {
        return providerTransactionId;
    }

    public void setProviderTransactionId(String providerTransactionId) {
        this.providerTransactionId = providerTransactionId;
    }

    public String getTeacherSubscriptionId() {
        return teacherSubscriptionId;
    }

    public void setTeacherSubscriptionId(String teacherSubscriptionId) {
        this.teacherSubscriptionId = teacherSubscriptionId;
    }

    public String getCoursePaymentId() {
        return coursePaymentId;
    }

    public void setCoursePaymentId(String coursePaymentId) {
        this.coursePaymentId = coursePaymentId;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
}
```

        **src/main/java/uz/codebyz/onlinecoursebackend/payment/dto/response/SubscriptionPlanResponse.java**
        ```java
package uz.codebyz.onlinecoursebackend.payment.dto.response;

import uz.codebyz.onlinecoursebackend.payment.entity.SubscriptionPeriod;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public class SubscriptionPlanResponse {

    private UUID id;
    private SubscriptionPeriod period;
    private BigDecimal price;
    private String nameUz;
    private String nameEn;
    private String descriptionUz;
    private String descriptionEn;
    private boolean active;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public SubscriptionPlanResponse() {
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public SubscriptionPeriod getPeriod() {
        return period;
    }

    public void setPeriod(SubscriptionPeriod period) {
        this.period = period;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public String getNameUz() {
        return nameUz;
    }

    public void setNameUz(String nameUz) {
        this.nameUz = nameUz;
    }

    public String getNameEn() {
        return nameEn;
    }

    public void setNameEn(String nameEn) {
        this.nameEn = nameEn;
    }

    public String getDescriptionUz() {
        return descriptionUz;
    }

    public void setDescriptionUz(String descriptionUz) {
        this.descriptionUz = descriptionUz;
    }

    public String getDescriptionEn() {
        return descriptionEn;
    }

    public void setDescriptionEn(String descriptionEn) {
        this.descriptionEn = descriptionEn;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
}
```

        **src/main/java/uz/codebyz/onlinecoursebackend/payment/dto/response/TeacherSubscriptionResponse.java**
        ```java
package uz.codebyz.onlinecoursebackend.payment.dto.response;

import uz.codebyz.onlinecoursebackend.payment.entity.SubscriptionStatus;
import java.time.LocalDateTime;
import java.util.UUID;

public class TeacherSubscriptionResponse {

    private UUID id;
    private Long teacherId;
    private String teacherName;
    private String subscriptionPlanId;
    private String subscriptionPlanName;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private SubscriptionStatus status;
    private String paymentId;
    private LocalDateTime createdAt;

    public TeacherSubscriptionResponse() {
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public Long getTeacherId() {
        return teacherId;
    }

    public void setTeacherId(Long teacherId) {
        this.teacherId = teacherId;
    }

    public String getTeacherName() {
        return teacherName;
    }

    public void setTeacherName(String teacherName) {
        this.teacherName = teacherName;
    }

    public String getSubscriptionPlanId() {
        return subscriptionPlanId;
    }

    public void setSubscriptionPlanId(String subscriptionPlanId) {
        this.subscriptionPlanId = subscriptionPlanId;
    }

    public String getSubscriptionPlanName() {
        return subscriptionPlanName;
    }

    public void setSubscriptionPlanName(String subscriptionPlanName) {
        this.subscriptionPlanName = subscriptionPlanName;
    }

    public LocalDateTime getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDateTime startDate) {
        this.startDate = startDate;
    }

    public LocalDateTime getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDateTime endDate) {
        this.endDate = endDate;
    }

    public SubscriptionStatus getStatus() {
        return status;
    }

    public void setStatus(SubscriptionStatus status) {
        this.status = status;
    }

    public String getPaymentId() {
        return paymentId;
    }

    public void setPaymentId(String paymentId) {
        this.paymentId = paymentId;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
```

        **src/main/java/uz/codebyz/onlinecoursebackend/payment/dto/response/CoursePaymentResponse.java**
        ```java
package uz.codebyz.onlinecoursebackend.payment.dto.response;

import uz.codebyz.onlinecoursebackend.payment.entity.CoursePaymentStatus;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public class CoursePaymentResponse {

    private UUID id;
    private UUID studentId;
    private String studentName;
    private UUID courseId;
    private String courseName;
    private BigDecimal amount;
    private CoursePaymentStatus status;
    private String paymentId;
    private LocalDateTime createdAt;

    public CoursePaymentResponse() {
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public UUID getStudentId() {
        return studentId;
    }

    public void setStudentId(UUID studentId) {
        this.studentId = studentId;
    }

    public String getStudentName() {
        return studentName;
    }

    public void setStudentName(String studentName) {
        this.studentName = studentName;
    }

    public UUID getCourseId() {
        return courseId;
    }

    public void setCourseId(UUID courseId) {
        this.courseId = courseId;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public CoursePaymentStatus getStatus() {
        return status;
    }

    public void setStatus(CoursePaymentStatus status) {
        this.status = status;
    }

    public String getPaymentId() {
        return paymentId;
    }

    public void setPaymentId(String paymentId) {
        this.paymentId = paymentId;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
```

        **4. Config fayli:**

        **src/main/java/uz/codebyz/onlinecoursebackend/payment/config/PaymentProperties.java**
        ```java
package uz.codebyz.onlinecoursebackend.payment.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "payments")
public class PaymentProperties {

    private Payme payme;
    private Click click;
    private Uzum uzum;
    private Paylov paylov;
    private Hamkor hamkor;
    private Paypal paypal;

    public Payme getPayme() {
        return payme;
    }

    public void setPayme(Payme payme) {
        this.payme = payme;
    }

    public Click getClick() {
        return click;
    }

    public void setClick(Click click) {
        this.click = click;
    }

    public Uzum getUzum() {
        return uzum;
    }

    public void setUzum(Uzum uzum) {
        this.uzum = uzum;
    }

    public Paylov getPaylov() {
        return paylov;
    }

    public void setPaylov(Paylov paylov) {
        this.paylov = paylov;
    }

    public Hamkor getHamkor() {
        return hamkor;
    }

    public void setHamkor(Hamkor hamkor) {
        this.hamkor = hamkor;
    }

    public Paypal getPaypal() {
        return paypal;
    }

    public void setPaypal(Paypal paypal) {
        this.paypal = paypal;
    }

    public static class Payme {
        private String merchantId;
        private String secretKey;
        private boolean testMode;

        public String getMerchantId() {
            return merchantId;
        }

        public void setMerchantId(String merchantId) {
            this.merchantId = merchantId;
        }

        public String getSecretKey() {
            return secretKey;
        }

        public void setSecretKey(String secretKey) {
            this.secretKey = secretKey;
        }

        public boolean isTestMode() {
            return testMode;
        }

        public void setTestMode(boolean testMode) {
            this.testMode = testMode;
        }
    }

    public static class Click {
        private String merchantId;
        private String serviceId;
        private String secretKey;

        public String getMerchantId() {
            return merchantId;
        }

        public void setMerchantId(String merchantId) {
            this.merchantId = merchantId;
        }

        public String getServiceId() {
            return serviceId;
        }

        public void setServiceId(String serviceId) {
            this.serviceId = serviceId;
        }

        public String getSecretKey() {
            return secretKey;
        }

        public void setSecretKey(String secretKey) {
            this.secretKey = secretKey;
        }
    }

    public static class Uzum {
        private String merchantId;
        private String secretKey;
        private String apiUrl;

        public String getMerchantId() {
            return merchantId;
        }

        public void setMerchantId(String merchantId) {
            this.merchantId = merchantId;
        }

        public String getSecretKey() {
            return secretKey;
        }

        public void setSecretKey(String secretKey) {
            this.secretKey = secretKey;
        }

        public String getApiUrl() {
            return apiUrl;
        }

        public void setApiUrl(String apiUrl) {
            this.apiUrl = apiUrl;
        }
    }

    public static class Paylov {
        private String merchantId;
        private String secretKey;
        private String apiUrl;

        public String getMerchantId() {
            return merchantId;
        }

        public void setMerchantId(String merchantId) {
            this.merchantId = merchantId;
        }

        public String getSecretKey() {
            return secretKey;
        }

        public void setSecretKey(String secretKey) {
            this.secretKey = secretKey;
        }

        public String getApiUrl() {
            return apiUrl;
        }

        public void setApiUrl(String apiUrl) {
            this.apiUrl = apiUrl;
        }
    }

    public static class Hamkor {
        private String terminalId;
        private String secretKey;
        private String apiUrl;

        public String getTerminalId() {
            return terminalId;
        }

        public void setTerminalId(String terminalId) {
            this.terminalId = terminalId;
        }

        public String getSecretKey() {
            return secretKey;
        }

        public void setSecretKey(String secretKey) {
            this.secretKey = secretKey;
        }

        public String getApiUrl() {
            return apiUrl;
        }

        public void setApiUrl(String apiUrl) {
            this.apiUrl = apiUrl;
        }
    }

    public static class Paypal {
        private String clientId;
        private String clientSecret;
        private String mode;
        private String apiUrl;

        public String getClientId() {
            return clientId;
        }

        public void setClientId(String clientId) {
            this.clientId = clientId;
        }

        public String getClientSecret() {
            return clientSecret;
        }

        public void setClientSecret(String clientSecret) {
            this.clientSecret = clientSecret;
        }

        public String getMode() {
            return mode;
        }

        public void setMode(String mode) {
            this.mode = mode;
        }

        public String getApiUrl() {
            return apiUrl;
        }

        public void setApiUrl(String apiUrl) {
            this.apiUrl = apiUrl;
        }
    }
}
```

        **5. Service fayllari:**

        **PaymentService.java**
        ```java
package uz.codebyz.onlinecoursebackend.payment.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import uz.codebyz.onlinecoursebackend.payment.dto.request.PaymentRequest;
import uz.codebyz.onlinecoursebackend.payment.dto.response.PaymentResponse;
import uz.codebyz.onlinecoursebackend.payment.entity.PaymentStatus;
import java.util.UUID;

public interface PaymentService {

    PaymentResponse createPayment(PaymentRequest request);

    PaymentResponse getPaymentById(UUID id);

    PaymentResponse getPaymentByTransactionId(String transactionId);

    Page<PaymentResponse> getAllPayments(Pageable pageable);

    Page<PaymentResponse> getPaymentsByStatus(PaymentStatus status, Pageable pageable);

    PaymentResponse updatePaymentStatus(UUID id, PaymentStatus status, String providerTransactionId);

    PaymentResponse refundPayment(UUID id);

    boolean verifyPayment(String transactionId);
}
```

        **SubscriptionPlanService.java**
        ```java
package uz.codebyz.onlinecoursebackend.payment.service;

import uz.codebyz.onlinecoursebackend.payment.dto.request.CreateSubscriptionPlanRequest;
import uz.codebyz.onlinecoursebackend.payment.dto.response.SubscriptionPlanResponse;
import uz.codebyz.onlinecoursebackend.payment.entity.SubscriptionPeriod;
import java.util.List;
import java.util.UUID;

public interface SubscriptionPlanService {

    SubscriptionPlanResponse createSubscriptionPlan(CreateSubscriptionPlanRequest request);

    SubscriptionPlanResponse getSubscriptionPlanById(UUID id);

    List<SubscriptionPlanResponse> getAllSubscriptionPlans();

    List<SubscriptionPlanResponse> getActiveSubscriptionPlans();

    SubscriptionPlanResponse updateSubscriptionPlan(UUID id, CreateSubscriptionPlanRequest request);

    void deleteSubscriptionPlan(UUID id);

    SubscriptionPlanResponse toggleSubscriptionPlanStatus(UUID id, boolean active);

    SubscriptionPlanResponse getSubscriptionPlanByPeriod(SubscriptionPeriod period);

    boolean existsByPeriod(SubscriptionPeriod period);
}
```

        **TeacherSubscriptionService.java**
        ```java
package uz.codebyz.onlinecoursebackend.payment.service;

import uz.codebyz.onlinecoursebackend.payment.dto.request.TeacherSubscriptionRequest;
import uz.codebyz.onlinecoursebackend.payment.dto.response.TeacherSubscriptionResponse;
import uz.codebyz.onlinecoursebackend.payment.entity.SubscriptionStatus;
import java.util.List;
import java.util.UUID;

public interface TeacherSubscriptionService {

    TeacherSubscriptionResponse createTeacherSubscription(TeacherSubscriptionRequest request);

    TeacherSubscriptionResponse getTeacherSubscriptionById(UUID id);

    List<TeacherSubscriptionResponse> getTeacherSubscriptionsByTeacherId(Long teacherId);

    TeacherSubscriptionResponse getActiveTeacherSubscription(Long teacherId);

    List<TeacherSubscriptionResponse> getSubscriptionsByStatus(SubscriptionStatus status);

    TeacherSubscriptionResponse updateSubscriptionStatus(UUID id, SubscriptionStatus status);

    void cancelTeacherSubscription(UUID id);

    boolean hasActiveSubscription(Long teacherId);

    void checkAndUpdateExpiredSubscriptions();
}
```

        **CoursePaymentService.java**
        ```java
package uz.codebyz.onlinecoursebackend.payment.service;

import uz.codebyz.onlinecoursebackend.payment.dto.request.CoursePaymentRequest;
import uz.codebyz.onlinecoursebackend.payment.dto.response.CoursePaymentResponse;
import uz.codebyz.onlinecoursebackend.payment.entity.CoursePaymentStatus;
import java.util.List;
import java.util.UUID;

public interface CoursePaymentService {

    CoursePaymentResponse createCoursePayment(CoursePaymentRequest request);

    CoursePaymentResponse getCoursePaymentById(UUID id);

    List<CoursePaymentResponse> getCoursePaymentsByStudentId(UUID studentId);

    List<CoursePaymentResponse> getCoursePaymentsByCourseId(UUID courseId);

    CoursePaymentResponse getCoursePaymentByStudentAndCourse(UUID studentId, UUID courseId);

    CoursePaymentResponse updateCoursePaymentStatus(UUID id, CoursePaymentStatus status);

    boolean hasPaidForCourse(UUID studentId, UUID courseId);

    void refundCoursePayment(UUID id);

    List<CoursePaymentResponse> getPaidCoursesByStudent(UUID studentId);
}
```

        **6. Controller fayllari:**

        **PaymentController.java**
        ```java
package uz.codebyz.onlinecoursebackend.payment.controller;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
        import uz.codebyz.onlinecoursebackend.common.ResponseDto;
import uz.codebyz.onlinecoursebackend.payment.dto.request.PaymentRequest;
import uz.codebyz.onlinecoursebackend.payment.dto.response.PaymentResponse;
import uz.codebyz.onlinecoursebackend.payment.entity.PaymentStatus;
import uz.codebyz.onlinecoursebackend.payment.service.PaymentService;
import java.util.UUID;

@RestController
@RequestMapping("/api/payments")
public class PaymentController {

    @Autowired
    private PaymentService paymentService;

    @PostMapping
    public ResponseEntity<ResponseDto<PaymentResponse>> createPayment(
            @Valid @RequestBody PaymentRequest request) {
        PaymentResponse response = paymentService.createPayment(request);
        return ResponseEntity.ok(ResponseDto.ok("To'lov yaratildi", response));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResponseDto<PaymentResponse>> getPayment(
            @PathVariable UUID id) {
        PaymentResponse response = paymentService.getPaymentById(id);
        return ResponseEntity.ok(ResponseDto.ok("To'lov ma'lumotlari", response));
    }

    @GetMapping("/transaction/{transactionId}")
    public ResponseEntity<ResponseDto<PaymentResponse>> getPaymentByTransactionId(
            @PathVariable String transactionId) {
        PaymentResponse response = paymentService.getPaymentByTransactionId(transactionId);
        return ResponseEntity.ok(ResponseDto.ok("To'lov ma'lumotlari", response));
    }

    @GetMapping
    public ResponseEntity<ResponseDto<Page<PaymentResponse>>> getAllPayments(
            Pageable pageable) {
        Page<PaymentResponse> payments = paymentService.getAllPayments(pageable);
        return ResponseEntity.ok(ResponseDto.ok("To'lovlar ro'yxati", payments));
    }

    @GetMapping("/status/{status}")
    public ResponseEntity<ResponseDto<Page<PaymentResponse>>> getPaymentsByStatus(
            @PathVariable PaymentStatus status,
            Pageable pageable) {
        Page<PaymentResponse> payments = paymentService.getPaymentsByStatus(status, pageable);
        return ResponseEntity.ok(ResponseDto.ok("To'lovlar ro'yxati", payments));
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<ResponseDto<PaymentResponse>> updatePaymentStatus(
            @PathVariable UUID id,
            @RequestParam PaymentStatus status,
            @RequestParam(required = false) String providerTransactionId) {
        PaymentResponse response = paymentService.updatePaymentStatus(id, status, providerTransactionId);
        return ResponseEntity.ok(ResponseDto.ok("To'lov holati yangilandi", response));
    }

    @PostMapping("/{id}/refund")
    public ResponseEntity<ResponseDto<PaymentResponse>> refundPayment(
            @PathVariable UUID id) {
        PaymentResponse response = paymentService.refundPayment(id);
        return ResponseEntity.ok(ResponseDto.ok("To'lov qaytarildi", response));
    }

    @GetMapping("/verify/{transactionId}")
    public ResponseEntity<ResponseDto<Boolean>> verifyPayment(
            @PathVariable String transactionId) {
        boolean verified = paymentService.verifyPayment(transactionId);
        if (verified) {
            return ResponseEntity.ok(ResponseDto.ok("To'lov tasdiqlandi", true));
        } else {
            return ResponseEntity.ok(ResponseDto.error("To'lov tasdiqlanmadi", false));
        }
    }
}
```

        **SubscriptionPlanController.java**
        ```java
package uz.codebyz.onlinecoursebackend.payment.controller;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
        import uz.codebyz.onlinecoursebackend.common.ResponseDto;
import uz.codebyz.onlinecoursebackend.payment.dto.request.CreateSubscriptionPlanRequest;
import uz.codebyz.onlinecoursebackend.payment.dto.response.SubscriptionPlanResponse;
import uz.codebyz.onlinecoursebackend.payment.entity.SubscriptionPeriod;
import uz.codebyz.onlinecoursebackend.payment.service.SubscriptionPlanService;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/subscription-plans")
public class SubscriptionPlanController {

    @Autowired
    private SubscriptionPlanService subscriptionPlanService;

    @PostMapping
    public ResponseEntity<ResponseDto<SubscriptionPlanResponse>> createSubscriptionPlan(
            @Valid @RequestBody CreateSubscriptionPlanRequest request) {
        SubscriptionPlanResponse response = subscriptionPlanService.createSubscriptionPlan(request);
        return ResponseEntity.ok(ResponseDto.ok("Obuna rejasi yaratildi", response));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResponseDto<SubscriptionPlanResponse>> getSubscriptionPlan(
            @PathVariable UUID id) {
        SubscriptionPlanResponse response = subscriptionPlanService.getSubscriptionPlanById(id);
        return ResponseEntity.ok(ResponseDto.ok("Obuna rejasi ma'lumotlari", response));
    }

    @GetMapping
    public ResponseEntity<ResponseDto<List<SubscriptionPlanResponse>>> getAllSubscriptionPlans() {
        List<SubscriptionPlanResponse> plans = subscriptionPlanService.getAllSubscriptionPlans();
        return ResponseEntity.ok(ResponseDto.ok("Obuna rejalari ro'yxati", plans));
    }

    @GetMapping("/active")
    public ResponseEntity<ResponseDto<List<SubscriptionPlanResponse>>> getActiveSubscriptionPlans() {
        List<SubscriptionPlanResponse> plans = subscriptionPlanService.getActiveSubscriptionPlans();
        return ResponseEntity.ok(ResponseDto.ok("Faol obuna rejalari", plans));
    }

    @GetMapping("/period/{period}")
    public ResponseEntity<ResponseDto<SubscriptionPlanResponse>> getSubscriptionPlanByPeriod(
            @PathVariable SubscriptionPeriod period) {
        SubscriptionPlanResponse response = subscriptionPlanService.getSubscriptionPlanByPeriod(period);
        return ResponseEntity.ok(ResponseDto.ok("Obuna rejasi ma'lumotlari", response));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ResponseDto<SubscriptionPlanResponse>> updateSubscriptionPlan(
            @PathVariable UUID id,
            @Valid @RequestBody CreateSubscriptionPlanRequest request) {
        SubscriptionPlanResponse response = subscriptionPlanService.updateSubscriptionPlan(id, request);
        return ResponseEntity.ok(ResponseDto.ok("Obuna rejasi yangilandi", response));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ResponseDto<Void>> deleteSubscriptionPlan(
            @PathVariable UUID id) {
        subscriptionPlanService.deleteSubscriptionPlan(id);
        return ResponseEntity.ok(ResponseDto.ok("Obuna rejasi o'chirildi"));
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<ResponseDto<SubscriptionPlanResponse>> toggleSubscriptionPlanStatus(
            @PathVariable UUID id,
            @RequestParam boolean active) {
        SubscriptionPlanResponse response = subscriptionPlanService.toggleSubscriptionPlanStatus(id, active);
        String message = active ? "Obuna rejasi faollashtirildi" : "Obuna rejasi nofaollashtirildi";
        return ResponseEntity.ok(ResponseDto.ok(message, response));
    }
}
```

        **TeacherSubscriptionController.java**
        ```java
package uz.codebyz.onlinecoursebackend.payment.controller;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
        import uz.codebyz.onlinecoursebackend.common.ResponseDto;
import uz.codebyz.onlinecoursebackend.payment.dto.request.TeacherSubscriptionRequest;
import uz.codebyz.onlinecoursebackend.payment.dto.response.TeacherSubscriptionResponse;
import uz.codebyz.onlinecoursebackend.payment.entity.SubscriptionStatus;
import uz.codebyz.onlinecoursebackend.payment.service.TeacherSubscriptionService;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/teacher-subscriptions")
public class TeacherSubscriptionController {

    @Autowired
    private TeacherSubscriptionService teacherSubscriptionService;

    @PostMapping
    public ResponseEntity<ResponseDto<TeacherSubscriptionResponse>> createTeacherSubscription(
            @Valid @RequestBody TeacherSubscriptionRequest request) {
        TeacherSubscriptionResponse response = teacherSubscriptionService.createTeacherSubscription(request);
        return ResponseEntity.ok(ResponseDto.ok("O'qituvchi obunasi yaratildi", response));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResponseDto<TeacherSubscriptionResponse>> getTeacherSubscription(
            @PathVariable UUID id) {
        TeacherSubscriptionResponse response = teacherSubscriptionService.getTeacherSubscriptionById(id);
        return ResponseEntity.ok(ResponseDto.ok("O'qituvchi obunasi ma'lumotlari", response));
    }

    @GetMapping("/teacher/{teacherId}")
    public ResponseEntity<ResponseDto<List<TeacherSubscriptionResponse>>> getTeacherSubscriptions(
            @PathVariable Long teacherId) {
        List<TeacherSubscriptionResponse> subscriptions = teacherSubscriptionService
                .getTeacherSubscriptionsByTeacherId(teacherId);
        return ResponseEntity.ok(ResponseDto.ok("O'qituvchi obunalari", subscriptions));
    }

    @GetMapping("/teacher/{teacherId}/active")
    public ResponseEntity<ResponseDto<TeacherSubscriptionResponse>> getActiveTeacherSubscription(
            @PathVariable Long teacherId) {
        TeacherSubscriptionResponse response = teacherSubscriptionService.getActiveTeacherSubscription(teacherId);
        return ResponseEntity.ok(ResponseDto.ok("Faol obuna ma'lumotlari", response));
    }

    @GetMapping("/status/{status}")
    public ResponseEntity<ResponseDto<List<TeacherSubscriptionResponse>>> getSubscriptionsByStatus(
            @PathVariable SubscriptionStatus status) {
        List<TeacherSubscriptionResponse> subscriptions = teacherSubscriptionService
                .getSubscriptionsByStatus(status);
        return ResponseEntity.ok(ResponseDto.ok("Obunalar ro'yxati", subscriptions));
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<ResponseDto<TeacherSubscriptionResponse>> updateSubscriptionStatus(
            @PathVariable UUID id,
            @RequestParam SubscriptionStatus status) {
        TeacherSubscriptionResponse response = teacherSubscriptionService.updateSubscriptionStatus(id, status);
        return ResponseEntity.ok(ResponseDto.ok("Obuna holati yangilandi", response));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ResponseDto<Void>> cancelTeacherSubscription(
            @PathVariable UUID id) {
        teacherSubscriptionService.cancelTeacherSubscription(id);
        return ResponseEntity.ok(ResponseDto.ok("Obuna bekor qilindi"));
    }

    @GetMapping("/teacher/{teacherId}/has-active")
    public ResponseEntity<ResponseDto<Boolean>> hasActiveSubscription(
            @PathVariable Long teacherId) {
        boolean hasActive = teacherSubscriptionService.hasActiveSubscription(teacherId);
        return ResponseEntity.ok(ResponseDto.ok("Faol obuna mavjudligi", hasActive));
    }
}
```

        **CoursePaymentController.java**
        ```java
package uz.codebyz.onlinecoursebackend.payment.controller;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
        import uz.codebyz.onlinecoursebackend.common.ResponseDto;
import uz.codebyz.onlinecoursebackend.payment.dto.request.CoursePaymentRequest;
import uz.codebyz.onlinecoursebackend.payment.dto.response.CoursePaymentResponse;
import uz.codebyz.onlinecoursebackend.payment.entity.CoursePaymentStatus;
import uz.codebyz.onlinecoursebackend.payment.service.CoursePaymentService;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/course-payments")
public class CoursePaymentController {

    @Autowired
    private CoursePaymentService coursePaymentService;

    @PostMapping
    public ResponseEntity<ResponseDto<CoursePaymentResponse>> createCoursePayment(
            @Valid @RequestBody CoursePaymentRequest request) {
        CoursePaymentResponse response = coursePaymentService.createCoursePayment(request);
        return ResponseEntity.ok(ResponseDto.ok("Kurs to'lovi yaratildi", response));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResponseDto<CoursePaymentResponse>> getCoursePayment(
            @PathVariable UUID id) {
        CoursePaymentResponse response = coursePaymentService.getCoursePaymentById(id);
        return ResponseEntity.ok(ResponseDto.ok("Kurs to'lovi ma'lumotlari", response));
    }

    @GetMapping("/student/{studentId}")
    public ResponseEntity<ResponseDto<List<CoursePaymentResponse>>> getCoursePaymentsByStudent(
            @PathVariable UUID studentId) {
        List<CoursePaymentResponse> payments = coursePaymentService.getCoursePaymentsByStudentId(studentId);
        return ResponseEntity.ok(ResponseDto.ok("O'quvchi kurs to'lovlari", payments));
    }

    @GetMapping("/course/{courseId}")
    public ResponseEntity<ResponseDto<List<CoursePaymentResponse>>> getCoursePaymentsByCourse(
            @PathVariable UUID courseId) {
        List<CoursePaymentResponse> payments = coursePaymentService.getCoursePaymentsByCourseId(courseId);
        return ResponseEntity.ok(ResponseDto.ok("Kurs to'lovlari", payments));
    }

    @GetMapping("/student/{studentId}/course/{courseId}")
    public ResponseEntity<ResponseDto<CoursePaymentResponse>> getCoursePaymentByStudentAndCourse(
            @PathVariable UUID studentId,
            @PathVariable UUID courseId) {
        CoursePaymentResponse response = coursePaymentService
                .getCoursePaymentByStudentAndCourse(studentId, courseId);
        return ResponseEntity.ok(ResponseDto.ok("Kurs to'lovi ma'lumotlari", response));
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<ResponseDto<CoursePaymentResponse>> updateCoursePaymentStatus(
            @PathVariable UUID id,
            @RequestParam CoursePaymentStatus status) {
        CoursePaymentResponse response = coursePaymentService.updateCoursePaymentStatus(id, status);
        return ResponseEntity.ok(ResponseDto.ok("Kurs to'lovi holati yangilandi", response));
    }

    @GetMapping("/student/{studentId}/course/{courseId}/has-paid")
    public ResponseEntity<ResponseDto<Boolean>> hasPaidForCourse(
            @PathVariable UUID studentId,
            @PathVariable UUID courseId) {
        boolean hasPaid = coursePaymentService.hasPaidForCourse(studentId, courseId);
        return ResponseEntity.ok(ResponseDto.ok("Kurs uchun to'lov mavjudligi", hasPaid));
    }

    @PostMapping("/{id}/refund")
    public ResponseEntity<ResponseDto<CoursePaymentResponse>> refundCoursePayment(
            @PathVariable UUID id) {
        CoursePaymentResponse response = coursePaymentService.refundCoursePayment(id);
        return ResponseEntity.ok(ResponseDto.ok("Kurs to'lovi qaytarildi", response));
    }

    @GetMapping("/student/{studentId}/paid-courses")
    public ResponseEntity<ResponseDto<List<CoursePaymentResponse>>> getPaidCoursesByStudent(
            @PathVariable UUID studentId) {
        List<CoursePaymentResponse> payments = coursePaymentService.getPaidCoursesByStudent(studentId);
        return ResponseEntity.ok(ResponseDto.ok("To'langan kurslar", payments));
    }
}
```

        **7. Implementation fayllari (service impl):**

        **PaymentServiceImpl.java**
        ```java
package uz.codebyz.onlinecoursebackend.payment.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import uz.codebyz.onlinecoursebackend.course.entity.Course;
import uz.codebyz.onlinecoursebackend.course.repository.CourseRepository;
import uz.codebyz.onlinecoursebackend.payment.dto.request.PaymentRequest;
import uz.codebyz.onlinecoursebackend.payment.dto.response.PaymentResponse;
import uz.codebyz.onlinecoursebackend.payment.entity.*;
        import uz.codebyz.onlinecoursebackend.payment.repository.CoursePaymentRepository;
import uz.codebyz.onlinecoursebackend.payment.repository.PaymentRepository;
import uz.codebyz.onlinecoursebackend.payment.repository.SubscriptionPlanRepository;
import uz.codebyz.onlinecoursebackend.payment.repository.TeacherSubscriptionRepository;
import uz.codebyz.onlinecoursebackend.payment.service.PaymentService;
import uz.codebyz.onlinecoursebackend.student.entity.Student;
import uz.codebyz.onlinecoursebackend.student.repository.StudentRepository;
import uz.codebyz.onlinecoursebackend.teacher.entity.Teacher;
import uz.codebyz.onlinecoursebackend.teacher.repository.TeacherRepository;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
public class PaymentServiceImpl implements PaymentService {

    @Autowired
    private PaymentRepository paymentRepository;

    @Autowired
    private TeacherSubscriptionRepository teacherSubscriptionRepository;

    @Autowired
    private CoursePaymentRepository coursePaymentRepository;

    @Autowired
    private TeacherRepository teacherRepository;

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private SubscriptionPlanRepository subscriptionPlanRepository;

    @Override
    @Transactional
    public PaymentResponse createPayment(PaymentRequest request) {
        Payment payment = new Payment();
        payment.setProvider(request.getProvider());
        payment.setAmount(request.getAmount());
        payment.setCurrency(request.getCurrency() != null ? request.getCurrency() : "UZS");
        payment.setDescription(request.getDescription());
        payment.setStatus(PaymentStatus.PENDING);

        // Link teacher subscription if provided
        if (request.getTeacherSubscriptionId() != null) {
            TeacherSubscription subscription = teacherSubscriptionRepository
                    .findById(UUID.fromString(request.getTeacherSubscriptionId()))
                    .orElseThrow(() -> new RuntimeException("Obuna topilmadi"));
            payment.setTeacherSubscription(subscription);
        }

        // Link course payment if provided
        if (request.getCoursePaymentId() != null) {
            CoursePayment coursePayment = coursePaymentRepository
                    .findById(UUID.fromString(request.getCoursePaymentId()))
                    .orElseThrow(() -> new RuntimeException("Kurs to'lovi topilmadi"));
            payment.setCoursePayment(coursePayment);
        }

        Payment savedPayment = paymentRepository.save(payment);
        return mapToResponse(savedPayment);
    }

    @Override
    public PaymentResponse getPaymentById(UUID id) {
        Payment payment = paymentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("To'lov topilmadi"));
        return mapToResponse(payment);
    }

    @Override
    public PaymentResponse getPaymentByTransactionId(String transactionId) {
        Payment payment = paymentRepository.findByTransactionId(transactionId)
                .orElseThrow(() -> new RuntimeException("To'lov topilmadi"));
        return mapToResponse(payment);
    }

    @Override
    public Page<PaymentResponse> getAllPayments(Pageable pageable) {
        return paymentRepository.findAll(pageable)
                .map(this::mapToResponse);
    }

    @Override
    public Page<PaymentResponse> getPaymentsByStatus(PaymentStatus status, Pageable pageable) {
        return paymentRepository.findByStatus(status, pageable)
                .map(this::mapToResponse);
    }

    @Override
    @Transactional
    public PaymentResponse updatePaymentStatus(UUID id, PaymentStatus status, String providerTransactionId) {
        Payment payment = paymentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("To'lov topilmadi"));

        payment.setStatus(status);
        if (providerTransactionId != null) {
            payment.setProviderTransactionId(providerTransactionId);
        }

        // If payment is successful, update related entities
        if (status == PaymentStatus.SUCCESS) {
            updateRelatedEntities(payment);
        }

        Payment updatedPayment = paymentRepository.save(payment);
        return mapToResponse(updatedPayment);
    }

    @Override
    @Transactional
    public PaymentResponse refundPayment(UUID id) {
        Payment payment = paymentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("To'lov topilmadi"));

        // Check if payment can be refunded
        if (payment.getStatus() != PaymentStatus.SUCCESS) {
            throw new RuntimeException("Faqat muvaffaqiyatli to'lovlar qaytarilishi mumkin");
        }

        payment.setStatus(PaymentStatus.REFUNDED);

        // Update related entities
        if (payment.getTeacherSubscription() != null) {
            TeacherSubscription subscription = payment.getTeacherSubscription();
            subscription.setStatus(SubscriptionStatus.CANCELLED);
            teacherSubscriptionRepository.save(subscription);
        }

        if (payment.getCoursePayment() != null) {
            CoursePayment coursePayment = payment.getCoursePayment();
            coursePayment.setStatus(CoursePaymentStatus.REFUNDED);
            coursePaymentRepository.save(coursePayment);
        }

        Payment refundedPayment = paymentRepository.save(payment);
        return mapToResponse(refundedPayment);
    }

    @Override
    public boolean verifyPayment(String transactionId) {
        Optional<Payment> paymentOpt = paymentRepository.findByTransactionId(transactionId);
        if (paymentOpt.isEmpty()) {
            return false;
        }

        Payment payment = paymentOpt.get();
        return payment.getStatus() == PaymentStatus.SUCCESS;
    }

    private void updateRelatedEntities(Payment payment) {
        if (payment.getTeacherSubscription() != null) {
            TeacherSubscription subscription = payment.getTeacherSubscription();
            subscription.setStatus(SubscriptionStatus.ACTIVE);
            subscription.setStartDate(LocalDateTime.now());

            // Calculate end date based on subscription plan
            SubscriptionPlan plan = subscription.getSubscriptionPlan();
            subscription.setEndDate(LocalDateTime.now().plusMonths(plan.getPeriod().getMonths()));

            teacherSubscriptionRepository.save(subscription);
        }

        if (payment.getCoursePayment() != null) {
            CoursePayment coursePayment = payment.getCoursePayment();
            coursePayment.setStatus(CoursePaymentStatus.PAID);
            coursePaymentRepository.save(coursePayment);
        }
    }

    private PaymentResponse mapToResponse(Payment payment) {
        PaymentResponse response = new PaymentResponse();
        response.setId(payment.getId());
        response.setTransactionId(payment.getTransactionId());
        response.setProvider(payment.getProvider());
        response.setStatus(payment.getStatus());
        response.setAmount(payment.getAmount());
        response.setCurrency(payment.getCurrency());
        response.setDescription(payment.getDescription());
        response.setProviderTransactionId(payment.getProviderTransactionId());

        if (payment.getTeacherSubscription() != null) {
            response.setTeacherSubscriptionId(payment.getTeacherSubscription().getId().toString());
        }

        if (payment.getCoursePayment() != null) {
            response.setCoursePaymentId(payment.getCoursePayment().getId().toString());
        }

        response.setCreatedAt(payment.getCreatedAt());
        response.setUpdatedAt(payment.getUpdatedAt());

        return response;
    }
}
```

        **SubscriptionPlanServiceImpl.java**
        ```java
package uz.codebyz.onlinecoursebackend.payment.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import uz.codebyz.onlinecoursebackend.payment.dto.request.CreateSubscriptionPlanRequest;
import uz.codebyz.onlinecoursebackend.payment.dto.response.SubscriptionPlanResponse;
import uz.codebyz.onlinecoursebackend.payment.entity.SubscriptionPeriod;
import uz.codebyz.onlinecoursebackend.payment.entity.SubscriptionPlan;
import uz.codebyz.onlinecoursebackend.payment.repository.SubscriptionPlanRepository;
import uz.codebyz.onlinecoursebackend.payment.service.SubscriptionPlanService;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class SubscriptionPlanServiceImpl implements SubscriptionPlanService {

    @Autowired
    private SubscriptionPlanRepository subscriptionPlanRepository;

    @Override
    @Transactional
    public SubscriptionPlanResponse createSubscriptionPlan(CreateSubscriptionPlanRequest request) {
        // Check if plan with this period already exists
        if (subscriptionPlanRepository.existsByPeriodAndActiveTrue(request.getPeriod())) {
            throw new RuntimeException("Bu davr uchun obuna rejasi allaqachon mavjud");
        }

        SubscriptionPlan plan = new SubscriptionPlan();
        plan.setPeriod(request.getPeriod());
        plan.setPrice(request.getPrice());
        plan.setNameUz(request.getNameUz());
        plan.setNameEn(request.getNameEn());
        plan.setDescriptionUz(request.getDescriptionUz());
        plan.setDescriptionEn(request.getDescriptionEn());
        plan.setActive(request.isActive());

        SubscriptionPlan savedPlan = subscriptionPlanRepository.save(plan);
        return mapToResponse(savedPlan);
    }

    @Override
    public SubscriptionPlanResponse getSubscriptionPlanById(UUID id) {
        SubscriptionPlan plan = subscriptionPlanRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Obuna rejasi topilmadi"));
        return mapToResponse(plan);
    }

    @Override
    public List<SubscriptionPlanResponse> getAllSubscriptionPlans() {
        return subscriptionPlanRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<SubscriptionPlanResponse> getActiveSubscriptionPlans() {
        return subscriptionPlanRepository.findByActiveTrue()
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public SubscriptionPlanResponse updateSubscriptionPlan(UUID id, CreateSubscriptionPlanRequest request) {
        SubscriptionPlan plan = subscriptionPlanRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Obuna rejasi topilmadi"));

        // Check if another active plan with this period exists
        if (!plan.getPeriod().equals(request.getPeriod())) {
            Optional<SubscriptionPlan> existingPlan = subscriptionPlanRepository
                    .findByPeriodAndActiveTrue(request.getPeriod());
            if (existingPlan.isPresent() && !existingPlan.get().getId().equals(id)) {
                throw new RuntimeException("Bu davr uchun obuna rejasi allaqachon mavjud");
            }
        }

        plan.setPeriod(request.getPeriod());
        plan.setPrice(request.getPrice());
        plan.setNameUz(request.getNameUz());
        plan.setNameEn(request.getNameEn());
        plan.setDescriptionUz(request.getDescriptionUz());
        plan.setDescriptionEn(request.getDescriptionEn());
        plan.setActive(request.isActive());

        SubscriptionPlan updatedPlan = subscriptionPlanRepository.save(plan);
        return mapToResponse(updatedPlan);
    }

    @Override
    @Transactional
    public void deleteSubscriptionPlan(UUID id) {
        SubscriptionPlan plan = subscriptionPlanRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Obuna rejasi topilmadi"));

        // Don't delete if there are active subscriptions using this plan
        // You can add this check if needed

        subscriptionPlanRepository.delete(plan);
    }

    @Override
    @Transactional
    public SubscriptionPlanResponse toggleSubscriptionPlanStatus(UUID id, boolean active) {
        SubscriptionPlan plan = subscriptionPlanRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Obuna rejasi topilmadi"));

        plan.setActive(active);

        SubscriptionPlan updatedPlan = subscriptionPlanRepository.save(plan);
        return mapToResponse(updatedPlan);
    }

    @Override
    public SubscriptionPlanResponse getSubscriptionPlanByPeriod(SubscriptionPeriod period) {
        SubscriptionPlan plan = subscriptionPlanRepository.findByPeriodAndActiveTrue(period)
                .orElseThrow(() -> new RuntimeException("Obuna rejasi topilmadi"));
        return mapToResponse(plan);
    }

    @Override
    public boolean existsByPeriod(SubscriptionPeriod period) {
        return subscriptionPlanRepository.existsByPeriodAndActiveTrue(period);
    }

    private SubscriptionPlanResponse mapToResponse(SubscriptionPlan plan) {
        SubscriptionPlanResponse response = new SubscriptionPlanResponse();
        response.setId(plan.getId());
        response.setPeriod(plan.getPeriod());
        response.setPrice(plan.getPrice());
        response.setNameUz(plan.getNameUz());
        response.setNameEn(plan.getNameEn());
        response.setDescriptionUz(plan.getDescriptionUz());
        response.setDescriptionEn(plan.getDescriptionEn());
        response.setActive(plan.isActive());
        response.setCreatedAt(plan.getCreatedAt());
        response.setUpdatedAt(plan.getUpdatedAt());
        return response;
    }
}
```

        **TeacherSubscriptionServiceImpl.java**
        ```java
package uz.codebyz.onlinecoursebackend.payment.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import uz.codebyz.onlinecoursebackend.payment.dto.request.TeacherSubscriptionRequest;
import uz.codebyz.onlinecoursebackend.payment.dto.response.TeacherSubscriptionResponse;
import uz.codebyz.onlinecoursebackend.payment.entity.*;
        import uz.codebyz.onlinecoursebackend.payment.repository.SubscriptionPlanRepository;
import uz.codebyz.onlinecoursebackend.payment.repository.TeacherSubscriptionRepository;
import uz.codebyz.onlinecoursebackend.payment.service.TeacherSubscriptionService;
import uz.codebyz.onlinecoursebackend.teacher.entity.Teacher;
import uz.codebyz.onlinecoursebackend.teacher.repository.TeacherRepository;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class TeacherSubscriptionServiceImpl implements TeacherSubscriptionService {

    @Autowired
    private TeacherSubscriptionRepository teacherSubscriptionRepository;

    @Autowired
    private TeacherRepository teacherRepository;

    @Autowired
    private SubscriptionPlanRepository subscriptionPlanRepository;

    @Override
    @Transactional
    public TeacherSubscriptionResponse createTeacherSubscription(TeacherSubscriptionRequest request) {
        Teacher teacher = teacherRepository.findById(request.getTeacherId())
                .orElseThrow(() -> new RuntimeException("O'qituvchi topilmadi"));

        SubscriptionPlan plan = subscriptionPlanRepository.findById(UUID.fromString(request.getSubscriptionPlanId()))
                .orElseThrow(() -> new RuntimeException("Obuna rejasi topilmadi"));

        // Check if teacher already has active subscription
        if (hasActiveSubscription(teacher.getId())) {
            throw new RuntimeException("O'qituvchida allaqachon faol obuna mavjud");
        }

        TeacherSubscription subscription = new TeacherSubscription();
        subscription.setTeacher(teacher);
        subscription.setSubscriptionPlan(plan);
        subscription.setStartDate(LocalDateTime.now());
        subscription.setEndDate(LocalDateTime.now().plusMonths(plan.getPeriod().getMonths()));
        subscription.setStatus(SubscriptionStatus.PENDING);

        TeacherSubscription savedSubscription = teacherSubscriptionRepository.save(subscription);
        return mapToResponse(savedSubscription);
    }

    @Override
    public TeacherSubscriptionResponse getTeacherSubscriptionById(UUID id) {
        TeacherSubscription subscription = teacherSubscriptionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Obuna topilmadi"));
        return mapToResponse(subscription);
    }

    @Override
    public List<TeacherSubscriptionResponse> getTeacherSubscriptionsByTeacherId(Long teacherId) {
        Teacher teacher = teacherRepository.findById(teacherId)
                .orElseThrow(() -> new RuntimeException("O'qituvchi topilmadi"));

        return teacherSubscriptionRepository.findByTeacher(teacher)
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public TeacherSubscriptionResponse getActiveTeacherSubscription(Long teacherId) {
        TeacherSubscription subscription = teacherSubscriptionRepository
                .findActiveSubscriptionByTeacherId(teacherId)
                .orElseThrow(() -> new RuntimeException("Faol obuna topilmadi"));
        return mapToResponse(subscription);
    }

    @Override
    public List<TeacherSubscriptionResponse> getSubscriptionsByStatus(SubscriptionStatus status) {
        return teacherSubscriptionRepository.findByStatus(status)
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public TeacherSubscriptionResponse updateSubscriptionStatus(UUID id, SubscriptionStatus status) {
        TeacherSubscription subscription = teacherSubscriptionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Obuna topilmadi"));

        subscription.setStatus(status);

        TeacherSubscription updatedSubscription = teacherSubscriptionRepository.save(subscription);
        return mapToResponse(updatedSubscription);
    }

    @Override
    @Transactional
    public void cancelTeacherSubscription(UUID id) {
        TeacherSubscription subscription = teacherSubscriptionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Obuna topilmadi"));

        subscription.setStatus(SubscriptionStatus.CANCELLED);
        teacherSubscriptionRepository.save(subscription);
    }

    @Override
    public boolean hasActiveSubscription(Long teacherId) {
        return teacherSubscriptionRepository.findActiveSubscriptionByTeacherId(teacherId).isPresent();
    }

    @Override
    @Scheduled(cron = "0 0 0 * * ?") // Run daily at midnight
    @Transactional
    public void checkAndUpdateExpiredSubscriptions() {
        LocalDateTime now = LocalDateTime.now();
        List<TeacherSubscription> expiredSubscriptions = teacherSubscriptionRepository
                .findExpiredSubscriptions(now);

        for (TeacherSubscription subscription : expiredSubscriptions) {
            subscription.setStatus(SubscriptionStatus.EXPIRED);
            teacherSubscriptionRepository.save(subscription);
        }
    }

    private TeacherSubscriptionResponse mapToResponse(TeacherSubscription subscription) {
        TeacherSubscriptionResponse response = new TeacherSubscriptionResponse();
        response.setId(subscription.getId());
        response.setTeacherId(subscription.getTeacher().getId());
        response.setTeacherName(subscription.getTeacher().getUser().getFirstname() + " " +
                subscription.getTeacher().getUser().getLastname());
        response.setSubscriptionPlanId(subscription.getSubscriptionPlan().getId().toString());
        response.setSubscriptionPlanName(subscription.getSubscriptionPlan().getNameUz());
        response.setStartDate(subscription.getStartDate());
        response.setEndDate(subscription.getEndDate());
        response.setStatus(subscription.getStatus());

        if (subscription.getPayment() != null) {
            response.setPaymentId(subscription.getPayment().getId().toString());
        }

        response.setCreatedAt(subscription.getCreatedAt());
        return response;
    }
}
```

        **CoursePaymentServiceImpl.java**
        ```java
package uz.codebyz.onlinecoursebackend.payment.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import uz.codebyz.onlinecoursebackend.course.entity.Course;
import uz.codebyz.onlinecoursebackend.course.repository.CourseRepository;
import uz.codebyz.onlinecoursebackend.payment.dto.request.CoursePaymentRequest;
import uz.codebyz.onlinecoursebackend.payment.dto.response.CoursePaymentResponse;
import uz.codebyz.onlinecoursebackend.payment.entity.CoursePayment;
import uz.codebyz.onlinecoursebackend.payment.entity.CoursePaymentStatus;
import uz.codebyz.onlinecoursebackend.payment.repository.CoursePaymentRepository;
import uz.codebyz.onlinecoursebackend.payment.service.CoursePaymentService;
import uz.codebyz.onlinecoursebackend.student.entity.Student;
import uz.codebyz.onlinecoursebackend.student.repository.StudentRepository;
import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class CoursePaymentServiceImpl implements CoursePaymentService {

    @Autowired
    private CoursePaymentRepository coursePaymentRepository;

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private CourseRepository courseRepository;

    @Override
    @Transactional
    public CoursePaymentResponse createCoursePayment(CoursePaymentRequest request) {
        Student student = studentRepository.findById(UUID.fromString(request.getStudentId()))
                .orElseThrow(() -> new RuntimeException("O'quvchi topilmadi"));

        Course course = courseRepository.findById(UUID.fromString(request.getCourseId()))
                .orElseThrow(() -> new RuntimeException("Kurs topilmadi"));

        // Check if student already paid for this course
        if (hasPaidForCourse(student.getId(), course.getId())) {
            throw new RuntimeException("O'quvchi ushbu kurs uchun allaqachon to'lov qilgan");
        }

        CoursePayment coursePayment = new CoursePayment();
        coursePayment.setStudent(student);
        coursePayment.setCourse(course);
        coursePayment.setAmount(course.getFinalPrice() != null ? course.getFinalPrice() : course.getPrice());
        coursePayment.setStatus(CoursePaymentStatus.PENDING);

        CoursePayment savedPayment = coursePaymentRepository.save(coursePayment);
        return mapToResponse(savedPayment);
    }

    @Override
    public CoursePaymentResponse getCoursePaymentById(UUID id) {
        CoursePayment coursePayment = coursePaymentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Kurs to'lovi topilmadi"));
        return mapToResponse(coursePayment);
    }

    @Override
    public List<CoursePaymentResponse> getCoursePaymentsByStudentId(UUID studentId) {
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new RuntimeException("O'quvchi topilmadi"));

        return coursePaymentRepository.findByStudent(student)
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<CoursePaymentResponse> getCoursePaymentsByCourseId(UUID courseId) {
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new RuntimeException("Kurs topilmadi"));

        return coursePaymentRepository.findByCourse(course)
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public CoursePaymentResponse getCoursePaymentByStudentAndCourse(UUID studentId, UUID courseId) {
        CoursePayment coursePayment = coursePaymentRepository
                .findByStudentIdAndCourseId(studentId, courseId)
                .orElseThrow(() -> new RuntimeException("Kurs to'lovi topilmadi"));
        return mapToResponse(coursePayment);
    }

    @Override
    @Transactional
    public CoursePaymentResponse updateCoursePaymentStatus(UUID id, CoursePaymentStatus status) {
        CoursePayment coursePayment = coursePaymentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Kurs to'lovi topilmadi"));

        coursePayment.setStatus(status);

        CoursePayment updatedPayment = coursePaymentRepository.save(coursePayment);
        return mapToResponse(updatedPayment);
    }

    @Override
    public boolean hasPaidForCourse(UUID studentId, UUID courseId) {
        return coursePaymentRepository.existsPaidPayment(studentId, courseId);
    }

    @Override
    @Transactional
    public void refundCoursePayment(UUID id) {
        CoursePayment coursePayment = coursePaymentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Kurs to'lovi topilmadi"));

        coursePayment.setStatus(CoursePaymentStatus.REFUNDED);
        coursePaymentRepository.save(coursePayment);
    }

    @Override
    public List<CoursePaymentResponse> getPaidCoursesByStudent(UUID studentId) {
        return coursePaymentRepository.findPaidCoursesByStudentId(studentId)
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    private CoursePaymentResponse mapToResponse(CoursePayment coursePayment) {
        CoursePaymentResponse response = new CoursePaymentResponse();
        response.setId(coursePayment.getId());
        response.setStudentId(coursePayment.getStudent().getId());
        response.setStudentName(coursePayment.getStudent().getUser().getFirstname() + " " +
                coursePayment.getStudent().getUser().getLastname());
        response.setCourseId(coursePayment.getCourse().getId());
        response.setCourseName(coursePayment.getCourse().getName());
        response.setAmount(coursePayment.getAmount());
        response.setStatus(coursePayment.getStatus());

        if (coursePayment.getPayment() != null) {
            response.setPaymentId(coursePayment.getPayment().getId().toString());
        }

        response.setCreatedAt(coursePayment.getCreatedAt());
        return response;
    }
}
```

**8. application.yml konfiguratsiyasi:**

**src/main/resources/application.yml**

**9. Global Exception Handler:**

**src/main/java/uz/codebyz/onlinecoursebackend/payment/exception/GlobalExceptionHandler.java**
```java
package uz.codebyz.onlinecoursebackend.payment.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import uz.codebyz.onlinecoursebackend.common.ResponseDto;
import java.util.HashMap;
import java.util.Map;

                    @ControllerAdvice
                    public class GlobalExceptionHandler {

                        @ExceptionHandler(RuntimeException.class)
                        public ResponseEntity<ResponseDto<String>> handleRuntimeException(RuntimeException ex) {
                            ResponseDto<String> response = ResponseDto.error(ex.getMessage());
                            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
                        }

                        @ExceptionHandler(MethodArgumentNotValidException.class)
                        public ResponseEntity<ResponseDto<Map<String, String>>> handleValidationExceptions(
                                MethodArgumentNotValidException ex) {
                            Map<String, String> errors = new HashMap<>();
                            ex.getBindingResult().getAllErrors().forEach((error) -> {
                                String fieldName = ((FieldError) error).getField();
                                String errorMessage = error.getDefaultMessage();
                                errors.put(fieldName, errorMessage);
                            });

                            ResponseDto<Map<String, String>> response = ResponseDto.error("Validatsiya xatosi", errors);
                            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
                        }

                        @ExceptionHandler(Exception.class)
                        public ResponseEntity<ResponseDto<String>> handleGeneralException(Exception ex) {
                            ResponseDto<String> response = ResponseDto.error("Server xatosi: " + ex.getMessage());
                            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
                        }
                    }
```

                    Bu to'liq payment moduli professional darajada tayyor. Lombokdan butunlay voz kechildi, barcha getter/setterlar qo'lda yozildi. API endpointlar to'liq RESTful standartlarda ishlab chiqilgan.
*/
