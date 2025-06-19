package uz.zafar.onlinecourse.rest.admin;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import uz.zafar.onlinecourse.service.AuthService;
import uz.zafar.onlinecourse.service.HomeworkService;

@Tag(name = "Admin homework Controller", description = "Admin paneli uchun uyga vazifa bilan ishlash API'lari")
@RestController
@RequestMapping("/api/admin/homework/")
@RequiredArgsConstructor
public class AdminLessonHomeworkRestController {
    private final HomeworkService homeworkService ;
    private AuthService authService ;

}
