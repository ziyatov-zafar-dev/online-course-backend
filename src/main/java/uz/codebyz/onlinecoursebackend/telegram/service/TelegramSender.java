package uz.codebyz.onlinecoursebackend.telegram.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;

@Service
public class TelegramSender {

    @Value("${telegram.users.bot.token}")
    private String usersBotToken;

    private final RestTemplate restTemplate = new RestTemplate();

    public void sendMessageWithButton(Long chatId, String text, String buttonText, String url) {

        String api = "https://api.telegram.org/bot" + usersBotToken + "/sendMessage";

        Map<String, Object> body = Map.of(
                "chat_id", chatId,
                "text", text,
                "reply_markup", Map.of(
                        "inline_keyboard", List.of(
                                List.of(
                                        Map.of(
                                                "text", buttonText,
                                                "url", url
                                        )
                                )
                        )
                )
        );

        restTemplate.postForObject(api, body, String.class);
    }
}
