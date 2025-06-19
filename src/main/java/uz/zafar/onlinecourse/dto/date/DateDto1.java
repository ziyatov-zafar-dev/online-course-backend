package uz.zafar.onlinecourse.dto.date;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DateDto1 {
    private int year;
    private int month;
    private int day;
    private int hour;
    private int minute;
    private int second;
    public DateDto1(int year, int month, int day, int hour, int minute, int second) {
        this.year = year;
        this.month = month;
        this.day = day;
        this.hour = hour;
        this.minute = minute;
        this.second = second;
    }
}
