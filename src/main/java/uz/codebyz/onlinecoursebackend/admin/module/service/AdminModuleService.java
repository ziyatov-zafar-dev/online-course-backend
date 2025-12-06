package uz.codebyz.onlinecoursebackend.admin.module.service;

import org.springframework.data.domain.Page;
import uz.codebyz.onlinecoursebackend.admin.module.dto.AdminModuleCreateRequestDto;
import uz.codebyz.onlinecoursebackend.admin.module.dto.AdminModuleResponseDto;
import uz.codebyz.onlinecoursebackend.admin.module.dto.AdminModuleTypeRequestDto;
import uz.codebyz.onlinecoursebackend.admin.module.dto.AdminModuleUpdateRequestDto;
import uz.codebyz.onlinecoursebackend.common.ResponseDto;

import java.util.List;
import java.util.UUID;

public interface AdminModuleService {
    ResponseDto<AdminModuleResponseDto> findById(UUID moduleId);

    ResponseDto<List<AdminModuleResponseDto>> getModulesByCourseId(UUID courseId);

    ResponseDto<Page<AdminModuleResponseDto>> getModulesByCourseId(UUID courseId, int page, int size);

    ResponseDto<AdminModuleResponseDto> addModuleToCourse(AdminModuleCreateRequestDto dto);

    ResponseDto<Void> softDeleteModule(UUID moduleId);

    ResponseDto<AdminModuleResponseDto> updateModule(UUID moduleId, AdminModuleUpdateRequestDto dto);


    ResponseDto<List<AdminModuleResponseDto>> getModulesByTypeByCourseId(AdminModuleTypeRequestDto type, UUID courseId);

    ResponseDto<Page<AdminModuleResponseDto>> getModulesByTypeByCourseId(AdminModuleTypeRequestDto type, UUID courseId, int page, int size);


    ResponseDto<AdminModuleResponseDto> restoreFromSoftDelete(UUID moduleId);

    ResponseDto<Void> hardDelete(UUID moduleId);

}
