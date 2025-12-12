package uz.codebyz.onlinecoursebackend.telegram.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "telegram")
public class TelegramProperties {

    private String baseUrl;
    private Users users;

    public String getBaseUrl() {
        return baseUrl;
    }

    public void setBaseUrl(String baseUrl) {
        this.baseUrl = baseUrl;
    }

    public Users getUsers() {
        return users;
    }

    public void setUsers(Users users) {
        this.users = users;
    }

    public static class Users {
        private Bot bot;

        public Bot getBot() {
            return bot;
        }

        public void setBot(Bot bot) {
            this.bot = bot;
        }
    }

    public static class Bot {
        private String token;
        private String username;
        private String webhookPath;

        public String getToken() {
            return token;
        }

        public void setToken(String token) {
            this.token = token;
        }

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getWebhookPath() {
            return webhookPath;
        }

        public void setWebhookPath(String webhookPath) {
            this.webhookPath = webhookPath;
        }
    }
}
