package uz.zafar.onlinecourse.service.impl;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import uz.zafar.onlinecourse.db.domain.*;
import uz.zafar.onlinecourse.db.repository.*;
import uz.zafar.onlinecourse.dto.ResponseDto;
import uz.zafar.onlinecourse.dto.homework_submission_dto.req.AddHomeworkSubmissionDto;
import uz.zafar.onlinecourse.dto.homework_submission_dto.res.SubmissionDto;
import uz.zafar.onlinecourse.dto.homework_submission_file_dto.res.HomeworkSubmissionFileDto;
import uz.zafar.onlinecourse.helper.TimeUtil;
import uz.zafar.onlinecourse.service.AuthService;
import uz.zafar.onlinecourse.service.HomeworkSubmissionService;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@Log4j2
@RequiredArgsConstructor
public class HomeworkSubmissionServiceImpl implements HomeworkSubmissionService {


    private final StudentRepository studentRepository;
    private final HomeworkRepository homeworkRepository;
    private final HomeworkSubmissionRepository homeworkSubmissionRepository;
    private final TypeRepository typeRepository;
    private final AuthService authService;
    private final HomeworkSubmissionFileRepository homeworkSubmissionFileRepository;

    @Override
    public ResponseDto<HomeworkSubmission> addHomeworkSubmission(AddHomeworkSubmissionDto dto, HttpServletRequest req) throws Exception {
        try {
            Optional<Student> sOp = studentRepository.findById(dto.getStudentId());
            Optional<Homework> hOp = homeworkRepository.findById(dto.getHomeworkId());
            if (sOp.isEmpty()) {
                throw new Exception("Not found student id " + dto.getStudentId());
            }
            if (hOp.isEmpty()) {
                throw new Exception("Not found homework id " + dto.getHomeworkId());
            }
            Student student = sOp.get();
            Homework homework = hOp.get();

            if (homework.getDeadline().before(TimeUtil.currentTashkentTime())) {
                throw new Exception("Deadline is passed. Deadline time: " + homework.getDeadline());
            }
            HomeworkSubmission checkSub = homeworkSubmissionRepository.findByStudentAndHomework(student.getId(), homework.getId());


            String url = "uploads/files/homework-submission";
            Path directoryPath = Paths.get(url);
            if (!Files.exists(directoryPath)) {
                Files.createDirectories(directoryPath);
            }
            Optional<Type> tOp = typeRepository.findById(dto.getTypeId());
            if (tOp.isEmpty()) throw new Exception("Not found type id " + dto.getTypeId());
            Type type = tOp.get();
            HomeworkSubmissionFile submissionFile = null;
            MultipartFile file = dto.getFile();
            if (file.getOriginalFilename() != null && !file.getOriginalFilename().isEmpty()) {
                String fileName = file.getOriginalFilename();
                String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(TimeUtil.currentTashkentTime());
                fileName = fileName.substring(0, fileName.length() - 4).replace(" ", "_").replace("+", "_").replace("-", "_");
                fileName = fileName.concat(timeStamp + file.getOriginalFilename().substring(file.getOriginalFilename().length() - 4));
                if (!type.getName().substring(type.getName().length() - 3).equals(fileName.substring(fileName.length() - 3))) {
                    throw new Exception("Not equals type name " + type.getName());
                }
                Path filePath = directoryPath.resolve(fileName);
                Files.write(filePath, file.getBytes());
                String fileUrl = authService.getHostUrl(req) + "/files/homework-submission/" + fileName;
                submissionFile = new HomeworkSubmissionFile();
                submissionFile.setFileUrl(fileUrl);
                submissionFile.setFileName(fileName);
                submissionFile.setCreated(TimeUtil.currentTashkentTime());
                submissionFile.setType(type);
                submissionFile.setActive(true);
            }
            if (checkSub != null) {
                throw new Exception("Student id " + dto.getStudentId() + " already exists");
            }
            if (submissionFile == null) {
                throw new Exception("Not found submission file");
            }
            checkSub = new HomeworkSubmission();
            checkSub.setHomeworkId(homework.getId());
            checkSub.setStudentId(student.getId());
            checkSub.setActive(true);
            checkSub.setCreated(TimeUtil.currentTashkentTime());
            checkSub.setLessonId(homework.getLesson().getId());
            HomeworkSubmission save = homeworkSubmissionRepository.save(checkSub);
            submissionFile.setHomeworkSubmissionId(save.getId());
            homeworkSubmissionFileRepository.save(submissionFile);
            return new ResponseDto<>(true, "Successfully added homework submission", save);
        } catch (Exception e) {
            log.error(e);
            return new ResponseDto<>(false, e.getMessage());
        }
    }

    @Override
    public ResponseDto<SubmissionDto> getHomeworkSubmissionByHomeworkIdAndStudentId(UUID homeworkId, Long studentId) {
        try {
            if (homeworkRepository.findById(homeworkId).isEmpty()) {
                throw new Exception("The homework was not done");
            }
            if (studentRepository.findById(studentId).isEmpty()) {
                throw new Exception("The student was not done");
            }
            HomeworkSubmission homeworkSubmission = homeworkSubmissionRepository.findByHomerworkSubmissionHomeworkIdAndStudentId(homeworkId, studentId);
            if (homeworkSubmission == null) {
                throw new Exception("student not found");
            }
            SubmissionDto dto = new SubmissionDto();
            dto.setHomeworkId(homeworkSubmission.getHomeworkId());
            dto.setStudentId(homeworkSubmission.getStudentId());
            dto.setLessonId(homeworkSubmission.getLessonId());
            dto.setSubmissionId(homeworkSubmission.getId());
            List<HomeworkSubmissionFile> list = homeworkSubmissionFileRepository.getHomeworkFiles(homeworkSubmission.getId());
            if (!list.isEmpty()) {
                HomeworkSubmissionFile file = list.get(0);
                dto.setFile(HomeworkSubmissionFileDto.builder().typeId(file.getType().getId()).pkey(homeworkSubmission.getId()).fileUrl(file.getFileUrl()).created(file.getCreated()).fileName(file.getFileName()).homeworkSubmissionId(homeworkSubmission.getId()).build());
            }
            return new ResponseDto<>(true, "Successfully loaded homework submission", dto);
        } catch (Exception e) {
            log.error(e);
            return new ResponseDto<>(false, e.getMessage());
        }
    }

    @Override
    public ResponseDto<Void> removeHomeworkSubmissionByHomeworkId(UUID homeworkId, Long studentId) throws Exception {
        try {
            Optional<Homework> hOp = homeworkRepository.findById(homeworkId);
            if (hOp.isEmpty()) {
                throw new Exception("The homework was not done");
            }
            Homework homework = hOp.get();
            if (homework.getDeadline().before(TimeUtil.currentTashkentTime())) {
                throw new Exception("Deadline is passed. Deadline time: " + homework.getDeadline());
            }
            if (studentRepository.findById(studentId).isEmpty()) {
                throw new Exception("The student was not done");
            }
            HomeworkSubmission homeworkSubmission = homeworkSubmissionRepository.findByHomerworkSubmissionHomeworkIdAndStudentId(homeworkId, studentId);
            if (homeworkSubmission == null) {
                throw new Exception("homeworkSubmission not found");
            }
            homeworkSubmission.setActive(false);
            homeworkSubmissionRepository.save(homeworkSubmission);
            return new ResponseDto<>(true, "Successfully removed homework submission");
        } catch (Exception e) {
            log.error(e);
            return new ResponseDto<>(false, e.getMessage());
        }
    }
}