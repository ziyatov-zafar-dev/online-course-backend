package uz.codebyz.onlinecoursebackend.telegram.controller;

import io.swagger.v3.oas.annotations.Hidden;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uz.codebyz.onlinecoursebackend.telegram.service.TelegramUsersService;

import java.util.Map;

@RestController
@Hidden
@RequestMapping("/telegram/users")
public class TelegramUsersWebhookController {

    private final TelegramUsersService service;

    public TelegramUsersWebhookController(TelegramUsersService service) {
        this.service = service;
    }

    @PostMapping("/webhook")
    public ResponseEntity<Void> handle(@RequestBody Map<String, Object> update, HttpServletRequest request) {
        service.handleUpdate(update, request);
        return ResponseEntity.ok().build();
    }
}
