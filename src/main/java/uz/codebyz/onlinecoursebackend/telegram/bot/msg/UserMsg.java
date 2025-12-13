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


    public String aboutCourse(Course course) {

        StringBuilder sb = new StringBuilder();

        sb.append("📘 *").append(course.getName()).append("*\n\n");

        // Tavsif
        if (course.getDescription() != null) {
            sb.append("📝 ").append(course.getDescription()).append("\n\n");
        }

        // Narxlar
        if (course.getFinalPrice() != null && course.getPrice() != null
                && course.getFinalPrice().compareTo(course.getPrice()) < 0) {

            sb.append("💰 Narx: ~")
                    .append(course.getPrice())
                    .append(" so‘m~\n");

            sb.append("🔥 Chegirmadagi narx: *")
                    .append(course.getFinalPrice())
                    .append(" so‘m*\n\n");

        } else if (course.getPrice() != null) {
            sb.append("💰 Narx: *")
                    .append(course.getPrice())
                    .append(" so‘m*\n\n");
        }

        // Telegram linklar
        if (Boolean.TRUE.equals(course.getHasTelegramGroup())
                && course.getTelegramGroupLink() != null) {
            sb.append("👥 Guruh: ")
                    .append(course.getTelegramGroupLink())
                    .append("\n");
        }

        if (Boolean.TRUE.equals(course.getHasTelegramChannel())
                && course.getTelegramChannelLink() != null) {
            sb.append("📢 Kanal: ")
                    .append(course.getTelegramChannelLink())
                    .append("\n");
        }

        // Modul va skill soni
        if (course.getModules() != null && !course.getModules().isEmpty()) {
            sb.append("\n📦 Modullar soni: ")
                    .append(course.getModules().size());
        }

        if (course.getSkills() != null && !course.getSkills().isEmpty()) {
            sb.append("\n🎯 O‘rganiladigan skilllar: ")
                    .append(course.getSkills().size());
        }

        sb.append("\n\n👇 Kursni tanlash uchun pastdagi tugmalardan foydalaning");

        return sb.toString();
    }

}
