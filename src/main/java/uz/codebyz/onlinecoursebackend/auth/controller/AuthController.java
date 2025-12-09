package uz.codebyz.onlinecoursebackend.auth.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import uz.codebyz.onlinecoursebackend.auth.dto.*;
import uz.codebyz.onlinecoursebackend.auth.service.AuthService;
import uz.codebyz.onlinecoursebackend.auth.service.GeminiService;
import uz.codebyz.onlinecoursebackend.common.ApiResponse;
import uz.codebyz.onlinecoursebackend.common.ResponseDto;
import uz.codebyz.onlinecoursebackend.revokedToken.entity.RevokedToken;
import uz.codebyz.onlinecoursebackend.revokedToken.repository.RevokedTokenRepository;
import uz.codebyz.onlinecoursebackend.security.UserPrincipal;
import uz.codebyz.onlinecoursebackend.user.User;
import uz.codebyz.onlinecoursebackend.user.UserRepository;
import uz.codebyz.onlinecoursebackend.userDevice.entity.UserDevice;
import uz.codebyz.onlinecoursebackend.userDevice.repository.UserDeviceRepository;
import uz.codebyz.onlinecoursebackend.userDevice.service.UserDeviceService;

import java.util.Optional;

@RestController
@RequestMapping("/api/auth")
@Tag(name = "Auth", description = "Sign-up, sign-in, verification, password reset, refresh-token va username tekshirish")
public class AuthController {

    private final AuthService authService;
    private final GeminiService geminiService;
    private final UserDeviceRepository userDeviceRepository;
    private final RevokedTokenRepository revokedTokenRepository;
    private final UserDeviceService userDeviceService;
    private final UserRepository userRepository;

    public AuthController(AuthService authService, GeminiService geminiService, UserDeviceRepository userDeviceRepository, RevokedTokenRepository revokedTokenRepository, UserDeviceService userDeviceService, UserRepository userRepository) {
        this.authService = authService;
        this.geminiService = geminiService;
        this.userDeviceRepository = userDeviceRepository;
        this.revokedTokenRepository = revokedTokenRepository;
        this.userDeviceService = userDeviceService;
        this.userRepository = userRepository;
    }

    @Operation(summary = "Email/parol bilan ro'yxatdan o'tish (2 bosqich)", description = "User yaratadi, tasdiqlash kodi emailga yuboriladi")
    @PostMapping("/sign-up")
    public ResponseEntity<ApiResponse<Object>> signUp(@Valid @RequestBody SignUpRequest request) {
        ApiResponse<Object> response = authService.signUp(request);
        HttpStatus status = response.isSuccess() ? HttpStatus.OK : HttpStatus.BAD_REQUEST;
        return ResponseEntity.status(status).body(response);
    }

    @Operation(summary = "Sign-up kodi bilan tasdiqlash")
    @PostMapping("/sign-up/verify")
    public ResponseEntity<ApiResponse<?>> verifySignUp(@Valid @RequestBody SignUpVerifyRequest request, HttpServletRequest http) {
        ApiResponse<?> response = authService.verifySignUp(request, http);
        HttpStatus status = response.isSuccess() ? HttpStatus.OK : HttpStatus.BAD_REQUEST;
        return ResponseEntity.status(status).body(response);
    }

    @Operation(summary = "Email/parol bilan kirish (kod yuborish bosqichi)")
    @PostMapping("/sign-in")
    public ResponseEntity<ApiResponse<?>> signIn(@Valid @RequestBody SignInRequest request, HttpServletRequest http) {
        ApiResponse<?> response = authService.signIn(request, http);
        HttpStatus status;
        if (!response.isSuccess()) {
            status = response.getErrorCode() != null && response.getErrorCode().equals("ACCOUNT_NOT_VERIFIED") ? HttpStatus.BAD_REQUEST : HttpStatus.UNAUTHORIZED;
        } else {
            status = HttpStatus.OK;
        }
        return ResponseEntity.status(status).body(response);
    }

    @Operation(summary = "Kodni tasdiqlab JWT olish")
    @PostMapping("/sign-in/verify")
    public ResponseEntity<ApiResponse<?>> verifySignIn(@Valid @RequestBody SignInVerifyRequest request, HttpServletRequest http) {
        ApiResponse<?> response = authService.verifySignIn(request, http);
        HttpStatus status = response.isSuccess() ? HttpStatus.OK : HttpStatus.BAD_REQUEST;
        return ResponseEntity.status(status).body(response);
    }

    @Operation(summary = "Refresh token orqali yangi access/refresh olish")
    @PostMapping("/refresh-token")
    public ResponseEntity<ApiResponse<AuthTokensResponse>> refreshToken(@Valid @RequestBody RefreshTokenRequest request) {
        ApiResponse<AuthTokensResponse> response = authService.refreshToken(request);
        HttpStatus status = response.isSuccess() ? HttpStatus.OK : HttpStatus.UNAUTHORIZED;
        return ResponseEntity.status(status).body(response);
    }

    @Operation(summary = "Parolni unutdim – kod yuborish")
    @PostMapping("/forgot-password")
    public ResponseEntity<ApiResponse<Object>> forgotPassword(@Valid @RequestBody ForgotPasswordRequest request) {
        return ResponseEntity.ok(authService.forgotPassword(request));
    }

    @GetMapping("/me/devices")
    public ResponseDto<?> myDevices(@AuthenticationPrincipal UserPrincipal principal, HttpServletRequest http) {
        return ResponseDto.ok("Ok", userDeviceService.getDevices(principal.getUser().getId(), http));
    }

    @GetMapping("/my-devices-auth")
    public ResponseDto<?> myDevices(@RequestParam("gmail") String gmail, HttpServletRequest http) {
        Optional<User> uOp = userRepository.findByEmail(gmail);
        if (uOp.isEmpty()) {
            return new ResponseDto<>(false, "User not found");
        }
        User currentUser = uOp.get();
        return ResponseDto.ok("Ok", userDeviceService.getDevices(currentUser.getId(), http));
    }

    @Transactional
    @DeleteMapping("/me/delete-device/{deviceId}")
    public ResponseDto<?> deleteDevicesss(@AuthenticationPrincipal UserPrincipal userPrincipal, @PathVariable("deviceId") String deviceId, HttpServletRequest request) {

        User currentUser = userPrincipal.getUser();

        // HOZIRGI DEVICE ID
        String currentDeviceId = DigestUtils.sha256Hex(request.getHeader("User-Agent") + "-" + request.getRemoteAddr());

        // O‘chirilmoqchi bo‘lgan device obyektini DB’dan topamiz
        Optional<UserDevice> optionalDevice = userDeviceRepository.findByDeviceId(deviceId);
        if (optionalDevice.isEmpty()) {
            return new ResponseDto<>(false, "Device not found");
        }

        UserDevice device = optionalDevice.get();

        // 🔥 1. SHART: Device faqat o‘sha userga tegishli bo‘lishi kerak
        if (!device.getUserId().equals(currentUser.getId())) {
            return ResponseDto.error("Siz boshqa foydalanuvchining device'ini o‘chira olmaysiz", "FORBIDDEN");
        }

        // 🔥 2. SHART: O‘z qurilmasini o‘chira olmasin
        if (deviceId.equals(currentDeviceId)) {
            return ResponseDto.error("O‘z qurilmangizni bu endpoint bilan o‘chirolmaysiz", "FORBIDDEN");
        }

        // 🔥 3. Faqat shu device uchun tokenni bloklash
        String authHeader = request.getHeader("Authorization");
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7);
            RevokedToken revoked = new RevokedToken();
            revoked.setToken(token);
            revokedTokenRepository.save(revoked);
        }

        // 🔥 4. Device’ni o‘chiramiz
        userDeviceRepository.deleteById(device.getId());

        return ResponseDto.ok("Device removed successfully");
    }

    @Transactional
    @DeleteMapping("/me/delete-device-for-auth-user/{deviceId}")
    public ResponseDto<?> deleteDeviceBsyAuth(@RequestParam("gmail") String gmail, @PathVariable("deviceId") String deviceId, HttpServletRequest request) {
        Optional<User> uOp = userRepository.findByEmail(gmail);
        if (uOp.isEmpty()) {
            return new ResponseDto<>(false, "User not found");
        }
        User currentUser = uOp.get();
        String currentDeviceId = DigestUtils.sha256Hex(request.getHeader("User-Agent") + "-" + request.getRemoteAddr());

        // O‘chirilmoqchi bo‘lgan device obyektini DB’dan topamiz
        Optional<UserDevice> optionalDevice = userDeviceRepository.findByDeviceId(deviceId);
        if (optionalDevice.isEmpty()) {
            return new ResponseDto<>(false, "Device not found");
        }

        UserDevice device = optionalDevice.get();

        // 🔥 1. SHART: Device faqat o‘sha userga tegishli bo‘lishi kerak
        if (!device.getUserId().equals(currentUser.getId())) {
            return ResponseDto.error("Siz boshqa foydalanuvchining device'ini o‘chira olmaysiz", "FORBIDDEN");
        }

        // 🔥 2. SHART: O‘z qurilmasini o‘chira olmasin
        if (deviceId.equals(currentDeviceId)) {
            return ResponseDto.error("O‘z qurilmangizni bu endpoint bilan o‘chirolmaysiz", "FORBIDDEN");
        }

        // 🔥 3. Faqat shu device uchun tokenni bloklash
        String authHeader = request.getHeader("Authorization");
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7);
            RevokedToken revoked = new RevokedToken();
            revoked.setToken(token);
            revokedTokenRepository.save(revoked);
        }

        // 🔥 4. Device’ni o‘chiramiz
        userDeviceRepository.deleteById(device.getId());

        return ResponseDto.ok("Device removed successfully");
    }


    @Operation(summary = "Parolni tiklash – kod + yangi parol")
    @PostMapping("/reset-password")
    public ResponseEntity<ApiResponse<Object>> resetPassword(@Valid @RequestBody ResetPasswordRequest request) {
        ApiResponse<Object> response = authService.resetPassword(request);
        HttpStatus status = response.isSuccess() ? HttpStatus.OK : HttpStatus.BAD_REQUEST;
        return ResponseEntity.status(status).body(response);
    }

    @Operation(summary = "Username yaroqliligini tekshirish", description = "Regex: harf bilan boshlanadi, 5-32 belgi, lotin/raqam/_. Token talab qilinadi (STUDENT/TEACHER/ADMIN). O‘zining username bo‘lsa ham yaroqli.")
    @GetMapping("/validate-username")
    @PreAuthorize("hasAnyRole('STUDENT','TEACHER','ADMIN')")
    public ResponseEntity<ApiResponse<Object>> validateUsername(@AuthenticationPrincipal UserPrincipal principal, @RequestParam("username") String username) {
        ApiResponse<Object> response = authService.validateUsername(username, principal != null ? principal.getUser() : null);
        HttpStatus status = response.isSuccess() ? HttpStatus.OK : HttpStatus.BAD_REQUEST;
        return ResponseEntity.status(status).body(response);
    }
}
