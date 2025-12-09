package uz.codebyz.onlinecoursebackend.admin.rest;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import uz.codebyz.onlinecoursebackend.admin.device.service.DeviceService;
import uz.codebyz.onlinecoursebackend.common.ResponseDto;
import uz.codebyz.onlinecoursebackend.revokedToken.entity.RevokedToken;
import uz.codebyz.onlinecoursebackend.revokedToken.repository.RevokedTokenRepository;
import uz.codebyz.onlinecoursebackend.security.UserPrincipal;
import uz.codebyz.onlinecoursebackend.user.User;
import uz.codebyz.onlinecoursebackend.userDevice.repository.UserDeviceRepository;
import uz.codebyz.onlinecoursebackend.userDevice.service.UserDeviceService;

import java.util.UUID;

@RestController
@RequestMapping("/api/admin/device")
public class AdminUserDeviceRestController {
    private final DeviceService deviceService;
    private final UserDeviceService userDeviceService;
    private final UserDeviceRepository userDeviceRepository;
    private final RevokedTokenRepository revokedTokenRepository;

    public AdminUserDeviceRestController(DeviceService deviceService, UserDeviceService userDeviceService, UserDeviceRepository userDeviceRepository, RevokedTokenRepository revokedTokenRepository) {
        this.deviceService = deviceService;
        this.userDeviceService = userDeviceService;
        this.userDeviceRepository = userDeviceRepository;
        this.revokedTokenRepository = revokedTokenRepository;
    }

    @GetMapping("list")
    public ResponseEntity<?> getAllDevices(HttpServletRequest req) {
        return ResponseEntity.ok(new ResponseDto<>(true, "Ok", userDeviceService.getAllDevices(req)));
    }

    @GetMapping("get-all-by-user-id")
    public ResponseEntity<?> getAllDevicesByUserId(@RequestParam("user_id") UUID user_id, HttpServletRequest req) {
        return ResponseEntity.ok(new ResponseDto<>(true, "Ok", userDeviceService.getDevices(user_id, req)));
    }

    @GetMapping("list/pagination")
    public ResponseEntity<?> getAllDevices(HttpServletRequest req, @RequestParam("page") int page, @RequestParam("size") int size) {
        return ResponseEntity.ok(new ResponseDto<>(true, "Ok", userDeviceService.getAllDevices(req, page, size)));
    }


    @Transactional
    @DeleteMapping("/delete-device/{deviceId}")
    public ResponseDto<?> deleteDevice(@AuthenticationPrincipal UserPrincipal userPrincipal,
                                       @PathVariable("deviceId") String deviceId,
                                       HttpServletRequest request) {
        // 1. HOZIRGI device emasligini tekshiramiz
        User user = userPrincipal.getUser();
        String currentDeviceId = DigestUtils.sha256Hex(
                request.getHeader("User-Agent") + "-" + request.getRemoteAddr()
        );

        if (!deviceId.equals(currentDeviceId)) {
            if (userDeviceRepository.findByUserIdAndDeviceId(user.getId(), deviceId).isEmpty()) {
                return new ResponseDto<>(false, "Device not found");
            }
            // 2. Yangi: Userning tokenini BLOCK qilish (logout qilish)
            String authHeader = request.getHeader("Authorization");
            if (authHeader != null && authHeader.startsWith("Bearer ")) {
                String token = authHeader.substring(7);
                RevokedToken revoked = new RevokedToken();
                revoked.setToken(token);
                revokedTokenRepository.save(revoked);
            }
            // 3. Bazadan device’ni o‘chiramiz
            userDeviceRepository.deleteByUserIdAndDeviceId(user.getId(), deviceId);

            return ResponseDto.ok("Device removed and user logged out");
        }
        return ResponseDto.error("O‘z qurilmangizni bu endpoint bilan o‘chirolmaysiz", "FORBIDDEN");
    }
}
