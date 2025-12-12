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
import java.util.Optional;
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

    private Optional<LocalDateTime> getLastActive(UUID userId) {
        return userDeviceRepository.findAllByUserId(userId)
                .stream()
                .map(UserDevice::getLastActive)
                .filter(Objects::nonNull)
                .max(LocalDateTime::compareTo);
    }

    public boolean isUserOnline(UUID userId) {
/*
        return userDeviceRepository.findAllByUserId(userId)
                .stream()
                .anyMatch(device ->
                        Duration.between(device.getLastActive(), LocalDateTime.now()).toMinutes() < 2
                );
*/
        return getLastActive(userId)
                .map(last ->
                        Duration.between(last, CurrentTime.currentTime()).toMinutes() < 3
                )
                .orElse(false);    }

    /*public String getLastSeen(UUID userId) {
        Optional<LocalDateTime> lastActiveOpt = getLastActive(userId);
*//*
        return userDeviceRepository.findAllByUserId(userId)
                .stream()
                .map(UserDevice::getLastActive)
                .filter(Objects::nonNull)
                .max(LocalDateTime::compareTo)
                .map(this::formatDuration)
                .orElse("Hech qachon online bo‘lmagan.");
*//*
        return userDeviceRepository.findAllByUserId(userId)
                .stream()
                .map(UserDevice::getLastActive)
                .filter(Objects::nonNull)
                .max(LocalDateTime::compareTo)
                .map(this::formatDuration).get();
    }*/
    public String getLastSeen(UUID userId) {

        Optional<LocalDateTime> lastActiveOpt = getLastActive(userId);

        if (lastActiveOpt.isEmpty()) {
            return "Hech qachon online bo‘lmagan.";
        }

        LocalDateTime lastActive = lastActiveOpt.get();

        // 🔥 3 daqiqa qoidasi
        if (Duration.between(lastActive, CurrentTime.currentTime()).toMinutes() < 3) {
            return "Online";
        }

        return formatDuration(lastActive);
    }


    /*private String formatDuration(LocalDateTime last) {
        Duration diff = Duration.between(last, CurrentTime.currentTime());

        long seconds = diff.getSeconds();
        long minutes = diff.toMinutes();
        long hours = diff.toHours();
        long days = diff.toDays();

        if (seconds < 60) return seconds + " soniya oldin";
        if (minutes < 60) return minutes + " daqiqa oldin";
        if (hours < 24) return hours + " soat oldin";
        return days + " kun oldin";
    }*/
    private String formatDuration(LocalDateTime last) {

        LocalDateTime now = CurrentTime.currentTime();
        Duration diff = Duration.between(last, now);

        long minutes = diff.toMinutes();
        long hours = diff.toHours();
        long days = diff.toDays();

        // 🔥 0–2 daqiqa → ONLINE
        if (minutes < 2) {
            return "Online";
        }

        // 🔹 Daqiqa
        if (minutes < 60) {
            return minutes + " daqiqa oldin online edi";
        }

        // 🔹 Soat
        if (hours < 24) {
            return hours + " soat oldin online edi";
        }

        // 🔹 Kun
        if (days < 7) {
            return days + " kun oldin online edi";
        }

        // 🔹 Hafta
        long weeks = days / 7;
        if (weeks < 4) {
            return weeks + " hafta oldin online edi";
        }

        // 🔹 Oy (taxminiy)
        long months = days / 30;
        if (months < 12) {
            return months + " oy oldin online edi";
        }

        // 🔹 Yil
        long years = days / 365;
        return years + " yil oldin online edi";
    }
    /*private String formatDuration(LocalDateTime last) {

        LocalDateTime now = CurrentTime.currentTime();
        Duration diff = Duration.between(last, now);

        long seconds = diff.getSeconds();
        long minutes = diff.toMinutes();
        long hours = diff.toHours();
        long days = diff.toDays();

        // 🔥 0–1 sekund → ONLINE
        if (seconds <= 1) {
            return "Online";
        }

        // 🔹 Sekund (2–59)
        if (seconds < 60) {
            return seconds + " sekund oldin online edi";
        }

        // 🔹 Daqiqa (1–59)
        if (minutes < 60) {
            return minutes + " daqiqa oldin online edi";
        }

        // 🔹 Soat (1–23)
        if (hours < 24) {
            return hours + " soat oldin online edi";
        }

        // 🔹 Kun (1–6)
        if (days < 7) {
            return days + " kun oldin online edi";
        }

        // 🔹 Hafta (1–3)
        long weeks = days / 7;
        if (weeks < 4) {
            return weeks + " hafta oldin online edi";
        }

        // 🔹 Oy (1–11, taxminiy)
        long months = days / 30;
        if (months < 12) {
            return months + " oy oldin online edi";
        }

        // 🔹 Yil
        long years = days / 365;
        return years + " yil oldin online edi";
    }*/


}
