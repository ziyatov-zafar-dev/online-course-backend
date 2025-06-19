package uz.zafar.onlinecourse.dto.lesson_dto.res;

import com.fasterxml.jackson.annotation.JsonProperty;


public interface LessonFileAndTypeDto {
    @JsonProperty("file-url")
    String getFileUrl();
    @JsonProperty("file-name")
    String getFileName();
    @JsonProperty("file-type-id")
    String getTypeId();
    @JsonProperty("file-type-name")
    String getTypeName();
    @JsonProperty("created-year")
    int getCreatedYear();
    @JsonProperty("created-month")
    int getCreatedMonth();
    @JsonProperty("created-day")
    int getCreatedDay();
    @JsonProperty("created-hour")
    int getCreatedHour();
    @JsonProperty("created-minute")
    int getCreatedMinute();
    @JsonProperty("created-second")
    int getCreatedSecond();
}
