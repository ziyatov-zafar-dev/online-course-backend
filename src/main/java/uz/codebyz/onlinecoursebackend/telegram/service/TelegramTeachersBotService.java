package uz.codebyz.onlinecoursebackend.telegram.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.Update;
@Service
public class TelegramTeachersBotService {

    @Value("${auth-frontend.login-url}")
    private String loginUrl;

    private final TelegramSender sender;

    public TelegramTeachersBotService(TelegramSender sender) {
        this.sender = sender;
    }


    public void handle(Update update) {

        if (update.getMessage() == null || !update.getMessage().hasText()) return;

        String text = update.getMessage().getText();
        Long chatId = update.getMessage().getChatId();

        if ("/start".equals(text)) {

            String url = loginUrl + "?chatId=" + chatId;

            sender.sendMessageWithButton(
                    chatId,
                    """
                    👋 Assalomu alaykum!

                    CodeByZ platformasiga ulanish uchun
                    pastdagi tugmani bosing 👇
                    """,
                    "🔐 Ulanish",
                    url
            );
        }
    }
}
