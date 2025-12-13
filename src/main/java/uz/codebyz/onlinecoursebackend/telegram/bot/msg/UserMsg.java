package uz.codebyz.onlinecoursebackend.telegram.bot.msg;

import org.springframework.stereotype.Component;
import org.springframework.web.util.HtmlUtils;
import uz.codebyz.onlinecoursebackend.course.entity.Course;
import uz.codebyz.onlinecoursebackend.lesson.entity.Lesson;
import uz.codebyz.onlinecoursebackend.module.entity.Module;
import uz.codebyz.onlinecoursebackend.telegram.bot.kyb.UserKyb;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.NumberFormat;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Locale;

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


    /*public String aboutCourse(Course course) {

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
    }*/


    public String aboutCourse(Course course) {

        StringBuilder sb = new StringBuilder();

        sb.append("📘 <b>")
                .append(HtmlUtils.htmlEscape(course.getName()))
                .append("</b>\n\n");

        if (course.getDescription() != null && !course.getDescription().isBlank()) {
            sb.append("📝 ")
                    .append(HtmlUtils.htmlEscape(course.getDescription()))
                    .append("\n\n");
        }
        BigDecimal price = course.getPrice();
        BigDecimal finalPrice = course.getFinalPrice();

        LocalDateTime now = LocalDateTime.now();
        LocalDateTime start = course.getDiscountStartAt();
        LocalDateTime end = course.getDiscountEndAt();

        boolean discountActive =
                price != null
                        && finalPrice != null
                        && finalPrice.compareTo(price) < 0
                        && start != null
                        && end != null
                        && !now.isBefore(start)
                        && !now.isAfter(end);

        /* ================= PRICE BLOCK ================= */
        if (discountActive) {

            BigDecimal discountAmount = price.subtract(finalPrice);

            int discountPercent = discountAmount
                    .multiply(BigDecimal.valueOf(100))
                    .divide(price, 0, RoundingMode.HALF_UP)
                    .intValue();

            sb.append("💰 Narx: <s>")
                    .append(formatMoney(price))
                    .append("</s> → <b>")
                    .append(formatMoney(finalPrice))
                    .append("</b>\n");

            sb.append("🔥 Chegirma: ")
                    .append(discountPercent)
                    .append("% (−")
                    .append(formatMoney(discountAmount))
                    .append(")\n\n");

        } else if (price != null) {

            sb.append("💰 Narx: <b>")
                    .append(formatMoney(price))
                    .append("</b>\n\n");

        } else {

            sb.append("💰 Narx: <b>Bepul</b>\n\n");
        }
        int moduleCount = 0;
        int lessonCount = 0;
        if (course.getModules() != null) {
            for (Module module : course.getModules()) {

                if (Boolean.TRUE.equals(module.getActive())
                        && Boolean.FALSE.equals(module.getDeleted())) {

                    moduleCount++;

                    if (module.getLessons() != null) {
                        for (Lesson lesson : module.getLessons()) {

                            if (Boolean.TRUE.equals(lesson.getActive())
                                    && Boolean.FALSE.equals(lesson.getDeleted())) {
                                lessonCount++;
                            }
                        }
                    }
                }
            }
        }
        /* ================= INFO ================= */
        sb.append("📦 Modullar soni: ")
                .append(moduleCount)
                .append("\n");

        sb.append("📚 Darslar soni: ")
                .append(lessonCount)
                .append("\n");

        sb.append("\n👇 Kursni tanlash uchun pastdagi tugmalardan foydalaning");

        return sb.toString();
    }


    private String formatMoney(BigDecimal amount) {
        return NumberFormat
                .getInstance(new Locale("uz", "UZ"))
                .format(amount) + " so‘m";
    }
}
