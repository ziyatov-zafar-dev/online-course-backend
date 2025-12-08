package uz.codebyz.onlinecoursebackend.config;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import uz.codebyz.onlinecoursebackend.helper.CurrentTime;

import uz.codebyz.onlinecoursebackend.revokedToken.repository.RevokedTokenRepository;
import uz.codebyz.onlinecoursebackend.security.jwt.JwtService;
import uz.codebyz.onlinecoursebackend.user.UserRepository;
import uz.codebyz.onlinecoursebackend.userDevice.repository.UserDeviceRepository;
import uz.codebyz.onlinecoursebackend.userDevice.entity.UserDevice;


import java.io.IOException;
import java.util.Optional;
import java.util.UUID;

/*@Component
public class DeviceActivityFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final UserRepository userRepository;
    private final UserDeviceRepository userDeviceRepository;

    public DeviceActivityFilter(JwtService jwtService,
                                UserRepository userRepository,
                                UserDeviceRepository userDeviceRepository) {
        this.jwtService = jwtService;
        this.userRepository = userRepository;
        this.userDeviceRepository = userDeviceRepository;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        try {
            String authHeader = request.getHeader("Authorization");

            if (authHeader != null && authHeader.startsWith("Bearer ")) {

                String token = authHeader.substring(7);

                // TOKEN DAN EMAILNI OLYAPMIZ
                String email = jwtService.extractEmail(token);

                // EMAIL BO'YICHA FOYDALANUVCHI BORLIGINI TEKSHIRAMIZ
                var userOptional = userRepository.findByEmail(email);

                if (userOptional.isPresent()) {
                    var user = userOptional.get();

                    // Sizning jwtService.isTokenValid(token, user) methodingizga moslash
                    if (jwtService.isTokenValid(token, user)) {

                        // device ID generatsiya
                        String deviceId = DigestUtils.sha256Hex(
                                request.getHeader("User-Agent") + "-" + request.getRemoteAddr()
                        );

                        Optional<UserDevice> optional = userDeviceRepository
                                .findByUserIdAndDeviceId(user.getId(), deviceId);

                        if (optional.isPresent()) {
                            UserDevice device = optional.get();
                            device.setLastActive(CurrentTime.currentTime());
                            userDeviceRepository.save(device);
                        }
                    }
                }
            }
        } catch (Exception ignored) {
            // token yaroqsiz yoki email topilmasa jim o'tadi
        }

        filterChain.doFilter(request, response);
    }
}*/


@Component
public class DeviceActivityFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final UserRepository userRepository;
    private final UserDeviceRepository userDeviceRepository;
    private final RevokedTokenRepository revokedTokenRepository; // 🔥 YANGI

    public DeviceActivityFilter(JwtService jwtService,
                                UserRepository userRepository,
                                UserDeviceRepository userDeviceRepository,
                                RevokedTokenRepository revokedTokenRepository) {
        this.jwtService = jwtService;
        this.userRepository = userRepository;
        this.userDeviceRepository = userDeviceRepository;
        this.revokedTokenRepository = revokedTokenRepository; // 🔥 YANGI
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {
        try {
            String authHeader = request.getHeader("Authorization");

            if (authHeader != null && authHeader.startsWith("Bearer ")) {

                String token = authHeader.substring(7);

                // 🔥 1. AVVAL TOKEN BLOCK QILINGANMI — TEKSHIRAMIZ
                if (revokedTokenRepository.existsById(token)) {
                    response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                    response.getWriter().write("Session expired");
                    return;
                }

                // TOKEN DAN EMAILNI OLYAPMIZ
                String email = jwtService.extractEmail(token);

                // EMAIL BO'YICHA FOYDALANUVCHI BORLIGINI TEKSHIRAMIZ
                var userOptional = userRepository.findByEmail(email);

                if (userOptional.isPresent()) {
                    var user = userOptional.get();

                    // Sizning jwtService.isTokenValid(token, user) methodingizga moslash
                    if (jwtService.isTokenValid(token, user)) {

                        // device ID generatsiya
                        String deviceId = DigestUtils.sha256Hex(
                                request.getHeader("User-Agent") + "-" + request.getRemoteAddr()
                        );

                        Optional<UserDevice> optional = userDeviceRepository
                                .findByUserIdAndDeviceId(user.getId(), deviceId);

                        if (optional.isPresent()) {
                            UserDevice device = optional.get();
                            device.setLastActive(CurrentTime.currentTime());
                            userDeviceRepository.save(device);
                            userRepository.save(user);
                        }
                    }
                }
            }
        } catch (Exception ignored) {
            // token yaroqsiz yoki email topilmasa jim o'tadi
        }

        filterChain.doFilter(request, response);
    }
}

