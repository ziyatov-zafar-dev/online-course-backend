package uz.codebyz.onlinecoursebackend.telegram.bot.msg;

import org.springframework.stereotype.Component;
import uz.codebyz.onlinecoursebackend.course.entity.Course;
import uz.codebyz.onlinecoursebackend.telegram.bot.kyb.UserKyb;

import java.util.List;

@Component
public class UserMsg {
    private final UserKyb userKyb;

    public UserMsg(UserKyb userKyb) {
        this.userKyb = userKyb;
    }

    public String aboutAllCourses(List<Course> courses, int startIndex) {

        StringBuilder sb = new StringBuilder();
        sb.append("📚 *Barcha kurslar*\n\n");
        sb.append("Quyidagi kurslardan birini tanlang 🚀\n\n");

        for (int i = 0; i < courses.size(); i++) {
            sb.append(startIndex + i + 1)
                    .append(". ")
                    .append(courses.get(i).getName())
                    .append("\n");
        }
        return sb.toString();
    }


}
