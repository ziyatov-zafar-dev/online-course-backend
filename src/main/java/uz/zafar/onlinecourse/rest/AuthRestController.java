package uz.zafar.onlinecourse.rest;

import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uz.zafar.onlinecourse.dto.ResponseDto;
import uz.zafar.onlinecourse.dto.form.LoginForm;
import uz.zafar.onlinecourse.dto.user_dto.req.SignUpForm;
import uz.zafar.onlinecourse.service.AuthService;
import uz.zafar.onlinecourse.service.EmailService;
import uz.zafar.onlinecourse.service.UserService;

import java.io.File;
import java.util.List;
//import uz.farobiy.lesson_11_backend.dto.form.LoginForm;
//import uz.farobiy.lesson_11_backend.service.UserService;

@RestController
@RequestMapping("api/auth")
@Tag(
        name = "Authentication Controller",
        description = "Foydalanuvchini ro‘yxatdan o‘tkazish, tizimga kirish (login) va autentifikatsiya bilan bog‘liq endpointlar"
)
public class AuthRestController {
    @Autowired
    private UserService userService;
    @Autowired
    private AuthService authService;
    @Value("${homework.video.base.url}")
    private String homeworkVideoBaseUrl;
    @Autowired
    private EmailService emailService;

    @PostMapping("/sign-in")
    public ResponseEntity<?> signIn(@RequestBody LoginForm form) throws Exception {
        return ResponseEntity.ok(userService.signIn(form));
    }

    @PostMapping("/sign-up")
    public ResponseEntity<?> signUp(@RequestBody SignUpForm form) throws Exception {
        return ResponseEntity.ok(userService.signUp(form));
    }

    @PostMapping("/verification")
    public ResponseEntity<?> getVerificationCode(@RequestParam String verificationCode) throws Exception {
        return ResponseEntity.ok(userService.verifyCode(verificationCode));
    }
}
