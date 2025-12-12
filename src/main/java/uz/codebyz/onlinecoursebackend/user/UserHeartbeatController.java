package uz.codebyz.onlinecoursebackend.user;

import jakarta.servlet.http.HttpServletRequest;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import uz.codebyz.onlinecoursebackend.helper.CurrentTime;
import uz.codebyz.onlinecoursebackend.security.UserPrincipal;
import uz.codebyz.onlinecoursebackend.userDevice.entity.UserDevice;
import uz.codebyz.onlinecoursebackend.userDevice.repository.UserDeviceRepository;

@RestController
@RequestMapping("/api/user")
public class UserHeartbeatController {

    private final UserDeviceRepository userDeviceRepository;

    public UserHeartbeatController(UserDeviceRepository userDeviceRepository) {
        this.userDeviceRepository = userDeviceRepository;
    }

    @PostMapping("/heartbeat")
    public void heartbeat(HttpServletRequest request, @AuthenticationPrincipal UserPrincipal user) {
        String userAgent = request.getHeader("User-Agent");
        String ip = request.getRemoteAddr();
        String deviceId = DigestUtils.sha256Hex(userAgent + "-" + ip);
        UserDevice device = userDeviceRepository
                .findByUserIdAndDeviceId(user.getUser().getId(), deviceId)
                .orElseGet(() -> {
                    UserDevice d = new UserDevice();
                    d.setUserId(user.getUser().getId());
                    d.setDeviceId(deviceId);
                    return d;
                });
        device.setLastActive(CurrentTime.currentTime());
        userDeviceRepository.save(device);
    }
}