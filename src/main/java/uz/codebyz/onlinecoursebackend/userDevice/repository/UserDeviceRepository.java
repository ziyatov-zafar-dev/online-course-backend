package uz.codebyz.onlinecoursebackend.userDevice.repository;

import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import uz.codebyz.onlinecoursebackend.userDevice.entity.UserDevice;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserDeviceRepository extends JpaRepository<UserDevice, UUID> {
    boolean existsByUserIdAndDeviceId(UUID userId, String deviceId);

    long countByUserId(UUID userId);

    List<UserDevice> findAllByUserId(UUID userId);

    /*void deleteByUserIdAndDeviceId(UUID userId, String deviceId);*/
    @Transactional
    @Modifying
    @Query("DELETE FROM UserDevice ud WHERE ud.userId = :userId AND ud.deviceId = :deviceId")
    void deleteByUserIdAndDeviceId(@Param("userId") UUID userId,
                                   @Param("deviceId") String deviceId);
    Optional<UserDevice> findByUserIdAndDeviceId(UUID userId, String deviceId);

    Optional<UserDevice> findByDeviceId(String deviceId);
}
