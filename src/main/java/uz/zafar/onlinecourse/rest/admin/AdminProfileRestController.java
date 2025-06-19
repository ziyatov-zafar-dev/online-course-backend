package uz.zafar.onlinecourse.rest.admin;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uz.zafar.onlinecourse.db.domain.User;
import uz.zafar.onlinecourse.dto.ResponseDto;
import uz.zafar.onlinecourse.dto.ResponseDtoNotData;
import uz.zafar.onlinecourse.dto.form.EditProfile;
import uz.zafar.onlinecourse.service.UserService;

@RestController
@RequestMapping("api/admin/profile")
public class AdminProfileRestController {
    private final UserService userService;

    public AdminProfileRestController(UserService userService) {
        this.userService = userService;
    }

    @PutMapping("change-username")
    public ResponseEntity<ResponseDto<?>> changeUsername(@RequestParam String username) {
        return ResponseEntity.ok(userService.changeUsername(username));
    }

    @PutMapping("change-firstname-lastname-email")
    public ResponseEntity<ResponseDto<?>> changeProfile(@RequestBody EditProfile profile) {
        return ResponseEntity.ok(userService.changeProfile(profile));
    }
}
