package uz.codebyz.onlinecoursebackend.telegram.service;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import uz.codebyz.onlinecoursebackend.common.config.AuthFrontendProperties;
import uz.codebyz.onlinecoursebackend.telegram.config.TelegramProperties;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Map;

@Service
public class TelegramUpdateService {

    private final TelegramProperties telegramProperties;
    private final AuthFrontendProperties frontendProperties;
    private final RestTemplate restTemplate = new RestTemplate();

    public TelegramUpdateService(TelegramProperties telegramProperties,
                                 AuthFrontendProperties frontendProperties) {
        this.telegramProperties = telegramProperties;
        this.frontendProperties = frontendProperties;
    }
/*
    @SuppressWarnings("unchecked")
    public void handleTeachers(Map<String, Object> update) {

        if (!update.containsKey("message")) return;

        Map<String, Object> message = (Map<String, Object>) update.get("message");
        String text = (String) message.get("text");

        if (text == null || !text.startsWith("/start")) return;

        Map<String, Object> chat = (Map<String, Object>) message.get("chat");
        Long chatId = Long.valueOf(chat.get("id").toString());

        String responseText = """
            👋 Assalomu alaykum, ustoz!
            
            CodeByZ Teacher bot ishga tushdi.
            """;

        sendMessage(chatId, responseText, telegramProperties.getTeachers().getToken());
    }
*/

    @SuppressWarnings("unchecked")
    public void handleUsers(Map<String, Object> update) {

        // 1️⃣ message bormi?
        if (!update.containsKey("message")) return;

        Map<String, Object> message = (Map<String, Object>) update.get("message");

        // 2️⃣ text bormi?
        String text = (String) message.get("text");
        if (text == null || !text.startsWith("/start")) return;

        // 3️⃣ chat.id ni olamiz
        Map<String, Object> chat = (Map<String, Object>) message.get("chat");
        Long chatId = Long.valueOf(chat.get("id").toString());

        // 4️⃣ frontend login URL
        String loginUrl = frontendProperties.getLoginUrl()
                + "/auth/telegram?chatId=" + chatId;

        // 5️⃣ xabar matni
        String responseText = """
                👋 Assalomu alaykum!
                
                CodeByZ platformasiga kirish uchun quyidagi tugmani bosing 👇
                
                🔐 Login: %s
                """.formatted(loginUrl);

        sendMessage(chatId, responseText);
    }

    private void sendMessage(Long chatId, String text) {

        String token = telegramProperties.getUsers().getToken();

        String url = "https://api.telegram.org/bot" + token
                + "/sendMessage?chat_id=" + chatId
                + "&text=" + URLEncoder.encode(text, StandardCharsets.UTF_8);

        restTemplate.getForObject(url, String.class);
    }
}
