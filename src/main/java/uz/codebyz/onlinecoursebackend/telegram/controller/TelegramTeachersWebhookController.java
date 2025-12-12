package uz.codebyz.onlinecoursebackend.telegram.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.telegram.telegrambots.meta.api.objects.Update;
import uz.codebyz.onlinecoursebackend.telegram.service.TelegramTeachersBotService;

@RequestMapping("/telegram/teachers")
@RestController
public class TelegramTeachersWebhookController {
    private final TelegramTeachersBotService telegramTeachersBotService;

    public TelegramTeachersWebhookController(TelegramTeachersBotService telegramTeachersBotService) {
        this.telegramTeachersBotService = telegramTeachersBotService;
    }

    @PostMapping("/webhook")
    public ResponseEntity<Void> onUpdate(@RequestBody Update update) {
        telegramTeachersBotService.handle(update);
        return ResponseEntity.ok().build();
    }
}
