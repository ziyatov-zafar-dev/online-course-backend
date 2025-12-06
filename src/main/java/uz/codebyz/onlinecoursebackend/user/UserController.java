package uz.codebyz.onlinecoursebackend.user;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import uz.codebyz.onlinecoursebackend.auth.dto.*;
import uz.codebyz.onlinecoursebackend.auth.service.AuthService;
import uz.codebyz.onlinecoursebackend.common.ApiResponse;
import uz.codebyz.onlinecoursebackend.security.UserPrincipal;

@RestController
@RequestMapping("/api/users/me")
@Tag(name = "User", description = "Autentifikatsiyadan o‘tgan foydalanuvchi amallari")
public class UserController {

    private final AuthService authService;
    private final ProfileImageService profileImageService;

    public UserController(AuthService authService, ProfileImageService profileImageService) {
        this.authService = authService;
        this.profileImageService = profileImageService;
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
    public ResponseEntity<ApiResponse<UserResponse>> verifyChangeEmail(@AuthenticationPrincipal UserPrincipal principal,
                                                                       @Valid @RequestBody ChangeEmailVerifyRequest request) {
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
}
