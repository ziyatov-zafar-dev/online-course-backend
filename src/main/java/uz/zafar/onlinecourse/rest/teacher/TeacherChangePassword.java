package uz.zafar.onlinecourse.rest.teacher;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import uz.zafar.onlinecourse.dto.ResponseDto;
import uz.zafar.onlinecourse.dto.form.ChangePasswordForm;
import uz.zafar.onlinecourse.service.UserService;

@RequestMapping("/api/teacher/change-password")
@RequiredArgsConstructor
@RestController
@Tag(name = "Teacher Change Password Controller", description = "Teacher paneli uchun parollar bilan ishlash API'lari")
public class TeacherChangePassword {
    private final UserService userService;
    @PutMapping("{userId}")
    @Operation(summary = "change password" , description = "Parolni o'zgartirish uchun API")
    public ResponseDto<?> changePassword(@RequestBody ChangePasswordForm form, @PathVariable Long userId) {
        return userService.changePassword(form, userId);
    }
}
