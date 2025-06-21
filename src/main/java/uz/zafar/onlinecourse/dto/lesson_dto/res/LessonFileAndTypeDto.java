package uz.zafar.onlinecourse.dto.lesson_dto.res;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.UUID;


public interface LessonFileAndTypeDto {
    UUID getFileId();
    String getFileUrl();

    String getFileName();

    String getTypeId();

    String getTypeName();

    int getCreatedYear();

    int getCreatedMonth();

    int getCreatedDay();

    int getCreatedHour();

    int getCreatedMinute();

    int getCreatedSecond();
}
