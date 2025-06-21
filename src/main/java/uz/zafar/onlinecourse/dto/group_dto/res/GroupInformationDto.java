package uz.zafar.onlinecourse.dto.group_dto.res;

import java.util.Date;

public interface GroupInformationDto {
    long getStudentCount();

    long getLessonCount();

    Date getCreatedGroup();

    Date getLastEdited();

}
