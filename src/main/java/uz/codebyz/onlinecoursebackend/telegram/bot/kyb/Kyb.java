package uz.codebyz.onlinecoursebackend.telegram.bot.kyb;

import org.springframework.stereotype.Component;
import uz.codebyz.onlinecoursebackend.telegram.dto.ButtonDto;
import uz.codebyz.onlinecoursebackend.telegram.dto.ButtonType;

import java.util.List;

@Component
public class Kyb {
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
}
