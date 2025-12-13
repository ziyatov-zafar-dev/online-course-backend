package uz.codebyz.onlinecoursebackend.telegram.bot.msg;

import org.springframework.stereotype.Component;

@Component
public class UserMsg {
    public String aboutAllCourses() {
        return "📚 Barcha kurslar\n\nO‘zingizga mos kursni tanlang va o‘rganishni boshlang 🚀";
    }

}
