package uz.codebyz.onlinecoursebackend.admin.rest;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import uz.codebyz.onlinecoursebackend.admin.course.dto.AdminCourseSkillResponseDto;
import uz.codebyz.onlinecoursebackend.admin.course.dto.CreateAdminCourseSkillDto;
import uz.codebyz.onlinecoursebackend.admin.course.dto.UpdateAdminCourseSkillDto;
import uz.codebyz.onlinecoursebackend.admin.course.service.AdminCourseSkillService;
import uz.codebyz.onlinecoursebackend.common.ResponseDto;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/admin/skills")
@Tag(
        name = "Admin: Course Skills",
        description = "Admin panel orqali kursga tegishli skilllarni boshqarish"
)
public class AdminCourseSkillsRestController {

    private final AdminCourseSkillService skillService;

    public AdminCourseSkillsRestController(AdminCourseSkillService skillService) {
        this.skillService = skillService;
    }

    @Operation(summary = "Skill ID bo‘yicha olish")
    @GetMapping("skill/{skillId}")
    public ResponseEntity<ResponseDto<AdminCourseSkillResponseDto>> getSkillById(
            @PathVariable("skillId") UUID skillId
    ) {
        return ResponseEntity.ok(skillService.findById(skillId));
    }


    @Operation(summary = "Kursga tegishli barcha skilllarni olish")
    @GetMapping("/skills-by-course/{courseId}")
    public ResponseEntity<ResponseDto<List<AdminCourseSkillResponseDto>>> getSkillsByCourseId(
            @PathVariable("courseId") UUID courseId
    ) {
        return ResponseEntity.ok(skillService.findAllByCourseId(courseId));
    }

//    @Operation(summary = "Kursga yangi skill qo‘shish")
//    @PostMapping(
//            value = "/add-skill-to-course",
//            consumes = MediaType.MULTIPART_FORM_DATA_VALUE
//    )
//    public ResponseEntity<ResponseDto<AdminCourseSkillResponseDto>> addSkill(
//            @RequestBody @Valid CreateAdminCourseSkillDto createAdminCourseSkillDto,
//            @RequestParam("skill_icon") MultipartFile file
//            ) {
//        createAdminCourseSkillDto.setSkillIcon(file);
//        return ResponseEntity.ok(skillService.addSkillToCourse(createAdminCourseSkillDto));
//    }

    @Operation(summary = "Kursga yangi skill qo‘shish")
    @PostMapping(
            value = "/add-skill-to-course",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE
    )
    public ResponseEntity<ResponseDto<AdminCourseSkillResponseDto>> addSkill(
            @RequestParam("courseId") UUID courseId,
            @RequestParam("name") String name,
            @RequestParam("icon") MultipartFile icon,
            @RequestParam("orderNumber") Integer orderNumber
    ) {
        return ResponseEntity.ok(skillService.addSkillToCourse(
                new CreateAdminCourseSkillDto(
                        courseId, name, icon, orderNumber
                )
        ));
    }


    @Operation(summary = "Skillni tahrirlash")
        @PutMapping(
                value = "/edit/{skillId}",
                consumes = MediaType.MULTIPART_FORM_DATA_VALUE
        )
        public ResponseEntity<ResponseDto<AdminCourseSkillResponseDto>> editSkill(
                @PathVariable("skillId") UUID skillId,
                @RequestParam("skill_name") String skillName,
                @RequestParam("icon") MultipartFile skillIcon,
                @RequestParam("order_number") Integer orderNumber

        ) {
            UpdateAdminCourseSkillDto dto = new UpdateAdminCourseSkillDto();
            dto.setName(skillName);
            dto.setSkillIcon(skillIcon);
            dto.setOrderNumber(orderNumber);
            return ResponseEntity.ok(skillService.editCourseSkill(skillId, dto));
        }

//    @Operation(summary = "Skillni tahrirlash")
//    @PutMapping(
//            value = "/edit/{skillId}",
//            consumes = MediaType.MULTIPART_FORM_DATA_VALUE
//    )
//    public ResponseEntity<ResponseDto<AdminCourseSkillResponseDto>> editSkill(
//            @PathVariable("skillId") UUID skillId,
//            @RequestBody UpdateAdminCourseSkillDto dto
//    ) {
//        return ResponseEntity.ok(skillService.editCourseSkill(skillId, dto));
//    }

    @Operation(summary = "Skillni o‘chirish")
    @DeleteMapping("/delete/{skillId}")
    public ResponseEntity<ResponseDto<Void>> deleteSkill(
            @PathVariable("skillId") UUID skillId
    ) {
        return ResponseEntity.ok(skillService.deleteCourseSkill(skillId));
    }
}
