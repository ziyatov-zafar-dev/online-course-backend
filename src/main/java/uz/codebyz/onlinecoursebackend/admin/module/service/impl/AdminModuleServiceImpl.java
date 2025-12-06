package uz.codebyz.onlinecoursebackend.admin.module.service.impl;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import uz.codebyz.onlinecoursebackend.admin.course.mapper.AdminCourseMapper;
import uz.codebyz.onlinecoursebackend.admin.module.dto.AdminModuleCreateRequestDto;
import uz.codebyz.onlinecoursebackend.admin.module.dto.AdminModuleResponseDto;
import uz.codebyz.onlinecoursebackend.admin.module.dto.AdminModuleTypeRequestDto;
import uz.codebyz.onlinecoursebackend.admin.module.dto.AdminModuleUpdateRequestDto;
import uz.codebyz.onlinecoursebackend.admin.module.mapper.AdminModuleMapper;
import uz.codebyz.onlinecoursebackend.admin.module.service.AdminModuleService;
import uz.codebyz.onlinecoursebackend.common.ResponseDto;
import uz.codebyz.onlinecoursebackend.course.entity.Course;
import uz.codebyz.onlinecoursebackend.course.repository.CourseRepository;
import uz.codebyz.onlinecoursebackend.module.entity.Module;
import uz.codebyz.onlinecoursebackend.module.repository.ModuleRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class AdminModuleServiceImpl implements AdminModuleService {
    private final ModuleRepository moduleRepository;
    private final CourseRepository courseRepository;

    public AdminModuleServiceImpl(ModuleRepository moduleRepository, CourseRepository courseRepository) {
        this.moduleRepository = moduleRepository;
        this.courseRepository = courseRepository;
    }

    @Override
    public ResponseDto<AdminModuleResponseDto> findById(UUID moduleId) {
        Optional<Module> mOp = moduleRepository.findByModuleId(moduleId);
        return mOp.map(module -> new ResponseDto<>(true, "Success", AdminModuleMapper.toDto(module))).orElseGet(() -> new ResponseDto<>(false, "Not fount modele id : " + moduleId));
    }

    @Override
    public ResponseDto<List<AdminModuleResponseDto>> getModulesByCourseId(UUID courseId) {
        Optional<Course> cOp = courseRepository.findAdminByCourseId(courseId);
        return cOp.map(course -> new ResponseDto<>(true, "Success", AdminCourseMapper.toDto(course).getModules())).orElseGet(() -> new ResponseDto<>(false, "Not found course id : " + courseId));
    }

    @Override
    public ResponseDto<Page<AdminModuleResponseDto>> getModulesByCourseId(UUID courseId, int page, int size) {
        Page<Module> all = moduleRepository.findAllByCourseId(courseId, PageRequest.of(page, size));
        return new ResponseDto<>(true, "Success", AdminModuleMapper.toDtoPage(all));
    }

    @Override
    public ResponseDto<AdminModuleResponseDto> addModuleToCourse(AdminModuleCreateRequestDto dto) {
        Optional<Course> cOp = courseRepository.findAdminByCourseId(dto.getCourseId());
        if (cOp.isEmpty())
            return new ResponseDto<>(false, "Not found course id : " + dto.getCourseId());
        Module module = AdminModuleMapper.addModuleMapper(dto, cOp.get());
        return new ResponseDto<>(true, "Success", AdminModuleMapper.toDto(moduleRepository.save(module)));
    }

    @Override
    public ResponseDto<Void> softDeleteModule(UUID moduleId) {
        Optional<Module> mOp = moduleRepository.findByModuleId(moduleId);
        if (mOp.isEmpty())
            return new ResponseDto<>(false, "Not found module id : " + moduleId);
        Module module = mOp.get();
        module.setActive(false);
        module = moduleRepository.save(module);
        return new ResponseDto<>(true, "Muvaffaqiyatli o'chirildi ammo tiklash mumkin");
    }

    @Override
    public ResponseDto<AdminModuleResponseDto> updateModule(UUID moduleId, AdminModuleUpdateRequestDto dto) {
        Optional<Module> mOp = moduleRepository.findByModuleId(moduleId);
        if (mOp.isEmpty())
            return new ResponseDto<>(false, "Not found module id : " + moduleId);
        Module module = AdminModuleMapper.editModuleMapper(dto, mOp.get());
        module = moduleRepository.save(module);
        return new ResponseDto<>(true, "Success", AdminModuleMapper.toDto(module));
    }


    @Override
    public ResponseDto<List<AdminModuleResponseDto>> getModulesByTypeByCourseId(AdminModuleTypeRequestDto type, UUID courseId) {
        List<Module> modules;
        Optional<Course> cOp = courseRepository.findAdminByCourseId(courseId);
        if (cOp.isEmpty())
            return new ResponseDto<>(false, "Not found course id : " + courseId);
        if (type == AdminModuleTypeRequestDto.ACTIVE) {
            modules = moduleRepository.findAllByCourseId(courseId);
        } else if (type == AdminModuleTypeRequestDto.SOFT_DELETE) {
            modules = moduleRepository.findAllSoftDeletedModules(courseId);
        } else if (type == AdminModuleTypeRequestDto.HARD_DELETE) {
            modules = moduleRepository.findAllHardDeletedModules(courseId);
        } else return new ResponseDto<>(false, "Not found type : " + type);
        return new ResponseDto<>(true, "Success", AdminModuleMapper.toDto(modules));
    }

    @Override
    public ResponseDto<Page<AdminModuleResponseDto>> getModulesByTypeByCourseId(AdminModuleTypeRequestDto type, UUID courseId, int page, int size) {
        Page<Module> modules;
        Pageable pageable = PageRequest.of(page, size);
        Optional<Course> cOp = courseRepository.findAdminByCourseId(courseId);
        if (cOp.isEmpty())
            return new ResponseDto<>(false, "Not found course id : " + courseId);
        if (type == AdminModuleTypeRequestDto.ACTIVE) {
            modules = moduleRepository.findAllByCourseId(courseId, pageable);
        } else if (type == AdminModuleTypeRequestDto.SOFT_DELETE) {
            modules = moduleRepository.findAllSoftDeletedModules(courseId, pageable);
        } else if (type == AdminModuleTypeRequestDto.HARD_DELETE) {
            modules = moduleRepository.findAllHardDeletedModules(courseId, pageable);
        } else return new ResponseDto<>(false, "Not found type : " + type);
        return new ResponseDto<>(true, "Success", AdminModuleMapper.toDtoPage(modules));
    }


    @Override
    public ResponseDto<AdminModuleResponseDto> restoreFromSoftDelete(UUID moduleId) {
        Optional<Module> mOp = moduleRepository.findById(moduleId);
        if (mOp.isEmpty())
            return new ResponseDto<>(false, "Not found module id : " + moduleId);
        Module module = mOp.get();
        if (module.getDeleted()) {
            return new ResponseDto<>(false, "Bu module ni tiklash imkonsiz chunki hard delete qilingan");
        }
        if (!module.getActive() && !module.getDeleted()) {
            module.setActive(true);
            module = moduleRepository.save(module);
            return new ResponseDto<>(true, "Muvaffaqiyatli tiklandi", AdminModuleMapper.toDto(module));
        }

        if (module.getActive() && !module.getDeleted()) {
            return new ResponseDto<>(false, "Bu allaqachon aktiv qilinib bo'lingam");
        }
        return new ResponseDto<>(false, "Allaqachon aktiv qilingan");
    }

    @Override
    public ResponseDto<Void> hardDelete(UUID moduleId) {
        Optional<Module> mOp = moduleRepository.findById(moduleId);
        if (mOp.isEmpty())
            return new ResponseDto<>(false, "Not found module id : " + moduleId);
        Module module = mOp.get();
        if (module.getDeleted()) {
            return new ResponseDto<>(false, "Bu module allaqachon o'chirilgan");
        }
        if (!module.getActive() && !module.getDeleted()) {
            module.setDeleted(true);
            module = moduleRepository.save(module);
            return new ResponseDto<>(true, "Muvaffaqiyatli o'chirildi, endi buni tiklash imkonsiz");
        }
        if (module.getActive() && !module.getDeleted()) {
            return new ResponseDto<>(false, "Birdan o'chirish imkonsiz, birinchi soft delete qiling");
        }
        return new ResponseDto<>(false, "Kutilmagan xatolik");
    }
}
