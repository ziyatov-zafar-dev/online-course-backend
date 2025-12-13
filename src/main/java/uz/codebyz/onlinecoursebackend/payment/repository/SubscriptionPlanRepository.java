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