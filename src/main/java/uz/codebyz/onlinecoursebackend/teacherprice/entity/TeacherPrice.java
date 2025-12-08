package uz.codebyz.onlinecoursebackend.teacherprice.entity;

import jakarta.persistence.*;

import java.math.BigDecimal;

@Entity
@Table(name = "teacher_prices")
public class TeacherPrice {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Short id;
    @Column(precision = 19, scale = 2)
    private BigDecimal price;
    private Boolean free;

    public Boolean getFree() {
        return free;
    }
    public void setFree(Boolean free) {
        this.free = free;
    }

    public Short getId() {
        return id;
    }

    public void setId(Short id) {
        this.id = id;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }
}
