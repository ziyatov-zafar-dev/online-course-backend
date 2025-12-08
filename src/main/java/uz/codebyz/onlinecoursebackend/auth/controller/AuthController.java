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
import uz.codebyz.onlinecoursebackend.userDevice.repository.UserDeviceRepository;
import uz.codebyz.onlinecoursebackend.userDevice.service.UserDeviceService;

@RestController
@RequestMapping("/api/auth")
@Tag(name = "Auth", description = "Sign-up, sign-in, verification, password reset, refresh-token va username tekshirish")
public class AuthController {

    private final AuthService authService;
    private final GeminiService geminiService;
    private final UserDeviceRepository userDeviceRepository;
    private final RevokedTokenRepository revokedTokenRepository;
    private final UserDeviceService userDeviceService;

    public AuthController(AuthService authService, GeminiService geminiService, UserDeviceRepository userDeviceRepository, RevokedTokenRepository revokedTokenRepository, UserDeviceService userDeviceService) {
        this.authService = authService;
        this.geminiService = geminiService;
        this.userDeviceRepository = userDeviceRepository;
        this.revokedTokenRepository = revokedTokenRepository;
        this.userDeviceService = userDeviceService;
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
    public ResponseEntity<ApiResponse<Object>> verifySignUp(@Valid @RequestBody SignUpVerifyRequest request) {
        ApiResponse<Object> response = authService.verifySignUp(request);
        HttpStatus status = response.isSuccess() ? HttpStatus.OK : HttpStatus.BAD_REQUEST;
        return ResponseEntity.status(status).body(response);
    }

    @Operation(summary = "Email/parol bilan kirish (kod yuborish bosqichi)")
    @PostMapping("/sign-in")
    public ResponseEntity<ApiResponse<Object>> signIn(@Valid @RequestBody SignInRequest request) {
        ApiResponse<Object> response = authService.signIn(request);
        HttpStatus status;
        if (!response.isSuccess()) {
            status = response.getErrorCode() != null && response.getErrorCode().equals("ACCOUNT_NOT_VERIFIED")
                    ? HttpStatus.BAD_REQUEST : HttpStatus.UNAUTHORIZED;
        } else {
            status = HttpStatus.OK;
        }
        return ResponseEntity.status(status).body(response);
    }

    @Operation(summary = "Kodni tasdiqlab JWT olish")
    @PostMapping("/sign-in/verify")
    public ResponseEntity<ApiResponse<AuthTokensResponse>> verifySignIn(@Valid @RequestBody SignInVerifyRequest request, HttpServletRequest http) {
        ApiResponse<AuthTokensResponse> response = authService.verifySignIn(request, http);
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

    @Transactional
    @DeleteMapping("/me/devices/{deviceId}")
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
    public ResponseEntity<ApiResponse<Object>> validateUsername(@AuthenticationPrincipal UserPrincipal principal,
                                                                @RequestParam("username") String username) {
        ApiResponse<Object> response = authService.validateUsername(username, principal != null ? principal.getUser() : null);
        HttpStatus status = response.isSuccess() ? HttpStatus.OK : HttpStatus.BAD_REQUEST;
        return ResponseEntity.status(status).body(response);
    }
}
