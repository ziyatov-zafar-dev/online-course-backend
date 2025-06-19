package uz.zafar.onlinecourse.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import uz.zafar.onlinecourse.service.UserService;
//import uz.farobiy.lesson_11_backend.service.UserService;

@RestController
@RequestMapping("api/user-info")
public class UserInfoController {
    @Autowired
    private UserService userService;
    @GetMapping("/get-user")
    public ResponseEntity<?> getUserInfo() throws Exception {
        return ResponseEntity.ok(userService.currentUserInfo());
    }
}