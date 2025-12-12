package uz.codebyz.onlinecoursebackend.telegram.controller;

import io.swagger.v3.oas.annotations.Hidden;
import org.springframework.web.bind.annotation.*;
import uz.codebyz.onlinecoursebackend.telegram.service.TelegramUpdateService;

import java.util.Map;

@RestController
@RequestMapping("/api/telegram")
@Hidden
public class TelegramWebhookController {

    private final TelegramUpdateService updateService;

    public TelegramWebhookController(TelegramUpdateService updateService) {
        this.updateService = updateService;
    }

    @PostMapping("/users")
    public void usersWebhook(@RequestBody Map<String, Object> update) {
        updateService.handleUsers(update);
    }

    @PostMapping("/teachers")
    public void teachersWebhook(@RequestBody Map<String, Object> update) {
        updateService.handleTeachers(update);
    }
}
