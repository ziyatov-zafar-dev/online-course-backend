package uz.codebyz.onlinecoursebackend.userDevice.entity;

import jakarta.persistence.*;
import uz.codebyz.onlinecoursebackend.helper.CurrentTime;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "user_devices",
        uniqueConstraints = @UniqueConstraint(columnNames = {"user_id", "device_id"}))
public class UserDevice {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(columnDefinition = "uuid")
    private UUID id;

    @Column(name = "user_id", nullable = false)
    private UUID userId;

    @Column(unique = true)
    private String deviceId;
    private String userAgent;
    @Column(nullable = false)
    private String ipAddress;
    private LocalDateTime createdAt = CurrentTime.currentTime();
    private LocalDateTime lastActive = CurrentTime.currentTime();

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public UUID getUserId() {
        return userId;
    }

    public void setUserId(UUID userId) {
        this.userId = userId;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public String getUserAgent() {
        return userAgent;
    }

    public void setUserAgent(String userAgent) {
        this.userAgent = userAgent;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getLastActive() {
        return lastActive;
    }

    public void setLastActive(LocalDateTime lastActive) {
        this.lastActive = lastActive;
    }
}
