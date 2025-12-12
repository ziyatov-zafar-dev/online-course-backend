package uz.codebyz.onlinecoursebackend.telegram;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import uz.codebyz.onlinecoursebackend.common.ResponseDto;

@Service
public class TelegramNotificationService {

    @Value(value = "${telegram.bot.token}")
    private String botToken;

    private final WebClient webClient = WebClient.create();

    public ResponseDto<Void> sendMessage(Long chatId, String message) {
        try {

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
            return new ResponseDto<>(true, "Message Sent");
        } catch (Exception e) {
            return new ResponseDto<>(false, "Operatsiya bajarilmadi, buning sababi: " + e.getMessage());
        }
    }
}
