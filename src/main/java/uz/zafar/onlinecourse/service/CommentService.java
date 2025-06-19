package uz.zafar.onlinecourse.service;

import uz.zafar.onlinecourse.dto.ResponseDto;
import uz.zafar.onlinecourse.dto.comment_dto.req.AddCommentDto;
import uz.zafar.onlinecourse.dto.comment_dto.res.CommentCountDto;
import uz.zafar.onlinecourse.dto.comment_dto.res.CommentDto;

import java.util.List;
import java.util.UUID;

public interface CommentService {
    ResponseDto<CommentDto> addComment(AddCommentDto commentDto);

    ResponseDto<Void> deleteComment(UUID commentId);

    ResponseDto<List<CommentDto>> getAllCommentsByLessonId(UUID lessonId, Long studentId);

    ResponseDto<CommentDto> getCommentById(UUID commentId);

    ResponseDto<CommentCountDto> getCommentCountByLessonId(UUID lessonId);

}
