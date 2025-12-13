package uz.codebyz.onlinecoursebackend.telegram.bot.msg;

import org.springframework.stereotype.Component;
import uz.codebyz.onlinecoursebackend.course.entity.Course;
import uz.codebyz.onlinecoursebackend.telegram.bot.kyb.UserKyb;

import java.math.BigDecimal;
import java.math.RoundingMode;
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
    }*/public String aboutCourse(Course course) {

        StringBuilder sb = new StringBuilder();

        sb.append("📘 *").append(course.getName()).append("*\n\n");

        // Tavsif
        if (course.getDescription() != null && !course.getDescription().isBlank()) {
            sb.append("📝 ").append(course.getDescription()).append("\n\n");
        }

        BigDecimal price = course.getPrice();
        BigDecimal finalPrice = course.getFinalPrice();

        // ===== NARX BLOKI =====
        if (price != null && finalPrice != null && finalPrice.compareTo(price) < 0) {

            BigDecimal discountAmount = price.subtract(finalPrice);

            Integer discountPercent = course.getDiscountPercent();
            if (discountPercent == null) {
                discountPercent = discountAmount
                        .multiply(BigDecimal.valueOf(100))
                        .divide(price, 0, RoundingMode.HALF_UP)
                        .intValue();
            }

            sb.append("💰 Narx: ~")
                    .append(price.toPlainString())
                    .append(" so‘m~  →  *")
                    .append(finalPrice.toPlainString())
                    .append(" so‘m*\n");

            sb.append("🔥 Chegirma: ")
                    .append(discountPercent)
                    .append("%  (−")
                    .append(discountAmount.toPlainString())
                    .append(" so‘m)\n\n");

        } else if (price != null) {

            sb.append("💰 Narx: *")
                    .append(price.toPlainString())
                    .append(" so‘m*\n\n");
        }

        // ===== MODUL / SKILL =====
        if (course.getModules() != null && !course.getModules().isEmpty()) {
            sb.append("📦 Modullar soni: ")
                    .append(course.getModules().size())
                    .append("\n");
        }

        if (course.getSkills() != null && !course.getSkills().isEmpty()) {
            sb.append("🎯 O‘rganiladigan skilllar: ")
                    .append(course.getSkills().size())
                    .append("\n");
        }

        sb.append("\n👇 Kursni tanlash uchun pastdagi tugmalardan foydalaning");

        return sb.toString();
    }


}
