package uz.zafar.onlinecourse.rest.sessionApi;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uz.zafar.onlinecourse.component.jwt.JwtService;
import uz.zafar.onlinecourse.dto.ResponseDtoNotData;
import uz.zafar.onlinecourse.service.SessionService;

@RestController
@RequestMapping("/api/sessions")
public class SessionRestController {

    @Autowired
    private SessionService sessionService;
    @Autowired
    private JwtService jwtService;

    @GetMapping
    public ResponseEntity<?> getMySessions(@RequestHeader("Authorization") String token) {
        try {
            String userId = jwtService.extractUsername(token); // sizning JWT'dan userId olish metod
            return ResponseEntity.ok(sessionService.getSessions(userId));
        }catch (Exception e) {
            return ResponseEntity.ok(new ResponseDtoNotData(false , e.getMessage()));
        }
    }

    @DeleteMapping("/{token}")
    public ResponseEntity<?> deleteSession(@PathVariable String token,
                                           @RequestHeader("Authorization") String authToken) {
        try {
            String userId = jwtService.extractUsername(authToken);
            return ResponseEntity.ok(sessionService.deleteSession(userId, token));
        } catch (Exception e) {
            return ResponseEntity.ok(new ResponseDtoNotData(false , e.getMessage()));
        }
    }
}

