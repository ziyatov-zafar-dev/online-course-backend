package uz.zafar.onlinecourse.rest.student;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import uz.zafar.onlinecourse.dto.homework_submission_dto.req.AddHomeworkSubmissionDto;
import uz.zafar.onlinecourse.service.HomeworkSubmissionService;

import java.util.UUID;

@RequestMapping("/api/student/homework-submission")
@RequiredArgsConstructor
@RestController
@Tag(name = "Student Homework Submission Controller", description = "Student paneli uchun uyga vazifalar bilan ishlash API'lari")
public class StudentHomeworkSubmissionRestController {
    private final HomeworkSubmissionService homeworkSubmissionService;

    @PostMapping("add-homework-submission")
    public ResponseEntity<?> addHomeworkSubmission(
            @RequestParam MultipartFile file,
            @RequestParam Long studentId,
            @RequestParam Short typeId,
            @RequestParam UUID homeworkId,
            HttpServletRequest request
    ) throws Exception {
        AddHomeworkSubmissionDto homeworkSubmission = new AddHomeworkSubmissionDto();
        homeworkSubmission.setStudentId(studentId);
        homeworkSubmission.setHomeworkId(homeworkId);
        homeworkSubmission.setFile(file);
        homeworkSubmission.setTypeId(typeId);

        return ResponseEntity.ok(homeworkSubmissionService.addHomeworkSubmission(homeworkSubmission, request));
    }

    @GetMapping("add-homework-submission")
    public ResponseEntity<?> getHomeworkSubmissionByHomeworkIdAndStudentId(
            @RequestParam UUID homeworkId, @RequestParam Long studentID
    ) throws Exception {
        return ResponseEntity.ok(
                homeworkSubmissionService.getHomeworkSubmissionByHomeworkIdAndStudentId(homeworkId, studentID)
        );
    }

    @DeleteMapping("remove-homework-submission-by-homework-id")
    public ResponseEntity<?> removeHomeworkSubmissionByHomeworkId(
            @RequestParam UUID homeworkId, @RequestParam Long studentID
    ) throws Exception {
        return ResponseEntity.ok(
                homeworkSubmissionService.removeHomeworkSubmissionByHomeworkId(homeworkId, studentID)
        );
    }
}
