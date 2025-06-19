package uz.zafar.onlinecourse.service;

import uz.zafar.onlinecourse.dto.ResponseDto;
import uz.zafar.onlinecourse.dto.review_dto.req.AddReviewDto;
import uz.zafar.onlinecourse.dto.review_dto.res.LessonReviewDto;
import uz.zafar.onlinecourse.dto.review_dto.res.ReviewDto;

import java.util.UUID;

public interface ReviewService {
    ResponseDto<ReviewDto> addReview(AddReviewDto dto);
    ResponseDto<ReviewDto> getReview(UUID reviewId);
    ResponseDto<LessonReviewDto> getLessonReviews(UUID lessonId);
}
