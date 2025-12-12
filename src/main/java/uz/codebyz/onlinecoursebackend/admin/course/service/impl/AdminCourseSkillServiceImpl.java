package uz.codebyz.onlinecoursebackend.admin.course.service.impl;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import uz.codebyz.onlinecoursebackend.admin.course.dto.AdminCourseSkillResponseDto;
import uz.codebyz.onlinecoursebackend.admin.course.dto.CreateAdminCourseSkillDto;
import uz.codebyz.onlinecoursebackend.admin.course.dto.UpdateAdminCourseSkillDto;
import uz.codebyz.onlinecoursebackend.admin.course.dto.UploadFileResponseDto;
import uz.codebyz.onlinecoursebackend.admin.course.mapper.AdminCourseSkillMapper;
import uz.codebyz.onlinecoursebackend.admin.course.service.AdminCourseSkillService;
import uz.codebyz.onlinecoursebackend.common.ResponseDto;
import uz.codebyz.onlinecoursebackend.course.entity.Course;
import uz.codebyz.onlinecoursebackend.course.repository.CourseRepository;
import uz.codebyz.onlinecoursebackend.helper.CurrentTime;
import uz.codebyz.onlinecoursebackend.helper.FileHelper;
import uz.codebyz.onlinecoursebackend.skill.entity.Skill;
import uz.codebyz.onlinecoursebackend.skill.repository.SkillRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class AdminCourseSkillServiceImpl implements AdminCourseSkillService {
    @Value("${course.skill.icon}")
    private String skillIconBaseUrl;
    private final SkillRepository skillRepository;
    private final CourseRepository courseRepository;

    public AdminCourseSkillServiceImpl(SkillRepository skillRepository, CourseRepository courseRepository) {
        this.skillRepository = skillRepository;
        this.courseRepository = courseRepository;
    }

    @Override
    public ResponseDto<AdminCourseSkillResponseDto> findById(UUID skillId) {
        Optional<Skill> sOp = skillRepository.findById(skillId);
        if (sOp.isEmpty()) return new ResponseDto<>(false, "Skill not found");
        Skill skill = sOp.get();
        AdminCourseSkillResponseDto dto = AdminCourseSkillMapper.toDto(skill);
        return new ResponseDto<>(true, "Success", dto);
    }

    @Override
    public ResponseDto<List<AdminCourseSkillResponseDto>> findAllByCourseId(UUID courseId) {
        if (courseRepository.findById(courseId).isEmpty()) return new ResponseDto<>(false, "Course not found");
        return new ResponseDto<>(true, "Success", AdminCourseSkillMapper.toDto(skillRepository.getAllSkillsByCourse(courseId)));
    }

    @Override
    public ResponseDto<AdminCourseSkillResponseDto> addSkillToCourse(CreateAdminCourseSkillDto dto) {
        Optional<Course> cOp = courseRepository.findAdminByCourseId(dto.getCourseId());
        if (cOp.isEmpty()) return new ResponseDto<>(false, "Course not found");
        if (dto.getSkillIcon() == null)
            return new ResponseDto<>(false, "Skill ikonkasi topilmadi");

        MultipartFile image = dto.getSkillIcon();

        String contentType = image.getContentType();

        if (contentType == null || !contentType.startsWith("image/")) {
            throw new RuntimeException("Faqat rasm yuklash mumkin! (jpeg, png, webp...)");
        }
        ResponseDto<UploadFileResponseDto> savedFile = FileHelper.uploadFile(dto.getSkillIcon(), skillIconBaseUrl, false);
        if (!savedFile.isSuccess()) return new ResponseDto<>(false, savedFile.getMessage());
        Skill skill = new Skill();
        skill.setCourse(cOp.get());
        skill.setName(dto.getName());
        skill.setIconUrl(savedFile.getData().getFileUrl());
        skill.setActive(true);
        skill.setCreated(CurrentTime.currentTime());
        skill.setUpdated(CurrentTime.currentTime());
        skill.setOrderNumber(dto.getOrderNumber());
        skill = skillRepository.save(skill);
        return new ResponseDto<>(true, "Success", AdminCourseSkillMapper.toDto(skill));
    }

    @Override
    public ResponseDto<AdminCourseSkillResponseDto> editCourseSkill(UUID skillId,
                                                                    UpdateAdminCourseSkillDto dto) {
        Optional<Skill> sOp = skillRepository.findById(skillId);
        if (sOp.isEmpty()) return new ResponseDto<>(false, "Skill not found");

        if (dto.getSkillIcon() == null)
            return new ResponseDto<>(false, "Skill ikonkasi topilmadi");

        MultipartFile image = dto.getSkillIcon();

        String contentType = image.getContentType();

        if (contentType == null || !contentType.startsWith("image/")) {
            throw new RuntimeException("Faqat rasm yuklash mumkin! (jpeg, png, webp...)");
        }

        ResponseDto<UploadFileResponseDto> savedFile =
                FileHelper.uploadFile(dto.getSkillIcon(),
                        skillIconBaseUrl, false);
        if (!savedFile.isSuccess()) return new ResponseDto<>(false, savedFile.getMessage());
        Skill skill = sOp.get();
        skill.setName(dto.getName());
        FileHelper.deleteFile(skill.getIconUrl());
        skill.setIconUrl(savedFile.getData().getFileUrl());
        skill.setUpdated(CurrentTime.currentTime());
        skill.setOrderNumber(dto.getOrderNumber());
        skill = skillRepository.save(skill);
        return new ResponseDto<>(true, "Success", AdminCourseSkillMapper.toDto(skill));
    }

    @Override
    public ResponseDto<Void> deleteCourseSkill(UUID skillId) {
        Optional<Skill> sOp = skillRepository.findById(skillId);
        if (sOp.isEmpty()) return new ResponseDto<>(false, "Skill not found");
        Skill skill = sOp.get();
        skill.setActive(false);
        FileHelper.deleteFile(skill.getIconUrl());
        skill.setUpdated(CurrentTime.currentTime());
        skill = skillRepository.save(skill);
        return new ResponseDto<>(true, "Muvaffaqiyatli o'chirildi");
    }
}
