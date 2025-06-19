package uz.zafar.onlinecourse.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import uz.zafar.onlinecourse.db.domain.Comment;
import uz.zafar.onlinecourse.db.domain.Lesson;
import uz.zafar.onlinecourse.db.domain.Student;
import uz.zafar.onlinecourse.db.repository.CommentRepository;
import uz.zafar.onlinecourse.db.repository.LessonRepository;
import uz.zafar.onlinecourse.db.repository.StudentRepository;
import uz.zafar.onlinecourse.dto.ResponseDto;
import uz.zafar.onlinecourse.dto.comment_dto.req.AddCommentDto;
import uz.zafar.onlinecourse.dto.comment_dto.res.CommentCountDto;
import uz.zafar.onlinecourse.dto.comment_dto.res.CommentDto;
import uz.zafar.onlinecourse.helper.TimeUtil;
import uz.zafar.onlinecourse.service.CommentService;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@Log4j2
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {
    private final CommentRepository commentRepository;
    private final LessonRepository lessonRepository;
    private final StudentRepository studentRepository;

    @Override
    public ResponseDto<CommentDto> addComment(AddCommentDto commentDto) {
        try {
            Optional<Lesson> lOp = lessonRepository.findById(commentDto.getLessonId());
            if (lOp.isEmpty())
                throw new Exception("Not found lesson");
            Optional<Student> sOp = studentRepository.findById(commentDto.getStudentId());
            if (sOp.isEmpty())
                throw new Exception("Not found student");
            Comment comment = new Comment();
            comment.setComment(commentDto.getComment());
            comment.setCreated(TimeUtil.currentTashkentTime());
            comment.setLessonId(commentDto.getLessonId());
            comment.setStudentId(commentDto.getStudentId());
            comment.setLastEdited(TimeUtil.currentTashkentTime());
            comment.setEdited(false);
            return new ResponseDto<>(true, "Success", mapCommentDto(
                    commentRepository.save(comment), comment.getStudentId()
            ));
        } catch (Exception e) {
            log.error(e);
            return new ResponseDto<>(false, e.getMessage());
        }
    }

    private CommentDto mapCommentDto(Comment comment, Long studentId) {
        return CommentDto
                .builder()
                .me(studentId.equals(comment.getStudentId()))
                .commentId(comment.getId())
                .lessonId(comment.getLessonId())
                .studentId(comment.getStudentId())
                .comment(comment.getComment())
                .edited(comment.getEdited())
                .lastEditedAt(comment.getLastEdited())
                .createdAt(TimeUtil.currentTashkentTime())
                .build();
    }

    private CommentDto mapCommentDto(Comment comment) {
        return CommentDto
                .builder()
                .me(null)
                .commentId(comment.getId())
                .lessonId(comment.getLessonId())
                .studentId(comment.getStudentId())
                .comment(comment.getComment())
                .edited(comment.getEdited())
                .lastEditedAt(comment.getLastEdited())
                .createdAt(TimeUtil.currentTashkentTime())
                .build();
    }

    @Override
    public ResponseDto<Void> deleteComment(UUID commentId) {
        try {
            Optional<Comment> cOp = commentRepository.findById(commentId);
            if (cOp.isEmpty())
                throw new Exception("Not found comment");
            Comment comment = cOp.get();
            commentRepository.delete(comment);
            return new ResponseDto<>(true, "Success");
        } catch (Exception e) {
            log.error(e);
            return new ResponseDto<>(false, e.getMessage());
        }
    }

    @Override
    public ResponseDto<List<CommentDto>> getAllCommentsByLessonId(UUID lessonId, Long studentId) {
        try {
            List<Comment> comments = commentRepository.findAllByLessonId(lessonId);
            List<CommentDto> result = new ArrayList<>();
            for (Comment comment : comments) {
                result.add(mapCommentDto(comment, studentId));
            }
            return new ResponseDto<>(true, "Success", result);
        } catch (Exception e) {
            log.error(e);
            return new ResponseDto<>(false, e.getMessage());
        }
    }

    @Override
    public ResponseDto<CommentDto> getCommentById(UUID commentId) {
        try {
            Optional<Comment> cOp = commentRepository.findById(commentId);
            if (cOp.isEmpty())
                throw new Exception("Not found comment");
            Comment comment = cOp.get();
            return new ResponseDto<>(true, "Success", mapCommentDto(comment));
        } catch (Exception e) {
            log.error(e);
            return new ResponseDto<>(false, e.getMessage());
        }
    }

    @Override
    public ResponseDto<CommentCountDto> getCommentCountByLessonId(UUID lessonId) {
        try {
            return new ResponseDto<>(true, "Ok",
                    new CommentCountDto(commentRepository.findAllByLessonId(lessonId).size()));
        } catch (Exception e) {
            log.error(e);
            return new ResponseDto<>(false, e.getMessage());
        }
    }
}
