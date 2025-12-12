package uz.codebyz.onlinecoursebackend.telegram.config;

import jakarta.annotation.PostConstruct;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class TelegramWebhookConfig {

    private final TelegramProperties props;
    private final RestTemplate restTemplate = new RestTemplate();

    public TelegramWebhookConfig(TelegramProperties props) {
        this.props = props;
    }

    @PostConstruct
    public void registerWebhook() {

        String token = props.getUsers().getBot().getToken();
        String webhookUrl = props.getBaseUrl()
                + props.getUsers().getBot().getWebhookPath();

        String url = "https://api.telegram.org/bot"
                + token
                + "/setWebhook?url="
                + webhookUrl;

        restTemplate.getForObject(url, String.class);

        System.out.println("✅ Telegram USERS webhook registered: " + webhookUrl);
    }
}
