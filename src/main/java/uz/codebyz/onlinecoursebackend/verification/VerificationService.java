package uz.codebyz.onlinecoursebackend.verification;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import uz.codebyz.onlinecoursebackend.user.User;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.concurrent.ThreadLocalRandom;

@Service
public class VerificationService {

    private final VerificationCodeRepository verificationCodeRepository;
    private final long verificationLifetimeMs;

    public VerificationService(VerificationCodeRepository verificationCodeRepository,
                               @Value("${verified.email.time.milli.second}") long verificationLifetimeMs) {
        this.verificationCodeRepository = verificationCodeRepository;
        this.verificationLifetimeMs = verificationLifetimeMs;
    }

    @Transactional
    public VerificationCode createVerification(User user, VerificationType type, String targetEmail) {
        VerificationCode verificationCode = new VerificationCode();
        verificationCode.setUser(user);
        verificationCode.setType(type);
        verificationCode.setCode(generateCode());
        verificationCode.setExpiresAt(LocalDateTime.now().plusNanos(verificationLifetimeMs * 1_000_000));
        verificationCode.setTargetEmail(targetEmail);
        verificationCode.setUsed(false);
        return verificationCodeRepository.save(verificationCode);
    }

    @Transactional
    public Optional<VerificationCode> validateCode(User user, VerificationType type, String code, String targetEmail) {
        Optional<VerificationCode> optional = verificationCodeRepository.findByUserAndTypeAndCodeAndUsedIsFalse(user, type, code);
        if (optional.isEmpty()) {
            return Optional.empty();
        }
        VerificationCode verification = optional.get();
        if (verification.getExpiresAt().isBefore(LocalDateTime.now())) {
            return Optional.empty();
        }
        if (targetEmail != null && verification.getTargetEmail() != null && !verification.getTargetEmail().equalsIgnoreCase(targetEmail)) {
            return Optional.empty();
        }
        verification.setUsed(true);
        verificationCodeRepository.save(verification);
        return Optional.of(verification);
    }

    private String generateCode() {
        int code = ThreadLocalRandom.current().nextInt(100000, 999999);
        return String.format("%06d", code);
    }
}
