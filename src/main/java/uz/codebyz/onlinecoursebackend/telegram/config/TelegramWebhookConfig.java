package uz.codebyz.onlinecoursebackend.telegram.config;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration

public class TelegramWebhookConfig {

    @Value("${telegram.base-url}")
    private String baseUrl;

    @Value("${telegram.users.bot.token}")
    private String usersBotToken;

    @Value("${telegram.users.bot.webhook-path}")
    private String usersWebhookPath;

    @Value("${telegram.teachers.bot.token}")
    private String teachersBotToken;

    @Value("${telegram.teachers.bot.webhook-path}")
    private String teachersWebhookPath;

    @PostConstruct
    public void registerWebhooks() {
        setWebhook(usersBotToken, baseUrl + usersWebhookPath);
        setWebhook(teachersBotToken, baseUrl + teachersWebhookPath);
    }

    private void setWebhook(String token, String url) {
        RestTemplate rest = new RestTemplate();
        String api = "https://api.telegram.org/bot" + token + "/setWebhook?url=" + url;
        rest.getForObject(api, String.class);
    }
}

