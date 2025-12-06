package uz.codebyz.onlinecoursebackend.admin.homework.mapper;

import org.springframework.data.domain.Page;
import uz.codebyz.onlinecoursebackend.admin.course.dto.UploadFileResponseDto;
import uz.codebyz.onlinecoursebackend.admin.homework.dto.AdminHomeworkFileResponseDto;
import uz.codebyz.onlinecoursebackend.helper.FileHelper;
import uz.codebyz.onlinecoursebackend.homework.entity.Homework;
import uz.codebyz.onlinecoursebackend.homeworkfile.entity.HomeworkFile;

import java.util.List;
import java.util.stream.Collectors;

public class AdminHomeworkFileMapper {
    public static AdminHomeworkFileResponseDto toDto(HomeworkFile file) {
        return new AdminHomeworkFileResponseDto(
                file.getId(),
                file.getFileName(),
                file.getFileUrl(),
                file.getFileSize(),
                FileHelper.getFileSize(file.getFileSize())
        );
    }

    public static List<AdminHomeworkFileResponseDto> toDto(List<HomeworkFile> files) {
        return files.stream().map(AdminHomeworkFileMapper::toDto).collect(Collectors.toList());
    }

    public static Page<AdminHomeworkFileResponseDto> toDto(Page<HomeworkFile> files) {
        return files.map(AdminHomeworkFileMapper::toDto);
    }

    public static HomeworkFile addHomeworkFileMapper(UploadFileResponseDto req, Homework homework) {
        HomeworkFile h = new HomeworkFile();
        h.setFileName(req.getFileName());
        h.setFileUrl(req.getFileUrl());
        h.setFileSize(req.getFileSize());
        h.setActive(true);
        h.setHomework(homework);
        h.setExt(FileHelper.getFileExt(req.getFileName()));
        return h;
    }

}
