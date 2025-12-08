package uz.codebyz.onlinecoursebackend.userDevice.service;

import jakarta.servlet.http.HttpServletRequest;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.stereotype.Service;
import uz.codebyz.onlinecoursebackend.helper.CurrentTime;
import uz.codebyz.onlinecoursebackend.userDevice.dt.UserDeviceResponse;
import uz.codebyz.onlinecoursebackend.userDevice.repository.UserDeviceRepository;
import uz.codebyz.onlinecoursebackend.userDevice.entity.UserDevice;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service

public class UserDeviceService {

    private final UserDeviceRepository userDeviceRepository;

    public UserDeviceService(UserDeviceRepository userDeviceRepository) {
        this.userDeviceRepository = userDeviceRepository;
    }

    public List<UserDeviceResponse> getDevices(UUID userId, HttpServletRequest request) {

        String currentDeviceId = DigestUtils.sha256Hex(
                request.getHeader("User-Agent") + "-" + request.getRemoteAddr()
        );

        List<UserDevice> devices = userDeviceRepository.findAllByUserId(userId);

        return devices.stream()
                .map(device -> {
                    UserDeviceResponse dto = new UserDeviceResponse();
                    dto.setId(device.getId());
                    dto.setDeviceId(device.getDeviceId());
                    dto.setUserAgent(device.getUserAgent());
                    dto.setIpAddress(device.getIpAddress());
                    dto.setLastActive(device.getLastActive());
                    dto.setCreated(device.getCreatedAt());

                    // Hozir ishlatilayotgan qurilma shu bo'lsa → true
                    dto.setMe(device.getDeviceId().equals(currentDeviceId));

                    return dto;
                })
                .toList();
    }


    public boolean isUserOnline(UUID userId) {
        return userDeviceRepository.findAllByUserId(userId)
                .stream()
                .anyMatch(device ->
                        Duration.between(device.getLastActive(), LocalDateTime.now()).toMinutes() < 2
                );
    }

    public String getLastSeen(UUID userId) {
        return userDeviceRepository.findAllByUserId(userId)
                .stream()
                .map(UserDevice::getLastActive)
                .max(LocalDateTime::compareTo)
                .map(this::formatDuration)
                .orElse("Hech qachon online bo‘lmagan.");
    }


    private String formatDuration(LocalDateTime last) {
        Duration diff = Duration.between(last, CurrentTime.currentTime());

        long seconds = diff.getSeconds();
        long minutes = diff.toMinutes();
        long hours = diff.toHours();
        long days = diff.toDays();

        // 1) Online (0–5 seconds)
        if (seconds <= 5) return "Online";

        // 2) 1 daqiqadan kam
        if (seconds < 60) return seconds + " soniya oldin";

        // 3) 1 soatdan kam
        if (minutes < 60) return minutes + " daqiqa oldin";

        // 4) 24 soatdan kam
        if (hours < 24) return hours + " soat oldin";

        // 5) undan yuqori
        return days + " kun oldin";
    }
}
