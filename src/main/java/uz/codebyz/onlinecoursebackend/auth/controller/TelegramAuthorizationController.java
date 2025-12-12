package uz.codebyz.onlinecoursebackend.auth.controller;

import io.swagger.v3.oas.annotations.Hidden;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import uz.codebyz.onlinecoursebackend.auth.dto.TelegramAuthorizationRequest;
import uz.codebyz.onlinecoursebackend.auth.dto.TelegramAuthorizationResponse;
import uz.codebyz.onlinecoursebackend.auth.service.TelegramAuthorizationService;
import uz.codebyz.onlinecoursebackend.common.ResponseDto;

@RestController
@RequestMapping("/api/auth/authorization")
@Hidden
public class TelegramAuthorizationController {

    private final TelegramAuthorizationService telegramAuthorizationService;

    public TelegramAuthorizationController(TelegramAuthorizationService telegramAuthorizationService) {
        this.telegramAuthorizationService = telegramAuthorizationService;
    }

    @PostMapping("/telegram")
    public ResponseEntity<ResponseDto<TelegramAuthorizationResponse>> authorize(
            @Valid @RequestBody TelegramAuthorizationRequest request) {
        return ResponseEntity.ok(telegramAuthorizationService.authorize(request));
    }
}
