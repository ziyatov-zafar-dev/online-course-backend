package uz.zafar.onlinecourse.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import uz.zafar.onlinecourse.db.domain.Like;
import uz.zafar.onlinecourse.db.repository.LikeRepository;
import uz.zafar.onlinecourse.dto.ResponseDto;
import uz.zafar.onlinecourse.dto.like_dto.req.AddLikeDto;
import uz.zafar.onlinecourse.dto.like_dto.res.IsLikeDto;
import uz.zafar.onlinecourse.dto.like_dto.res.LikeCountDto;
import uz.zafar.onlinecourse.helper.TimeUtil;
import uz.zafar.onlinecourse.service.LikeService;

import java.util.Optional;
import java.util.UUID;

@Service
@Log4j2
@RequiredArgsConstructor
public class LikeServiceImpl implements LikeService {
    private final LikeRepository likeRepository;

    @Override
    public ResponseDto<LikeCountDto> getLikeCountByLessonId(UUID lessonId) {
        try {
            return new ResponseDto<>(true, "Success", new LikeCountDto(likeRepository.getLikeCountByLesson(lessonId)));
        } catch (Exception e) {
            log.error(e);
            return new ResponseDto<>(false, e.getMessage());
        }
    }

    @Override
    public ResponseDto<Void> addLike(AddLikeDto dto) {
        try {
            UUID lessonId = dto.getLessonId();
            Long studentId = dto.getStudentId();
            boolean liked = likeRepository.findByLessonIdAndStudentId(lessonId, studentId);
            if (liked) {
                throw new Exception("You have already clicked.");
            }
            Like like = new Like();
            like.setLessonId(lessonId);
            like.setStudentId(studentId);
            like.setLikedDate(TimeUtil.currentTashkentTime());
            likeRepository.save(like);
            return new ResponseDto<>(true, "Success");
        } catch (Exception e) {
            log.error(e);
            return new ResponseDto<>(false, e.getMessage());
        }
    }

    @Override
    public ResponseDto<Void> removeLike(UUID lessonId, Long studentId) {
        try {
            Optional<Like> lOp = likeRepository.findByStudentIdAndLessonId(studentId, lessonId);
            if (lOp.isEmpty()) {
                throw new Exception("Like is not clicked");
            }
            Like like = lOp.get();
            likeRepository.deleteById(like.getId());
            return new ResponseDto<>(true, "Success");
        } catch (Exception e) {
            log.error(e);
            return new ResponseDto<>(false, e.getMessage());
        }
    }

    @Override
    public ResponseDto<IsLikeDto> isLikedByUser(UUID lessonId, Long studentId) {
        try {
            return new ResponseDto<>(true, "Success",
                    new IsLikeDto(
                            likeRepository.findByLessonIdAndStudentId(lessonId, studentId)
                    )
            );

        } catch (Exception e) {
            log.error(e);
            return new ResponseDto<>(false, e.getMessage());
        }
    }
}
