package uz.codebyz.onlinecoursebackend.student.entity;

import jakarta.persistence.*;
import uz.codebyz.onlinecoursebackend.promocode.entity.PromoCodeUsage;
import uz.codebyz.onlinecoursebackend.user.User;

import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "students")
public class Student {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(columnDefinition = "uuid")
    private UUID id;

    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;
    @OneToMany(mappedBy = "student", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<PromoCodeUsage> promoCodeUsages;

    public List<PromoCodeUsage> getPromoCodeUsages() {
        return promoCodeUsages;
    }

    public void setPromoCodeUsages(List<PromoCodeUsage> promoCodeUsages) {
        this.promoCodeUsages = promoCodeUsages;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
