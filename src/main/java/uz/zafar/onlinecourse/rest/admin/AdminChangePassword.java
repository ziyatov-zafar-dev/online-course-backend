package uz.zafar.onlinecourse.rest.admin;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import uz.zafar.onlinecourse.dto.ResponseDto;
import uz.zafar.onlinecourse.dto.form.ChangePasswordForm;
import uz.zafar.onlinecourse.service.UserService;

@RequestMapping("/api/admin/change-password")
@RequiredArgsConstructor
@RestController
@Tag(name = "Admin change password Controller", description = "Admin paneli uchun parollar bilan ishlash API'lari")

public class AdminChangePassword {
    private final UserService userService;
    @PutMapping("{userId}")
    public ResponseDto<?> changePassword(@RequestBody ChangePasswordForm form, @PathVariable Long userId) {
        return userService.changePassword(form, userId);
    }
}
