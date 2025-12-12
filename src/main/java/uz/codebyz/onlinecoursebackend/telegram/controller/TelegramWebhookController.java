package uz.codebyz.onlinecoursebackend.telegram.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uz.codebyz.onlinecoursebackend.telegram.service.TelegramUpdateService;

import java.util.Map;

@RestController
@RequestMapping("/api/telegram")
public class TelegramWebhookController {

    private final TelegramUpdateService updateService;

    public TelegramWebhookController(TelegramUpdateService updateService) {
        this.updateService = updateService;
    }

    // USERS BOT WEBHOOK
    @PostMapping("/users")
    public ResponseEntity<Void> usersWebhook(@RequestBody Map<String, Object> update) {
        updateService.handleUsers(update);
        return ResponseEntity.ok().build();
    }

    // TEACHERS BOT WEBHOOK
   /* @PostMapping("/teachers")
    public ResponseEntity<Void> teachersWebhook(@RequestBody Map<String, Object> update) {
        updateService.handleTeachers(update);
        return ResponseEntity.ok().build();
    }*/
}
