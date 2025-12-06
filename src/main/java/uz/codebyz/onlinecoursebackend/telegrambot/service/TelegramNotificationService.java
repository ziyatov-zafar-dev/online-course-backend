package uz.codebyz.onlinecoursebackend.telegrambot.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service
public class TelegramNotificationService {

    @Value(value = "${telegram.bot.token}")
    private String botToken;

    private final WebClient webClient = WebClient.create();

    public void sendNotification(Long chatId, String message) {

        String url = "https://api.telegram.org/bot" + botToken + "/sendMessage";

        webClient.post()
                .uri(url)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue("""
                    {
                        "chat_id": %d,
                        "text": "%s"
                    }
                """.formatted(chatId, message.replace("\"", "'")))
                .retrieve()
                .bodyToMono(String.class)
                .subscribe();
    }
}
