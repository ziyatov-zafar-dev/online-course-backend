package uz.zafar.onlinecourse.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import uz.zafar.onlinecourse.db.domain.Lesson;
import uz.zafar.onlinecourse.db.domain.Review;
import uz.zafar.onlinecourse.db.domain.Student;
import uz.zafar.onlinecourse.db.repository.GroupRepository;
import uz.zafar.onlinecourse.db.repository.LessonRepository;
import uz.zafar.onlinecourse.db.repository.ReviewRepository;
import uz.zafar.onlinecourse.db.repository.StudentRepository;
import uz.zafar.onlinecourse.dto.ResponseDto;
import uz.zafar.onlinecourse.dto.review_dto.req.AddReviewDto;
import uz.zafar.onlinecourse.dto.review_dto.res.LessonReviewDto;
import uz.zafar.onlinecourse.dto.review_dto.res.ReviewDto;
import uz.zafar.onlinecourse.dto.student_dto.res.StudentDto;
import uz.zafar.onlinecourse.helper.TimeUtil;
import uz.zafar.onlinecourse.service.LessonService;
import uz.zafar.onlinecourse.service.ReviewService;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@Log4j2
@RequiredArgsConstructor
public class ReviewServiceImpl implements ReviewService {
    private final ReviewRepository reviewRepository;
    private final StudentRepository studentRepository;
    private final LessonRepository lessonRepository;
    private final GroupRepository groupRepository;
    private final LessonService lessonService;

    @Override
    public ResponseDto<ReviewDto> addReview(AddReviewDto dto) {
        try {
            if (studentRepository.findById(dto.getStudentId()).isEmpty()) {
                throw new Exception("Not found student with id " + dto.getStudentId());
            }
            if (lessonRepository.findById(dto.getLessonId()).isEmpty()) {
                throw new Exception("Not found lesson with id " + dto.getLessonId());
            }

            Review issetReview = reviewRepository.findByStudentIdAndLessonId(dto.getStudentId(), dto.getLessonId());
            if (issetReview != null) {
                throw new Exception("reviewed already");
            }

            return new ResponseDto<>(true, "Successfully added review student",
                    mapReviewToDto(reviewRepository.save(
                            Review
                                    .builder()
                                    .reviewAt(TimeUtil.currentTashkentTime())
                                    .studentId(dto.getStudentId())
                                    .lessonId(dto.getLessonId())
                                    .build()
                    )));
        } catch (Exception e) {
            log.error(e);
            return new ResponseDto<>(false, e.getMessage());
        }
    }

    private ReviewDto mapReviewToDto(Review review) {
        return
                ReviewDto
                        .builder()
                        .id(review.getId())
                        .studentId(review.getStudentId())
                        .lessonId(review.getLessonId())
                        .reviewDate(review.getReviewAt())
                        .build();
    }
    @Override
    public ResponseDto<ReviewDto> getReview(UUID reviewId) {
        try {
            Optional<Review> rOp = reviewRepository.findById(reviewId);
            if (rOp.isPresent()) {
                return new ResponseDto<>(true, "Ok", mapReviewToDto(rOp.get()));
            }
            throw new Exception("Not found review with id " + reviewId);
        } catch (Exception e) {
            log.error(e);
            return new ResponseDto<>(false, e.getMessage());
        }
    }

    private List<StudentDto> mapStudentToDto(UUID lessonId) {
        List<Student> students = reviewRepository.getStudentReviewLesson(lessonId);
        List<StudentDto> res = new ArrayList<>();
        for (Student student : students) {
            res.add(studentRepository.findByStudentId(student.getId()));
        }
        return res;
    }

    @Override
    public ResponseDto<LessonReviewDto> getLessonReviews(UUID lessonId) {
        try {
            Optional<Lesson> lOp = lessonRepository.findById(lessonId);
            if (lOp.isPresent()) {
                Lesson lesson = lOp.get();
                LessonReviewDto res = new LessonReviewDto();
                res.setLesson(lessonService.findById(lessonId).getData());
                res.setStudents(mapStudentToDto(lessonId));
                res.setReviewCount(mapStudentToDto(lessonId).size());
                return new ResponseDto<>(true, "Ok", res);
            }
            throw new Exception("Not found lesson with id " + lessonId);
        } catch (Exception e) {
            log.error(e);
            return new ResponseDto<>(false, e.getMessage());
        }
    }
}
