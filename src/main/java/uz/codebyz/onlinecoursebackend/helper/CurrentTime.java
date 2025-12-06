package uz.codebyz.onlinecoursebackend.helper;

import java.time.LocalDateTime;
import java.time.ZoneId;

public class CurrentTime {
    public static LocalDateTime currentTime() {
        return LocalDateTime.now(ZoneId.of("Asia/Tashkent"));
    }
}
