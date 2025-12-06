package uz.codebyz.onlinecoursebackend.verification;

import org.springframework.data.jpa.repository.JpaRepository;
import uz.codebyz.onlinecoursebackend.user.User;

import java.util.Optional;
import java.util.UUID;

public interface VerificationCodeRepository extends JpaRepository<VerificationCode, UUID> {
    Optional<VerificationCode> findTopByUserAndTypeAndUsedIsFalseOrderByCreatedAtDesc(User user, VerificationType type);

    Optional<VerificationCode> findByUserAndTypeAndCodeAndUsedIsFalse(User user, VerificationType type, String code);
}
