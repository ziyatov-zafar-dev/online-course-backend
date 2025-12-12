package uz.codebyz.onlinecoursebackend.userDevice.service;

import jakarta.servlet.http.HttpServletRequest;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import uz.codebyz.onlinecoursebackend.helper.CurrentTime;
import uz.codebyz.onlinecoursebackend.userDevice.dt.UserDeviceResponse;
import uz.codebyz.onlinecoursebackend.userDevice.repository.UserDeviceRepository;
import uz.codebyz.onlinecoursebackend.userDevice.entity.UserDevice;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Service

public class UserDeviceService {

    private final UserDeviceRepository userDeviceRepository;
    private final IpLocationService ipLocationService;

    public UserDeviceService(UserDeviceRepository userDeviceRepository, IpLocationService ipLocationService) {
        this.userDeviceRepository = userDeviceRepository;
        this.ipLocationService = ipLocationService;
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
                    dto.setUserId(device.getUserId());
                    dto.setDeviceId(device.getDeviceId());
                    dto.setUserAgent(device.getUserAgent());
                    dto.setIpAddress(device.getIpAddress());
                    dto.setLastActive(device.getLastActive());
                    dto.setCreated(device.getCreatedAt());
                    // Hozir ishlatilayotgan qurilma shu bo'lsa → true
                    dto.setMe(device.getDeviceId().equals(currentDeviceId));
                    dto.setLocation(ipLocationService.getLocation(device.getIpAddress()));
                    return dto;
                })
                .toList();
    }

    public List<UserDeviceResponse> getAllDevices(HttpServletRequest request, int page, int size) {

        // HOZIRGI QURILMA IDENTIFIKATORI
        String currentDeviceId = DigestUtils.sha256Hex(
                request.getHeader("User-Agent") + "-" + request.getRemoteAddr()
        );

        // PAGE VA SIZE BO‘YICHA DEVICE OLISh
        Pageable pageable = PageRequest.of(page, size);
        Page<UserDevice> devices = userDeviceRepository.findAll(pageable);

        // DTO GA MAP QILAMIZ
        return devices.getContent().stream()
                .map(device -> {
                    UserDeviceResponse dto = new UserDeviceResponse();
                    dto.setId(device.getId());
                    dto.setDeviceId(device.getDeviceId());
                    dto.setUserAgent(device.getUserAgent());
                    dto.setIpAddress(device.getIpAddress());
                    dto.setLastActive(device.getLastActive());
                    dto.setCreated(device.getCreatedAt());
                    dto.setUserId(device.getUserId());
                    dto.setLocation(ipLocationService.getLocation(device.getIpAddress()));
                    dto.setMe(device.getDeviceId().equals(currentDeviceId));
                    return dto;
                })
                .toList();
    }

    public List<UserDeviceResponse> getAllDevices(HttpServletRequest request) {

        // HOZIRGI QURILMA IDENTIFIKATORI
        String currentDeviceId = DigestUtils.sha256Hex(
                request.getHeader("User-Agent") + "-" + request.getRemoteAddr()
        );

        // BARCHA QURILMALAR
        List<UserDevice> devices = userDeviceRepository.findAll();

        // DTO GA MAP QILAMIZ
        return devices.stream()
                .map(device -> {
                    UserDeviceResponse dto = new UserDeviceResponse();
                    dto.setId(device.getId());
                    dto.setDeviceId(device.getDeviceId());
                    dto.setUserAgent(device.getUserAgent());
                    dto.setIpAddress(device.getIpAddress());
                    dto.setLastActive(device.getLastActive());
                    dto.setCreated(device.getCreatedAt());
                    dto.setUserId(device.getUserId());
                    dto.setLocation(ipLocationService.getLocation(device.getIpAddress()));
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
                .filter(Objects::nonNull)
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
