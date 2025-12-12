package uz.codebyz.onlinecoursebackend.telegram.service;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import uz.codebyz.onlinecoursebackend.telegram.config.TelegramProperties;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Map;

@Service
public class TelegramUsersService {

    private final TelegramProperties props;
    private final RestTemplate restTemplate = new RestTemplate();

    public TelegramUsersService(TelegramProperties props) {
        this.props = props;
    }

    @SuppressWarnings("unchecked")
    public void handleUpdate(Map<String, Object> update) {

        if (!update.containsKey("message")) return;

        Map<String, Object> message = (Map<String, Object>) update.get("message");
        String text = (String) message.get("text");

        if (text == null || !text.startsWith("/start")) return;

        Map<String, Object> chat = (Map<String, Object>) message.get("chat");
        Long chatId = Long.valueOf(chat.get("id").toString());

        String loginUrl =
                "https://online-course-tg-bot-for-users-up37-5p5csufu1.vercel.app"
                        + "/auth/telegram?chatId=" + chatId;

        String response = """
                👋 Assalomu alaykum!
                
                CodeByZ platformasiga kirish uchun pastdagi havola orqali login qiling:
                
                🔐 %s
                """.formatted(loginUrl);

        sendMessage(chatId, response);
    }

    private void sendMessage(Long chatId, String text) {

        String token = props.getUsers().getBot().getToken();

        String url = "https://api.telegram.org/bot" + token
                + "/sendMessage?chat_id=" + chatId
                + "&text=" + URLEncoder.encode(text, StandardCharsets.UTF_8);

        restTemplate.getForObject(url, String.class);
    }
}
