package uz.codebyz.onlinecoursebackend.telegram.controller;

import io.swagger.v3.oas.annotations.Hidden;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uz.codebyz.onlinecoursebackend.telegram.service.TelegramUpdateService;


import java.util.Map;

@RestController
@Hidden
@RequestMapping("/api/telegram")
public class TelegramUpdateController {

    private final TelegramUpdateService service;

    public TelegramUpdateController(TelegramUpdateService service) {
        this.service = service;
    }

    @PostMapping("/users")
    public ResponseEntity<Void> users(@RequestBody Map<String, Object> update) {
        service.handleUsers(update);
        return ResponseEntity.ok().build();
    }
}
