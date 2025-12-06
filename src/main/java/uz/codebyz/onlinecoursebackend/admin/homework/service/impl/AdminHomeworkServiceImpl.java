package uz.codebyz.onlinecoursebackend.admin.homework.service.impl;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import uz.codebyz.onlinecoursebackend.admin.course.dto.UploadFileResponseDto;
import uz.codebyz.onlinecoursebackend.admin.homework.dto.AdminAddHomeworkRequestDto;
import uz.codebyz.onlinecoursebackend.admin.homework.dto.AdminHomeworkFileResponseDto;
import uz.codebyz.onlinecoursebackend.admin.homework.dto.AdminHomeworkResponseDto;
import uz.codebyz.onlinecoursebackend.admin.homework.dto.AdminUpdateHomeworkRequestDto;
import uz.codebyz.onlinecoursebackend.admin.homework.mapper.AdminHomeworkFileMapper;
import uz.codebyz.onlinecoursebackend.admin.homework.mapper.AdminHomeworkMapper;
import uz.codebyz.onlinecoursebackend.admin.homework.service.AdminHomeworkService;
import uz.codebyz.onlinecoursebackend.common.ResponseDto;
import uz.codebyz.onlinecoursebackend.helper.CurrentTime;
import uz.codebyz.onlinecoursebackend.helper.FileHelper;
import uz.codebyz.onlinecoursebackend.homework.entity.Homework;
import uz.codebyz.onlinecoursebackend.homework.repository.HomeworkRepository;
import uz.codebyz.onlinecoursebackend.homeworkfile.entity.HomeworkFile;
import uz.codebyz.onlinecoursebackend.homeworkfile.repository.HomeworkFileRepository;
import uz.codebyz.onlinecoursebackend.lesson.entity.Lesson;
import uz.codebyz.onlinecoursebackend.lesson.repository.LessonRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class AdminHomeworkServiceImpl implements AdminHomeworkService {
    private final HomeworkFileRepository homeworkFileRepository;
    @Value("${course.homework.file.upload}")
    private String uploadHomeworkBasePath;
    private final HomeworkRepository homeworkRepository;
    private final LessonRepository lessonRepository;

    public AdminHomeworkServiceImpl(HomeworkRepository homeworkRepository, LessonRepository lessonRepository, HomeworkFileRepository homeworkFileRepository) {
        this.homeworkRepository = homeworkRepository;
        this.lessonRepository = lessonRepository;
        this.homeworkFileRepository = homeworkFileRepository;
    }

    @Override
    public ResponseDto<AdminHomeworkResponseDto> setHomework(AdminAddHomeworkRequestDto req) {
        Optional<Lesson> lOp = lessonRepository.findById(req.getLessonId());
        if (lOp.isEmpty())
            return new ResponseDto<>(false, "Lesson Not Found");

        if (lOp.get().getHomework() != null) {
            return new ResponseDto<>(false, "Bu darsga uyga vazifa qo'sha olmaysiz, faqat uni o'zgartira olasiz");
        }
        Homework homework = homeworkRepository.save(AdminHomeworkMapper.addHomeworkMapper(req, lOp.get()));
        Lesson lesson = lOp.get();
        lesson.setHomework(homework);
        lessonRepository.save(lesson);
        for (MultipartFile file : req.getFiles()) {
            try {
                UploadFileResponseDto data = FileHelper.uploadFile(file, uploadHomeworkBasePath, false).getData();
                HomeworkFile save = homeworkFileRepository.save(
                        AdminHomeworkFileMapper.addHomeworkFileMapper(data, homework)
                );
                homework.getHomeworkFiles().add(save);
            } catch (Exception ignored) {

            }
        }
        homework.setHasFiles(!req.getFiles().isEmpty());
        return new ResponseDto<>(true, "Success", AdminHomeworkMapper.toDto(
                homeworkRepository.save(homework)
        ));
    }

    @Override
    public ResponseDto<AdminHomeworkResponseDto> findById(UUID homeworkId) {
        Optional<Homework> hOp = homeworkRepository.getHomeworkById(homeworkId);
        return hOp.map(homework -> new ResponseDto<>(true, "Success", AdminHomeworkMapper.toDto(homework))).orElseGet(() -> new ResponseDto<>(false, "Homework Not Found"));
    }

    @Override
    public ResponseDto<AdminHomeworkResponseDto> changeHomework(UUID homeworkId, AdminUpdateHomeworkRequestDto req) {
        Optional<Homework> hOp = homeworkRepository.getHomeworkById(homeworkId);
        if (hOp.isEmpty())
            return new ResponseDto<>(false, "Homework Not Found");
        Homework homework = hOp.get();
        List<HomeworkFile> activeFiles = homework.getHomeworkFiles().stream().filter(HomeworkFile::getActive).toList();
        if (!activeFiles.isEmpty()) {
            for (HomeworkFile homeworkFile : activeFiles) {
                homeworkFile.setActive(false);
                FileHelper.deleteFile(homeworkFile.getFileUrl());
            }
        }
        for (MultipartFile file : req.getFiles()) {
            ResponseDto<UploadFileResponseDto> savedFile = FileHelper.uploadFile(file, uploadHomeworkBasePath, false);
            if (savedFile.isSuccess()) {
                UploadFileResponseDto data = savedFile.getData();
                HomeworkFile homeworkFile = AdminHomeworkFileMapper.addHomeworkFileMapper(data, homework);
                homeworkFileRepository.save(homeworkFile);
                homework.getHomeworkFiles().add(homeworkFile);
            }
        }
        homework.setHasFiles(!req.getFiles().isEmpty());
        homework.setTitle(req.getTitle());
        homework.setDescription(req.getDescription());
        homework.setMinScore(req.getMinScore());
        homework.setMaxScore(req.getMaxScore());
        homework = homeworkRepository.save(homework);
        return new ResponseDto<>(true, "Success", AdminHomeworkMapper.toDto(homework));
    }

    @Override
    public ResponseDto<AdminHomeworkResponseDto> changeMaxBall(UUID homeworkId, Integer newMaxBall) {
        Optional<Homework> hOp = homeworkRepository.getHomeworkById(homeworkId);
        if (hOp.isEmpty())
            return new ResponseDto<>(false, "Homework Not Found");
        Homework homework = hOp.get();
        homework.setUpdated(CurrentTime.currentTime());
        homework.setMaxScore(newMaxBall);
        return new ResponseDto<>(true, "Suceess", AdminHomeworkMapper.toDto(homeworkRepository.save(homework)));
    }

    @Override
    public ResponseDto<AdminHomeworkResponseDto> changeMinBall(UUID homeworkId, Integer newMinBall) {
        Optional<Homework> hOp = homeworkRepository.getHomeworkById(homeworkId);
        if (hOp.isEmpty())
            return new ResponseDto<>(false, "Homework Not Found");
        Homework homework = hOp.get();
        homework.setUpdated(CurrentTime.currentTime());
        homework.setMinScore(newMinBall);
        return new ResponseDto<>(true, "Suceess", AdminHomeworkMapper.toDto(homeworkRepository.save(homework)));
    }
}
