package uz.zafar.onlinecourse.helper;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Date;

public class TimeUtil {
    public static Date currentTashkentTime() {
        return Date.from(ZonedDateTime.now(ZoneId.of("Asia/Tashkent")).toInstant());
    }
}