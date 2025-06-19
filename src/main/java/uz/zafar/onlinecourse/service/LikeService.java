package uz.zafar.onlinecourse.service;

import uz.zafar.onlinecourse.dto.ResponseDto;
import uz.zafar.onlinecourse.dto.like_dto.req.AddLikeDto;
import uz.zafar.onlinecourse.dto.like_dto.res.IsLikeDto;
import uz.zafar.onlinecourse.dto.like_dto.res.LikeCountDto;

import java.util.UUID;

public interface LikeService {
    ResponseDto<LikeCountDto>getLikeCountByLessonId(UUID lessonId);
    ResponseDto<Void> addLike(AddLikeDto likeDto);
    ResponseDto<Void> removeLike(UUID lessonId, Long studentId);
    ResponseDto<IsLikeDto> isLikedByUser(UUID lessonId, Long studentId);
}
