package uz.codebyz.onlinecoursebackend.userDevice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import uz.codebyz.onlinecoursebackend.userDevice.entity.UserDevice;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserDeviceRepository extends JpaRepository<UserDevice, UUID> {
    boolean existsByUserIdAndDeviceId(UUID userId, String deviceId);

    long countByUserId(UUID userId);

    List<UserDevice> findAllByUserId(UUID userId);

    void deleteByUserIdAndDeviceId(UUID userId, String deviceId);

    Optional<UserDevice> findByUserIdAndDeviceId(UUID userId, String deviceId);
}
