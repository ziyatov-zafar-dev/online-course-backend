package uz.codebyz.onlinecoursebackend.device_login_attempts.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import uz.codebyz.onlinecoursebackend.device_login_attempts.entity.DeviceLoginAttempt;

import java.util.Optional;
import java.util.UUID;

public interface DeviceLoginAttemptRepository extends JpaRepository<DeviceLoginAttempt, UUID> {
    Optional<DeviceLoginAttempt> findByDeviceId(String deviceId);
}
