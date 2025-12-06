package uz.codebyz.onlinecoursebackend.promocode.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import uz.codebyz.onlinecoursebackend.promocode.entity.PromoCodeUsage;

import java.util.UUID;

public interface PromoCodeUsageRepository extends JpaRepository<PromoCodeUsage, UUID> {

}