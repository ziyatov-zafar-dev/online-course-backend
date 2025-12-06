package uz.codebyz.onlinecoursebackend.admin.homework.service;

import uz.codebyz.onlinecoursebackend.admin.homework.dto.AdminAddHomeworkRequestDto;
import uz.codebyz.onlinecoursebackend.admin.homework.dto.AdminHomeworkResponseDto;
import uz.codebyz.onlinecoursebackend.admin.homework.dto.AdminUpdateHomeworkRequestDto;
import uz.codebyz.onlinecoursebackend.common.ResponseDto;

import java.util.UUID;

public interface AdminHomeworkService {
    ResponseDto<AdminHomeworkResponseDto> setHomework(AdminAddHomeworkRequestDto req);

    ResponseDto<AdminHomeworkResponseDto> findById(UUID homeworkId);

    ResponseDto<AdminHomeworkResponseDto> changeHomework(UUID homeworkId, AdminUpdateHomeworkRequestDto req);

    ResponseDto<AdminHomeworkResponseDto> changeMaxBall(UUID homeworkId, Integer newMaxBall);

    ResponseDto<AdminHomeworkResponseDto> changeMinBall(UUID homeworkId, Integer newMinBall);
}
