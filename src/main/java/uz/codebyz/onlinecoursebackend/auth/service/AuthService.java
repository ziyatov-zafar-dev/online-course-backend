package uz.codebyz.onlinecoursebackend.auth.service;

import jakarta.servlet.http.HttpServletRequest;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import uz.codebyz.onlinecoursebackend.auth.dto.*;
import uz.codebyz.onlinecoursebackend.common.ApiResponse;
import uz.codebyz.onlinecoursebackend.device_login_attempts.entity.DeviceLoginAttempt;
import uz.codebyz.onlinecoursebackend.device_login_attempts.repository.DeviceLoginAttemptRepository;
import uz.codebyz.onlinecoursebackend.email.EmailService;
import uz.codebyz.onlinecoursebackend.helper.CurrentTime;
import uz.codebyz.onlinecoursebackend.helper.FileHelper;
import uz.codebyz.onlinecoursebackend.revokedToken.entity.RevokedToken;
import uz.codebyz.onlinecoursebackend.revokedToken.repository.RevokedTokenRepository;
import uz.codebyz.onlinecoursebackend.security.UserPrincipal;
import uz.codebyz.onlinecoursebackend.security.jwt.JwtAuthenticationFilter;
import uz.codebyz.onlinecoursebackend.security.jwt.JwtService;
import uz.codebyz.onlinecoursebackend.telegram.TelegramNotificationService;
import uz.codebyz.onlinecoursebackend.user.*;
import uz.codebyz.onlinecoursebackend.userDevice.entity.UserDevice;
import uz.codebyz.onlinecoursebackend.userDevice.repository.MaxDeviceRepository;
import uz.codebyz.onlinecoursebackend.userDevice.repository.UserDeviceRepository;
import uz.codebyz.onlinecoursebackend.userDevice.service.UserDeviceService;
import uz.codebyz.onlinecoursebackend.verification.VerificationCode;
import uz.codebyz.onlinecoursebackend.verification.VerificationService;
import uz.codebyz.onlinecoursebackend.verification.VerificationType;

import java.time.Duration;
import java.time.LocalDate;
import java.util.Optional;
import java.util.concurrent.ThreadLocalRandom;

@Service
public class AuthService {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final RevokedTokenRepository revokedTokenRepository;
    @Value("${login.security.max-wrong-attempts}")
    private int maxWrongAttempts;

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final VerificationService verificationService;
    private final EmailService emailService;
    private final JwtService jwtService;
    private final UserProfileRepository userProfileRepository;
    private final UserDeviceRepository userDeviceRepository;
    private final MaxDeviceRepository maxDeviceRepository;
    private final UserDeviceService userDeviceService;
    private final DeviceLoginAttemptRepository deviceLoginAttemptRepository;

    public AuthService(UserRepository userRepository,
                       PasswordEncoder passwordEncoder,
                       VerificationService verificationService,
                       EmailService emailService,
                       JwtService jwtService, UserProfileRepository userProfileRepository, UserDeviceRepository userDeviceRepository, MaxDeviceRepository maxDeviceRepository, UserDeviceService userDeviceService, DeviceLoginAttemptRepository deviceLoginAttemptRepository, JwtAuthenticationFilter jwtAuthenticationFilter, RevokedTokenRepository revokedTokenRepository, TelegramNotificationService telegramNotificationService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.verificationService = verificationService;
        this.emailService = emailService;
        this.jwtService = jwtService;
        this.userProfileRepository = userProfileRepository;
        this.userDeviceRepository = userDeviceRepository;
        this.maxDeviceRepository = maxDeviceRepository;
        this.userDeviceService = userDeviceService;
        this.deviceLoginAttemptRepository = deviceLoginAttemptRepository;
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
        this.revokedTokenRepository = revokedTokenRepository;
    }


    @Transactional
    public ApiResponse<Object> signUp(SignUpRequest request) {
        if (userRepository.existsByEmail(request.getEmail().toLowerCase())) {
            return ApiResponse.error("Bu email band.", "EMAIL_ALREADY_EXISTS");
        }
        if (userRepository.existsByUsername(request.getUsername())) {
            return ApiResponse.error("Bu username band.", "EMAIL_ALREADY_EXISTS");
        }

        if (!request.getPassword().equals(request.getConfirmPassword())) {
            return ApiResponse.error("Parollar mos kelmadi.", "PASSWORDS_NOT_MATCH");
        }

        User user = new User();
        user.setUsername(request.getUsername().toLowerCase());
        user.setFirstname(request.getFirstname());
        user.setLastname(request.getLastname());
        user.setEmail(request.getEmail());
        user.setUsername(generateUsername(request.getEmail()));
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRole(UserRole.STUDENT);
        user.setStatus(UserStatus.ACTIVE);
        user.setEnabled(false);
        if (request.getBirthDate() != null && !request.getBirthDate().isEmpty()) {
            user.setBirthDate(LocalDate.parse(request.getBirthDate()));
        }
        userRepository.save(user);

        VerificationCode verificationCode = verificationService.createVerification(user, VerificationType.SIGN_UP, null);
        emailService.sendEmail(user.getEmail(), "Tasdiqlash kodi", "Ro‘yxatdan o‘tishni tasdiqlang.", verificationCode.getCode());
        return ApiResponse.ok("Tasdiqlash kodi emailingizga yuborildi.");
    }

    @Transactional
    public ApiResponse<?> verifySignUp(SignUpVerifyRequest request, HttpServletRequest http) {

        String deviceId = generateDeviceId(http);

        // 1) DEVICE BLOK TEKSHIRISH
        ApiResponse<?> blockCheck = checkDeviceBlocked(deviceId);
        if (blockCheck != null) return blockCheck;

        // 2) USERNI TOPAMIZ
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> {
                    handleWrongPassword(deviceId);
                    return new BadCredentialsException("Login yoki parol noto‘g‘ri.");
                });

        // 3) KODNI TEKSHIRISH
        Optional<VerificationCode> valid = verificationService.validateCode(
                user,
                VerificationType.SIGN_UP,
                request.getCode(),
                null
        );

        if (valid.isEmpty()) {
            handleWrongPassword(deviceId);
            return ApiResponse.error("Kod noto‘g‘ri yoki muddati o‘tgan.", "INVALID_VERIFICATION_CODE");
        }

        // 🔥 4) TO‘G‘RI KOD → ATTEMPT RESET
        resetAttempts(deviceId);

        // 🔥 5) USERNI FAOLLASHTIRAMIZ
        user.setEnabled(true);

        // 🔥 6) PROFILE YO‘Q BO‘LSA YARATAMIZ
        if (user.getProfile() == null) {
            UserProfile profile = new UserProfile();
            profile.setUser(user);
            profile.setBio(null);
            profile.setWebsite(null);
            profile.setTelegram(null);
            profile.setGithub(null);
            profile.setLinkedin(null);
            profile.setTwitter(null);
            profile.setFacebook(null);

            userProfileRepository.save(profile);
            user.setProfile(profile);
        }

        userRepository.save(user);

        return ApiResponse.ok("Akkount muvaffaqiyatli tasdiqlandi.");
    }


    //    @Transactional
//    public ApiResponse<Object> signIn(SignInRequest request) {
//        User user = userRepository.findByEmail(request.getEmail())
//                .orElseThrow(() -> new BadCredentialsException("Login yoki parol noto‘g‘ri."));
//        if (!user.isEnabled()) {
//            return ApiResponse.error("Akkount tasdiqlanmagan.", "ACCOUNT_NOT_VERIFIED");
//        }
//        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
//            throw new BadCredentialsException("Login yoki parol noto‘g‘ri.");
//        }
//
//        VerificationCode verificationCode = verificationService.createVerification(user, VerificationType.SIGN_IN, null);
//        emailService.sendEmail(user.getEmail(), "Kirish kodi", "Kirishni tasdiqlang.", verificationCode.getCode());
//        return ApiResponse.ok("Kirishni tasdiqlash kodi emailingizga yuborildi.");
//    }
    private Optional<User> findByLogin(String login) {
        // email bo'lsa
        if (login.contains("@")) {
            return userRepository.findByEmail(login);
        }
        // username bo'lsa
        return userRepository.findByUsername(login);
    }

    @Transactional
    public SignInResult signIn(SignInRequest request, HttpServletRequest http) {

        String deviceId = generateDeviceId(http);

        // 1) DEVICE BLOK TEKSHIRAMIZ
        ApiResponse<?> blockCheck = checkDeviceBlocked(deviceId);
        if (blockCheck != null) {
            return new SignInResult(blockCheck, HttpStatus.UNAUTHORIZED);
        }

        // 2) USERNI TOPAMIZ
        User user;
        try {
            user = findByLogin(request.getLogin())
                    .orElseThrow(() -> new BadCredentialsException("Login yoki parol noto‘g‘ri."));
        } catch (BadCredentialsException ex) {
            handleWrongPassword(deviceId);
            return new SignInResult(
                    ApiResponse.error("Login yoki parol noto‘g‘ri.", "BAD_CREDENTIALS"),
                    HttpStatus.UNAUTHORIZED
            );
        }

        // 3) Tasdiqlanmagan bo‘lsa
        if (!user.isEnabled()) {
            return new SignInResult(
                    ApiResponse.error("Akkount tasdiqlanmagan.", "ACCOUNT_NOT_VERIFIED"),
                    HttpStatus.BAD_REQUEST
            );
        }

        // 4) Parol tekshirish
        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            handleWrongPassword(deviceId);

            return new SignInResult(
                    ApiResponse.error("Login yoki parol noto‘g‘ri.", "BAD_CREDENTIALS"),
                    HttpStatus.UNAUTHORIZED
            );
        }

        // 5) User bloklangan bo‘lsa
        if (user.getStatus() == UserStatus.BLOCKED) {
            return new SignInResult(
                    ApiResponse.error("Akkount bloklangan.", "ACCOUNT_BLOCKED"),
                    HttpStatus.FORBIDDEN
            );
        }

        // 6) TO‘G‘RI LOGIN → RESET
        resetAttempts(deviceId);

        // 7) EMAILGA VERIFICATION CODE
        VerificationCode code = verificationService.createVerification(
                user, VerificationType.SIGN_IN, null
        );


        emailService.sendEmail(
                user.getEmail(),
                "Kirish kodi",
                "Kirishni tasdiqlang.",
                code.getCode()
        );

        return new SignInResult(
                ApiResponse.ok("Kirishni tasdiqlash kodi yuborildi."),
                HttpStatus.OK
        );
    }


//    @Transactional
//    public ApiResponse<AuthTokensResponse> verifySignIn(SignInVerifyRequest request) {
//        User user = userRepository.findByEmail(request.getEmail())
//                .orElseThrow(() -> new BadCredentialsException("Login yoki parol noto‘g‘ri."));
//        Optional<VerificationCode> valid = verificationService.validateCode(user, VerificationType.SIGN_IN, request.getCode(), null);
//        if (valid.isEmpty()) {
//            return ApiResponse.error("Kod noto‘g‘ri yoki muddati o‘tgan.", "INVALID_VERIFICATION_CODE");
//        }
//        String access = jwtService.generateAccessToken(user);
//        String refresh = jwtService.generateRefreshToken(user);
//        return new ApiResponse<>(true, "Muvaffaqiyatli tizimga kirdingiz.", null,
//                new AuthTokensResponse(access, refresh, mapUser(user)));
//    }

    public String generateDeviceId(HttpServletRequest request) {
        String userAgent = request.getHeader("User-Agent");
        String ip = request.getRemoteAddr();
        String accept = request.getHeader("Accept");
        String encoding = request.getHeader("Accept-Encoding");
        String language = request.getHeader("Accept-Language");

        String raw = userAgent + "|" + ip + "|" + accept + "|" + encoding + "|" + language;

        return DigestUtils.sha256Hex(raw);
    }

    private void resetAttempts(String deviceId) {
        DeviceLoginAttempt attempt = deviceLoginAttemptRepository
                .findByDeviceId(deviceId)
                .orElse(null);
        if (attempt != null) {
            attempt.setAttempts(0);
            attempt.setBlockedUntil(null);
            deviceLoginAttemptRepository.save(attempt);
        }
    }

    private void handleWrongPassword(String deviceId) {

        DeviceLoginAttempt attempt = deviceLoginAttemptRepository
                .findByDeviceId(deviceId)
                .orElse(null);

        if (attempt == null) {
            attempt = new DeviceLoginAttempt();
            attempt.setDeviceId(deviceId);
            attempt.setAttempts(1);
            attempt.setLastAttempt(CurrentTime.currentTime());
            deviceLoginAttemptRepository.save(attempt);
            return;
        }

        attempt.setAttempts(attempt.getAttempts() + 1);
        attempt.setLastAttempt(CurrentTime.currentTime());

        if (attempt.getAttempts() >= maxWrongAttempts) {
            // attempts:
            // 3 → 1 soat
            // 4 → 2 soat
            // 5 → 3 soat ...
            int hours;
            if (attempt.getAttempts() < 10) {
                hours = attempt.getAttempts() - 2;
            } else {
                hours = 24 * 7;
            }
            attempt.setBlockedUntil(CurrentTime.currentTime().plusHours(hours));
        }

        deviceLoginAttemptRepository.save(attempt);
    }

    private ApiResponse<?> checkDeviceBlocked(String deviceId) {

        DeviceLoginAttempt attempt = deviceLoginAttemptRepository
                .findByDeviceId(deviceId)
                .orElse(null);

        if (attempt != null && attempt.getBlockedUntil() != null) {
            if (attempt.getBlockedUntil().isAfter(CurrentTime.currentTime())) {

                Duration d = Duration.between(CurrentTime.currentTime(), attempt.getBlockedUntil());

                long seconds = d.getSeconds();

                long hours = seconds / 3600;
                long minutes = (seconds % 3600) / 60;
                long secs = seconds % 60;

                String message;

// Formatni yasalishi
                if (hours > 0) {
                    message = hours + " soat " + minutes + " daqiqa " + secs + " sekund qoldi.";
                } else if (minutes > 0) {
                    message = minutes + " daqiqa " + secs + " sekund qoldi.";
                } else {
                    message = secs + " sekund qoldi.";
                }

                return ApiResponse.error(
                        "Ushbu qurilma vaqtincha bloklangan. Kutish vaqti: " + message,
                        "DEVICE_BLOCKED"
                );

            }
        }

        return null; // bloklanmagan
    }

//    @Transactional
//    public ApiResponse<?> verifySignIn(SignInVerifyRequest request, HttpServletRequest http) {
//
//        // 1. USERNI TOPAMIZ
//        User user = userRepository.findByEmail(request.getEmail())
//                .orElseThrow(() -> new BadCredentialsException("Login yoki parol noto‘g‘ri."));
//
//        // 2. KODNI TEKSHIRAMIZ
//        Optional<VerificationCode> valid = verificationService.validateCode(
//                user, VerificationType.SIGN_IN, request.getCode(), null
//        );
//
//        if (valid.isEmpty()) {
//            return ApiResponse.error("Kod noto‘g‘ri yoki muddati o‘tgan.", "INVALID_VERIFICATION_CODE");
//        }
//
//        // ================================
//        // 🚀 3. QURILMA LIMITINI TEKSHIRISH BO'LIMI
//        // ================================
//        String deviceId = generateDeviceId(http);
//
//        boolean exists = userDeviceRepository.existsByUserIdAndDeviceId(user.getId(), deviceId);
//
//        if (!exists) {
//
//            long activeDevices = userDeviceRepository.countByUserId(user.getId());
//            int maxDevices = maxDeviceRepository.getMaxDeviceCount().getDeviceCount();
//
//            // Agar limitga yetgan bo'lsa → bloklaymiz
//            if (activeDevices >= maxDevices) {
//                return ApiResponse.error(
//                        "Kirish rad etildi. Siz faqat " + maxDevices + " ta qurilmada ishlata olasiz.",
//                        "DEVICE_LIMIT_REACHED",
//                        userDeviceService.getDevices(user.getId(), http)
//                );
//            }
//
//            // Yangi qurilmani ro'yxatdan o'tkazamiz
//            UserDevice device = new UserDevice();
//            device.setUserId(user.getId());
//            device.setDeviceId(deviceId);
//            device.setUserAgent(http.getHeader("User-Agent"));
//            device.setIpAddress(http.getRemoteAddr());
//            userDeviceRepository.save(device);
//        }
//        // ================================
//        // 🚀 QURILMA LIMITI TUGADI
//        // ================================
//
//        // 4. TOKENLARNI GENERATSIYA QILAMIZ
//        String access = jwtService.generateAccessToken(user);
//        String refresh = jwtService.generateRefreshToken(user);
//
//        return new ApiResponse<>(
//                true,
//                "Muvaffaqiyatli tizimga kirdingiz." + userDeviceRepository.countByUserId(user.getId()),
//                null,
//                new AuthTokensResponse(access, refresh, mapUser(user))
//        );
//    }

    //    @Transactional
//    public ApiResponse<?> verifySignIn(SignInVerifyRequest request, HttpServletRequest http) {
//
//        String deviceId = generateDeviceId(http);
//
//        // 1️⃣ DEVICE BLOK TEKSHIRAMIZ (3 martalik xatolar)
//        ApiResponse<?> blockCheck = checkDeviceBlocked(deviceId);
//        if (blockCheck != null) return blockCheck;
//
//        // 2️⃣ USERNI TOPAMIZ
//        User user = userRepository.findByEmail(request.getLogin())
//                .orElseThrow(() -> {
//                    handleWrongPassword(deviceId); // noto'g'ri email bo'lsa ham shu device uchun attempt qo'shiladi
//                    return new BadCredentialsException("Login yoki parol noto‘g‘ri.");
//                });
//
//        // 3️⃣ TASDIQLASH KODINI TEKSHIRAMIZ
//        Optional<VerificationCode> valid = verificationService.validateCode(
//                user, VerificationType.SIGN_IN, request.getCode(), null
//        );
//
//        if (valid.isEmpty()) {
//            handleWrongPassword(deviceId); // noto'g'ri verification code
//            return ApiResponse.error("Kod noto‘g‘ri yoki muddati o‘tgan.", "INVALID_VERIFICATION_CODE");
//        }
//
//        // 4️⃣ TO‘G‘RI VERIFICATION → BLOKNI RESET QILAMIZ
//        resetAttempts(deviceId);
//
//        // ===================================================
//        // 🚀 5️⃣ QURILMA LIMITINI TEKSHIRAMIZ (SENING MEBHORING)
//        // ===================================================
//        boolean exists = userDeviceRepository.existsByUserIdAndDeviceId(user.getId(), deviceId);
//
//        if (!exists) {
//
//            long activeDevices = userDeviceRepository.countByUserId(user.getId());
//            int maxDevices = maxDeviceRepository.getMaxDeviceCount().getDeviceCount();
//
//            if (activeDevices >= maxDevices) {
//                return ApiResponse.error(
//                        "Kirish rad etildi. Siz faqat " + maxDevices + " ta qurilmada ishlata olasiz.",
//                        "DEVICE_LIMIT_REACHED",
//                        userDeviceService.getDevices(user.getId(), http)
//                );
//            }
//
//            // Yangi qurilmani ro'yxatdan o'tkazamiz
//            UserDevice device = new UserDevice();
//            device.setUserId(user.getId());
//            device.setDeviceId(deviceId);
//            device.setUserAgent(http.getHeader("User-Agent"));
//            device.setIpAddress(http.getRemoteAddr());
//            userDeviceRepository.save(device);
//        }
//
//        // ===================================================
//        // 🚀 6️⃣ TOKENLARNI GENERATSIYA QILAMIZ
//        // ===================================================
//
//        String access = jwtService.generateAccessToken(user);
//        String refresh = jwtService.generateRefreshToken(user);
//
//        return new ApiResponse<>(
//                true,
//                "Muvaffaqiyatli tizimga kirdingiz.",
//                null,
//                new AuthTokensResponse(access, refresh, mapUser(user))
//        );
//    }
    @Transactional
    public ApiResponse<?> verifySignIn(SignInVerifyRequest request, HttpServletRequest http) {

        String deviceId = generateDeviceId(http);

        // 1️⃣ DEVICE BLOK TEKSHIRAMIZ
        ApiResponse<?> blockCheck = checkDeviceBlocked(deviceId);
        if (blockCheck != null) return blockCheck;

        // 2️⃣ USERNI TOPAMIZ (email yoki username)
/*
        User user = findByLogin(request.getLogin())
                .orElseThrow(() -> {
                    handleWrongPassword(deviceId);
                    return new BadCredentialsException("Login yoki parol noto‘g‘ri.");
                });
*/

        User user = findByLogin(request.getLogin())
                .orElseThrow(() -> {
                    handleWrongPassword(deviceId);
                    return new BadCredentialsException("Login yoki parol noto‘g‘ri.");
                });
        Optional<VerificationCode> valid = verificationService.validateCode(
                user, VerificationType.SIGN_IN, request.getCode(), null
        );

        if (valid.isEmpty()) {
            handleWrongPassword(deviceId);
            return ApiResponse.error("Kod noto‘g‘ri yoki muddati o‘tgan.", "INVALID_VERIFICATION_CODE");
        }

        Optional<UserDevice> udOp = userDeviceRepository.findByDeviceId(deviceId);
        if (udOp.isPresent()) {
            return ApiResponse.error("Berilgan deviceId tizimda allaqachon mavjud"
                    , "DUPLICATION_DEVICE");
        }
        // 4️⃣ TO‘G‘RI VERIFICATION → BLOK RESET
        resetAttempts(deviceId);

        // ===================================================
        // 🚀 5️⃣ QURILMA LIMITINI TEKSHIRAMIZ
        // ===================================================
        boolean exists = userDeviceRepository.existsByUserIdAndDeviceId(user.getId(), deviceId);
        String currentIpAddress = http.getRemoteAddr();
        if (!exists) {

            long activeDevices = userDeviceRepository.countByUserId(user.getId());
            int maxDevices = maxDeviceRepository.getMaxDeviceCount().getDeviceCount();


            if (activeDevices >= maxDevices) {
                return ApiResponse.error(
                        "Kirish rad etildi. Siz faqat " + maxDevices + " ta qurilmada ishlata olasiz.",
                        "DEVICE_LIMIT_REACHED",
                        userDeviceService.getDevices(user.getId(), http)
                );
            }
            // Yangi qurilma qo‘shiladi
            UserDevice device = new UserDevice();
            device.setUserId(user.getId());
            device.setDeviceId(deviceId);
            device.setUserAgent(http.getHeader("User-Agent"));
            device.setIpAddress(http.getRemoteAddr());
            if (device.getIpAddress() != null)
                userDeviceRepository.save(device);
            else return ApiResponse.error("ip adres olishda xatolik yuz berdi", "NOT_FOUND_IP_ADDRESS");
        }

        // ===================================================
        // 🚀 6️⃣ TOKENLARNI GENERATSIYA QILAMIZ
        // ===================================================
        String access = jwtService.generateAccessToken(user);
        String refresh = jwtService.generateRefreshToken(user);

        return new ApiResponse<>(
                true,
                "Muvaffaqiyatli tizimga kirdingiz.",
                null,
                new AuthTokensResponse(access, refresh, mapUser(user))
        );
    }

    public ApiResponse<AuthTokensResponse> refreshToken(RefreshTokenRequest request) {
        String email = jwtService.extractEmail(request.getRefreshToken());
        User user = userRepository.findByEmail(email).orElseThrow(() -> new BadCredentialsException("Token noto‘g‘ri."));
        if (!jwtService.isTokenValid(request.getRefreshToken(), user)) {
            return ApiResponse.error("Refresh token yaroqsiz yoki muddati o‘tgan.", "INVALID_REFRESH_TOKEN");
        }
        String newAccess = jwtService.generateAccessToken(user);
        String newRefresh = jwtService.generateRefreshToken(user);
        return ApiResponse.ok("Token yangilandi.", new AuthTokensResponse(newAccess, newRefresh, mapUser(user)));
    }

    @Transactional
    public ApiResponse<Object> forgotPassword(ForgotPasswordRequest request) {
        Optional<User> optionalUser = userRepository.findByEmail(request.getEmail());
        optionalUser.ifPresent(user -> {
            VerificationCode verificationCode = verificationService.createVerification(user, VerificationType.FORGOT_PASSWORD, null);
            emailService.sendEmail(user.getEmail(), "Parolni tiklash", "Parolni tiklash kodini kiriting.", verificationCode.getCode());
        });
        return ApiResponse.ok("Parolni tiklash uchun kod emailingizga yuborildi.");
    }

    @Transactional
    public ApiResponse<Object> resetPassword(ResetPasswordRequest request) {
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new BadCredentialsException("Login yoki parol noto‘g‘ri."));
        if (!request.getNewPassword().equals(request.getConfirmPassword())) {
            return ApiResponse.error("Parollar mos kelmadi.", "PASSWORDS_NOT_MATCH");
        }
        Optional<VerificationCode> valid = verificationService.validateCode(user, VerificationType.FORGOT_PASSWORD, request.getCode(), null);
        if (valid.isEmpty()) {
            return ApiResponse.error("Kod noto‘g‘ri yoki muddati o‘tgan.", "INVALID_VERIFICATION_CODE");
        }
        user.setPassword(passwordEncoder.encode(request.getNewPassword()));
        userRepository.save(user);
        return ApiResponse.ok("Parolingiz muvaffaqiyatli o'zgartirildi.");
    }

    @Transactional
    public ApiResponse<Object> changePassword(User user, ChangePasswordRequest request) {
        if (!passwordEncoder.matches(request.getCurrentPassword(), user.getPassword())) {
            return ApiResponse.error("Joriy parol noto‘g‘ri.", "BAD_CURRENT_PASSWORD");
        }
        if (!request.getNewPassword().equals(request.getConfirmPassword())) {
            return ApiResponse.error("Parollar mos kelmadi.", "PASSWORDS_NOT_MATCH");
        }
        user.setPassword(passwordEncoder.encode(request.getNewPassword()));
        userRepository.save(user);
        return ApiResponse.ok("Parol muvaffaqiyatli o'zgartirildi.");
    }


    @Transactional
    public ApiResponse<UserResponse> changeUsername(User user, ChangeUsernameRequest request) {
        request.setNewUsername(user.getUsername().toLowerCase());
        if (!isSuccessUsername(request.getNewUsername())) {
            return ApiResponse.error("Username yaroqli emas", "INVALID_USERNAME");
        }
        if (userRepository.existsByUsername(request.getNewUsername())) {
            return ApiResponse.error("Foydalanuvchi nomi band.", "USERNAME_ALREADY_EXISTS");
        }
        user.setUsername(request.getNewUsername());
        userRepository.save(user);
        return ApiResponse.ok("Foydalanuvchi nomi yangilandi.", mapUser(user));
    }

    private boolean isSuccessUsername(String username) {
        if (username == null) return false;
        return username.matches("^[a-zA-Z][a-zA-Z0-9_]{4,31}$");
    }

    @Transactional
    public ApiResponse<Object> requestChangeEmail(User user, ChangeEmailRequest request) {
        if (userRepository.existsByEmail(request.getNewEmail())) {
            return ApiResponse.error("Bu email band.", "EMAIL_ALREADY_EXISTS");
        }
        VerificationCode code = verificationService.createVerification(user, VerificationType.CHANGE_EMAIL, request.getNewEmail());
        emailService.sendEmail(request.getNewEmail(), "Emailni tasdiqlash", "Yangi emailingizni tasdiqlang.", code.getCode());
        return ApiResponse.ok("Emailni tasdiqlash kodi yuborildi.");
    }

    @Transactional
    public ApiResponse<UserResponse> verifyChangeEmail(User user, ChangeEmailVerifyRequest request) {
        Optional<VerificationCode> valid = verificationService.validateCode(user, VerificationType.CHANGE_EMAIL, request.getCode(), request.getNewEmail());
        if (valid.isEmpty()) {
            return ApiResponse.error("Kod noto‘g‘ri yoki muddati o‘tgan.", "INVALID_VERIFICATION_CODE");
        }
        user.setEmail(request.getNewEmail());
        userRepository.save(user);
        return ApiResponse.ok("Email yangilandi.", mapUser(user));
    }

    @Transactional
    public ApiResponse<UserResponse> updateProfile(User user, UpdateProfileRequest request) {
        if (request.getFirstname() != null) {
            user.setFirstname(request.getFirstname());
        }
        if (request.getLastname() != null) {
            user.setLastname(request.getLastname());
        }
        if (request.getBirthDate() != null && !request.getBirthDate().isEmpty()) {
            user.setBirthDate(LocalDate.parse(request.getBirthDate()));
        }
        if (request.getBio() != null || hasSocials(request)) {
            if (user.getProfile() == null) {
                UserProfile profile = new UserProfile();
                profile.setUser(user);
                user.setProfile(profile);
            }
            if (request.getBio() != null) {
                user.getProfile().setBio(request.getBio());
            }
            if (request.getWebsite() != null) user.getProfile().setWebsite(request.getWebsite());
            if (request.getTelegram() != null) user.getProfile().setTelegram(request.getTelegram());
            if (request.getGithub() != null) user.getProfile().setGithub(request.getGithub());
            if (request.getLinkedin() != null) user.getProfile().setLinkedin(request.getLinkedin());
            if (request.getTwitter() != null) user.getProfile().setTwitter(request.getTwitter());
            if (request.getFacebook() != null) user.getProfile().setFacebook(request.getFacebook());
            if (request.getInstagram() != null) user.getProfile().setInstagram(request.getInstagram());
            user.getProfile().setUpdatedAt();
        }
        userRepository.save(user);
        return ApiResponse.ok("Profil muvaffaqiyatli yangilandi.", mapUser(user));
    }

    private boolean hasSocials(UpdateProfileRequest request) {
        return request.getWebsite() != null || request.getTelegram() != null || request.getGithub() != null
                || request.getLinkedin() != null || request.getTwitter() != null
                || request.getFacebook() != null || request.getInstagram() != null;
    }

    public ApiResponse<UserResponse> getMe(User user) {
        return ApiResponse.ok("OK", mapUser(user));
    }

    private String generateUsername(String email) {
        String base = email.split("@")[0];
        String candidate = base;
        while (userRepository.existsByUsername(candidate)) {
            candidate = base + "_" + ThreadLocalRandom.current().nextInt(1000, 9999);
        }
        return candidate;
    }

    public ApiResponse<Object> validateUsername(String username, User currentUser) {
        if (!isSuccessUsername(username.toLowerCase())) {
            return ApiResponse.error("Username yaroqsiz. Harf bilan boshlansin, 5-32 belgi va faqat lotin/raqam/_.", "USERNAME_INVALID");
        }
        Optional<User> existing = userRepository.findByUsername(username);
        if (existing.isPresent()) {
            if (currentUser != null && existing.get().getId().equals(currentUser.getId())) {
                return ApiResponse.ok("Username yaroqli.", null);
            }
            return ApiResponse.error("Bu username band.", "USERNAME_ALREADY_EXISTS");
        }
        return ApiResponse.ok("Username yaroqli.");
    }


    private UserResponse mapUser(User user) {
        UserResponse res = new UserResponse();
        res.setId(user.getId());
        res.setFirstname(user.getFirstname());
        res.setLastname(user.getLastname());
        res.setUsername(user.getUsername());
        res.setEmail(user.getEmail());
        boolean userOnline = userDeviceService.isUserOnline(user.getId());
        res.setOnline(userOnline);
//        res.setLastOnline(userDeviceService.getLastSeen(user.getId()));
        res.setLastOnline(userOnline ? CurrentTime.currentTime().toString() : userDeviceService.getLastSeen(user.getId()));
        res.setBirthDate(user.getBirthDate() != null ? user.getBirthDate().toString() : null);
        if (user.getProfile() != null) {
            res.setBio(user.getProfile().getBio());
            res.setWebsite(user.getProfile().getWebsite());
            res.setTelegram(user.getProfile().getTelegram());
            res.setGithub(user.getProfile().getGithub());
            res.setLinkedin(user.getProfile().getLinkedin());
            res.setTwitter(user.getProfile().getTwitter());
            res.setFacebook(user.getProfile().getFacebook());
            res.setInstagram(user.getProfile().getInstagram());
            if (user.getProfile().getImages() != null && !user.getProfile().getImages().isEmpty()) {
                java.util.List<ProfileImageResponse> imgDtos = new java.util.ArrayList<>();
                for (UserProfileImage img : user.getProfile().getImages()) {
                    ProfileImageResponse dto = new ProfileImageResponse();
                    dto.setId(img.getId());
                    dto.setUrl(img.getUrl());
                    dto.setOriginalName(img.getOriginalName());
                    dto.setContentType(img.getContentType());
                    dto.setSize(img.getSize());
                    dto.setSizeMB(FileHelper.getFileSize(img.getSize()));
//                    dto.setSizeMB(FileHelper.getFileSize(3584));
                    imgDtos.add(dto);
                }
                res.setImages(imgDtos);
            }
        }
        res.setRole(user.getRole());
        res.setStatus(user.getStatus());
        res.setEnabled(user.isEnabled());
        return res;
    }

    /*    public ResponseEntity<?> logout(HttpServletRequest request, UserPrincipal principal) {
            User user = principal.getUser();
            String token = jwtAuthenticationFilter.extractToken(request);
            String deviceId = jwtService.extractDeviceId(request);
            // token revoked jadvaliga qo‘shiladi
            revokedTokenRepository.save(new RevokedToken(token));

            // qurilmani o'chirish
    //        userDeviceRepository.deleteByUserIdAndDeviceId(user.getId(), deviceId);
    //        userDeviceRepository.deleteById(device.getId());
            return ResponseEntity.ok(ApiResponse.ok("Logged out"));
        }*/
    @Transactional
    public ResponseEntity<?> logout(HttpServletRequest request, UserPrincipal principal) {

        User user = principal.getUser();

        // 🔐 Token va deviceId olish
        String token = jwtAuthenticationFilter.extractToken(request);
        String deviceId = jwtService.extractDeviceId(request);

        // 1️⃣ Tokenni revoked qilish
        revokedTokenRepository.save(new RevokedToken(token));

        // 2️⃣ Aynan shu device ni o‘chirish
        userDeviceRepository.deleteByUserIdAndDeviceId(
                user.getId(),
                deviceId
        );

        return ResponseEntity.ok(
                ApiResponse.ok("Logged out successfully")
        );
    }

}
