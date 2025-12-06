package uz.codebyz.onlinecoursebackend.admin.homework.mapper;

import org.springframework.data.domain.Page;
import uz.codebyz.onlinecoursebackend.admin.homework.dto.AdminAddHomeworkRequestDto;
import uz.codebyz.onlinecoursebackend.admin.homework.dto.AdminHomeworkResponseDto;
import uz.codebyz.onlinecoursebackend.helper.CurrentTime;
import uz.codebyz.onlinecoursebackend.homework.entity.Homework;
import uz.codebyz.onlinecoursebackend.homeworkfile.entity.HomeworkFile;
import uz.codebyz.onlinecoursebackend.lesson.entity.Lesson;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class AdminHomeworkMapper {
    public static AdminHomeworkResponseDto toDto(Homework homework) {
        AdminHomeworkResponseDto adminHomeworkResponseDto = new AdminHomeworkResponseDto();
        adminHomeworkResponseDto.setHomeworkId(homework.getId());
        adminHomeworkResponseDto.setTitle(homework.getTitle());
        adminHomeworkResponseDto.setDescription(homework.getDescription());
        adminHomeworkResponseDto.setHasFiles(homework.getHasFiles());
        adminHomeworkResponseDto.setMaxScore(homework.getMaxScore());
        adminHomeworkResponseDto.setHasText(homework.getHasText());
        adminHomeworkResponseDto.setMinScore(homework.getMinScore());
        adminHomeworkResponseDto.setCreated(homework.getCreated());
        adminHomeworkResponseDto.setUpdated(homework.getUpdated());
        adminHomeworkResponseDto.setHomeworkFiles(
                AdminHomeworkFileMapper.toDto(
                        homework.getHomeworkFiles().stream()
                                .filter(HomeworkFile::getActive).toList())
        );
        return adminHomeworkResponseDto;
    }

    public static List<AdminHomeworkResponseDto> toDto(List<Homework> homeworks) {
        return homeworks.stream().map(AdminHomeworkMapper::toDto).collect(Collectors.toList());
    }

    public static Page<AdminHomeworkResponseDto> toDto(Page<Homework> homeworks) {
        return homeworks.map(AdminHomeworkMapper::toDto);
    }

    public static Homework addHomeworkMapper(AdminAddHomeworkRequestDto req, Lesson lesson) {
        Homework homework = new Homework();
        homework.setTitle(req.getTitle());
        homework.setDescription(req.getDescription());
        homework.setHasText(false);
        homework.setHasFiles(false);
        homework.setActive(true);
        homework.setMaxScore(req.getMaxScore());
        homework.setMinScore(req.getMinScore());
        homework.setCreated(CurrentTime.currentTime());
        homework.setUpdated(CurrentTime.currentTime());
        homework.setLesson(lesson);
        homework.setHomeworkFiles(new ArrayList<>());
        return homework;
    }
}
