package uz.codebyz.onlinecoursebackend.telegram.controller;

import io.swagger.v3.oas.annotations.Hidden;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import uz.codebyz.onlinecoursebackend.common.ResponseDto;
import uz.codebyz.onlinecoursebackend.telegram.config.TelegramProperties;

@RestController
@RequestMapping("/api/telegram/admin")
@Hidden
public class TelegramAdminController {

    private final TelegramProperties props;
    private final RestTemplate restTemplate;

    public TelegramAdminController(TelegramProperties props, RestTemplate restTemplate) {
        this.props = props;
        this.restTemplate = restTemplate;
    }

    @PostMapping("/set-users-webhook")
    public ResponseDto<String> setUsersWebhook() {
        String url = "https://api.telegram.org/bot"
                + props.getUsers().getToken()
                + "/setWebhook?url="
                + props.getBaseUrl() + "/api/telegram/users";

        restTemplate.getForObject(url, String.class);
        return ResponseDto.ok("Users webhook set");
    }

   /* @PostMapping("/set-teachers-webhook")
    public ResponseDto<String> setTeachersWebhook() {
        String url = "https://api.telegram.org/bot"
                + props.getTeachers().getToken()
                + "/setWebhook?url="
                + props.getBaseUrl() + "/api/telegram/teachers";

        restTemplate.getForObject(url, String.class);
        return ResponseDto.ok("Teachers webhook set");
    }*/
}
