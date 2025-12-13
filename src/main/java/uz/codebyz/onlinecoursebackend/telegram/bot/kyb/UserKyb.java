package uz.codebyz.onlinecoursebackend.telegram.bot.kyb;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import uz.codebyz.onlinecoursebackend.course.entity.Course;
import uz.codebyz.onlinecoursebackend.telegram.dto.ButtonDto;
import uz.codebyz.onlinecoursebackend.telegram.dto.ButtonType;

import java.util.ArrayList;
import java.util.List;

@Component
public class UserKyb {
    @Value("${courses.list.size}")
    private int size;

    public List<List<ButtonDto>> menu() {
        return List.of(
                List.of(
                        new ButtonDto("\uD83D\uDCDA Barcha kurslar", ButtonType.INLINE, "all_courses"),
                        new ButtonDto("\uD83D\uDCDA Mening kurslarim", ButtonType.INLINE, "my_courses")
                ),
                List.of(
                        new ButtonDto("\uD83C\uDFC6 Sertifikatlarim", ButtonType.INLINE, "my_certificates"),
                        new ButtonDto("\uD83D\uDCB3 To‘lovlar tarixi", ButtonType.INLINE, "all_payment")
                ),
                List.of(
                        new ButtonDto("\uD83D\uDCDE Yordam / Support", ButtonType.INLINE, "support"),
                        new ButtonDto("ℹ️ Platforma haqida", ButtonType.INLINE, "about_platform")
                )
        );
    }

    public List<Course> paginate(List<Course> courses, int page, int size) {

        if (courses == null || courses.isEmpty()) {
            return List.of();
        }

        int fromIndex = page * size;
        int toIndex = Math.min(fromIndex + size, courses.size());

        if (fromIndex >= courses.size()) {
            return List.of();
        }

        return courses.subList(fromIndex, toIndex);
    }

    public List<List<ButtonDto>> getAllCourses(List<Course> allCourses, String data) {

        int size = 5;
        int page = 0;

        if (data != null && data.startsWith("course_page_")) {
            page = Integer.parseInt(data.replace("course_page_", ""));
        }

        List<Course> courses = paginate(allCourses, page, size);

        List<List<ButtonDto>> rows = new ArrayList<>();
        List<ButtonDto> row = new ArrayList<>();

        int startIndex = page * size;

        /* ================== COURSE BUTTONS ================== */
        for (int i = 0; i < courses.size(); i++) {

            ButtonDto button = new ButtonDto(
                    String.valueOf(startIndex + i + 1),   // 1,2,3,4...
                    ButtonType.INLINE,
                    String.valueOf(courses.get(i).getId()) // courseId
            );
            row.add(button);
            if (row.size() == 2) {
                rows.add(row);
                row = new ArrayList<>();
            }
        }

        // oxirgi qator
        if (!row.isEmpty()) {
            rows.add(row);
        }

        /* ================== PAGINATION ================== */
        List<ButtonDto> navRow = new ArrayList<>();

        if (page > 0) {
            navRow.add(new ButtonDto(
                    "⬅️ Oldingisi",
                    ButtonType.INLINE,
                    "course_page_" + (page - 1)
            ));
        }

        if ((page + 1) * size < allCourses.size()) {
            navRow.add(new ButtonDto(
                    "Keyingisi ➡️",
                    ButtonType.INLINE,
                    "course_page_" + (page + 1)
            ));
        }

        if (!navRow.isEmpty()) {
            rows.add(navRow);
        }

        return rows;
    }

}
