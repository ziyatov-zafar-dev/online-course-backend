package uz.codebyz.onlinecoursebackend.telegram.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.telegram.telegrambots.meta.api.objects.Update;
import uz.codebyz.onlinecoursebackend.telegram.service.TelegramUsersBotService;

@RestController

@RequestMapping("/telegram/users")
public class TelegramUsersWebhookController {
    private final TelegramUsersBotService telegramUsersBotService;

    public TelegramUsersWebhookController(TelegramUsersBotService telegramUsersBotService) {
        this.telegramUsersBotService = telegramUsersBotService;
    }

    @PostMapping("/webhook")
    public ResponseEntity<Void> onUpdate(@RequestBody Update update) {
        telegramUsersBotService.handle(update);
        return ResponseEntity.ok().build();
    }
}
