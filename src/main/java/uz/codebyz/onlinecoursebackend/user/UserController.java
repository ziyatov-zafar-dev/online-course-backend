package uz.codebyz.onlinecoursebackend.user;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.http.HttpRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import uz.codebyz.onlinecoursebackend.auth.dto.*;
import uz.codebyz.onlinecoursebackend.auth.service.AuthService;
import uz.codebyz.onlinecoursebackend.common.ApiResponse;
import uz.codebyz.onlinecoursebackend.common.ResponseDto;
import uz.codebyz.onlinecoursebackend.revokedToken.entity.RevokedToken;
import uz.codebyz.onlinecoursebackend.revokedToken.repository.RevokedTokenRepository;
import uz.codebyz.onlinecoursebackend.security.UserPrincipal;
import uz.codebyz.onlinecoursebackend.userDevice.entity.UserDevice;
import uz.codebyz.onlinecoursebackend.userDevice.repository.UserDeviceRepository;
import uz.codebyz.onlinecoursebackend.userDevice.service.UserDeviceService;

import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/users/me")
@Tag(name = "User", description = "Autentifikatsiyadan o‘tgan foydalanuvchi amallari")
public class UserController {

    private final AuthService authService;
    private final ProfileImageService profileImageService;
    private final UserDeviceService userDeviceService;
    private final RevokedTokenRepository revokedTokenRepository;
    private final UserDeviceRepository userDeviceRepository;

    public UserController(AuthService authService, ProfileImageService profileImageService, UserDeviceService userDeviceService, RevokedTokenRepository revokedTokenRepository, UserDeviceRepository userDeviceRepository) {
        this.authService = authService;
        this.profileImageService = profileImageService;
        this.userDeviceService = userDeviceService;
        this.revokedTokenRepository = revokedTokenRepository;
        this.userDeviceRepository = userDeviceRepository;
    }

    @Operation(summary = "Mening ma’lumotlarimni olish")
    @GetMapping
    public ResponseEntity<ApiResponse<UserResponse>> getMe(@AuthenticationPrincipal UserPrincipal principal) {
        return ResponseEntity.ok(authService.getMe(principal.getUser()));
    }

    @Operation(summary = "Profilni yangilash (ism, familiya, tug‘ilgan sana, bio, social)")
    @PatchMapping("/profile")
    public ResponseEntity<ApiResponse<UserResponse>> updateProfile(@AuthenticationPrincipal UserPrincipal principal,
                                                                   @RequestBody UpdateProfileRequest request) {
        return ResponseEntity.ok(authService.updateProfile(principal.getUser(), request));
    }

    @Operation(summary = "Parolni o‘zgartirish")
    @PostMapping("/change-password")
    public ResponseEntity<ApiResponse<Object>> changePassword(@AuthenticationPrincipal UserPrincipal principal,
                                                              @Valid @RequestBody ChangePasswordRequest request) {
        ApiResponse<Object> response = authService.changePassword(principal.getUser(), request);
        HttpStatus status = response.isSuccess() ? HttpStatus.OK : HttpStatus.BAD_REQUEST;
        return ResponseEntity.status(status).body(response);
    }

    @Operation(summary = "Username ni o‘zgartirish")
    @PostMapping("/change-username")
    public ResponseEntity<ApiResponse<UserResponse>> changeUsername(@AuthenticationPrincipal UserPrincipal principal,
                                                                    @Valid @RequestBody ChangeUsernameRequest request) {
        ApiResponse<UserResponse> response = authService.changeUsername(principal.getUser(), request);
        HttpStatus status = response.isSuccess() ? HttpStatus.OK : HttpStatus.BAD_REQUEST;
        return ResponseEntity.status(status).body(response);
    }

    @Operation(summary = "Yangi email uchun kod yuborish")
    @PostMapping("/change-email/request")
    public ResponseEntity<ApiResponse<Object>> requestChangeEmail(@AuthenticationPrincipal UserPrincipal principal,
                                                                  @Valid @RequestBody ChangeEmailRequest request) {
        ApiResponse<Object> response = authService.requestChangeEmail(principal.getUser(), request);
        HttpStatus status = response.isSuccess() ? HttpStatus.OK : HttpStatus.BAD_REQUEST;
        return ResponseEntity.status(status).body(response);
    }

    @Operation(summary = "Yangi emailni kod orqali tasdiqlash")
    @PostMapping("/change-email/verify")
    public ResponseEntity<ApiResponse<UserResponse>> verifyChangeEmail(
            @AuthenticationPrincipal UserPrincipal principal,
            @Valid @RequestBody ChangeEmailVerifyRequest request
    ) {
        ApiResponse<UserResponse> response = authService.verifyChangeEmail(principal.getUser(), request);
        HttpStatus status = response.isSuccess() ? HttpStatus.OK : HttpStatus.BAD_REQUEST;
        return ResponseEntity.status(status).body(response);
    }

    @Operation(summary = "Profil rasmi yuklash")
    @PostMapping(value = "/profile/image", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ApiResponse<UserResponse>> uploadProfileImage(@AuthenticationPrincipal UserPrincipal principal,
                                                                        @RequestParam("file") MultipartFile file) {
        UserResponse data = profileImageService.upload(file, principal.getUser());
        return ResponseEntity.ok(ApiResponse.ok("Profil rasmi yuklandi.", data));
    }
    @PostMapping("/logout")
    public ResponseEntity<?> logout(HttpServletRequest request, @AuthenticationPrincipal UserPrincipal principal) {

        return authService.logout(request,principal);
    }

    @GetMapping("/me/status")
    public ApiResponse<?> getMyStatus(@AuthenticationPrincipal UserPrincipal principal) {
        boolean online = userDeviceService.isUserOnline(principal.getUser().getId());
        String lastSeen = userDeviceService.getLastSeen(principal.getUser().getId());

        return ApiResponse.ok("Ok", Map.of(
                "online", online,
                "lastSeen", lastSeen
        ));
    }

    @GetMapping("/me/devices")
    public ApiResponse<?> myDevices(@AuthenticationPrincipal UserPrincipal userPrincipal, HttpServletRequest req) {
        return ApiResponse.ok("Ok", userDeviceService.getDevices(userPrincipal.getUser().getId(), req));
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

}
