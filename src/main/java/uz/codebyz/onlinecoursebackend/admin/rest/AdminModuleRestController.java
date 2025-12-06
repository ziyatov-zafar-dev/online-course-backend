package uz.codebyz.onlinecoursebackend.admin.rest;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uz.codebyz.onlinecoursebackend.admin.module.dto.AdminModuleCreateRequestDto;
import uz.codebyz.onlinecoursebackend.admin.module.dto.AdminModuleResponseDto;
import uz.codebyz.onlinecoursebackend.admin.module.dto.AdminModuleTypeRequestDto;
import uz.codebyz.onlinecoursebackend.admin.module.dto.AdminModuleUpdateRequestDto;
import uz.codebyz.onlinecoursebackend.admin.module.service.AdminModuleService;
import uz.codebyz.onlinecoursebackend.common.ResponseDto;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/admin/modules")
@Tag(
        name = "Admin Module Management",
        description = "Kurs modullarini boshqarish uchun admin API: qo‘shish, tahrirlash, o‘chirish va ro‘yxatni olish"
)
public class AdminModuleRestController {

    private final AdminModuleService moduleService;

    public AdminModuleRestController(AdminModuleService moduleService) {
        this.moduleService = moduleService;
    }

    @Operation(summary = "Kursga yangi modul qo‘shish")
    @PostMapping("/add-module")
    public ResponseEntity<ResponseDto<AdminModuleResponseDto>> addModule(
            @RequestBody AdminModuleCreateRequestDto dto
    ) {
        return ResponseEntity.ok(moduleService.addModuleToCourse(dto));
    }

    @Operation(summary = "Modulni ID bo‘yicha olish")
    @GetMapping("/module/{moduleId}")
    public ResponseEntity<ResponseDto<AdminModuleResponseDto>> findById(
            @PathVariable(value = "moduleId") UUID moduleId
    ) {
        return ResponseEntity.ok(moduleService.findById(moduleId));
    }

    @Operation(summary = "Kursga tegishli barcha modullar ro‘yxati")
    @GetMapping("/get-all-modules-by-course-id/{courseId}")
    public ResponseEntity<ResponseDto<List<AdminModuleResponseDto>>> getModulesByCourse(
            @PathVariable(value = "courseId") UUID courseId
    ) {
        return ResponseEntity.ok(moduleService.getModulesByCourseId(courseId));
    }

    @Operation(summary = "Kurs modullarini pagination bilan olish")
    @GetMapping("/get-all-modules-by-course-id/{courseId}/pagination")
    public ResponseEntity<ResponseDto<Page<AdminModuleResponseDto>>> getModulesByCoursePaged(
            @PathVariable UUID courseId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        return ResponseEntity.ok(moduleService.getModulesByCourseId(courseId, page, size));
    }

    @Operation(summary = "Modulni tahrirlash")
    @PutMapping("/edit-module/{moduleId}")
    public ResponseEntity<ResponseDto<AdminModuleResponseDto>> updateModule(
            @PathVariable(value = "moduleId") UUID moduleId,
            @RequestBody AdminModuleUpdateRequestDto dto
    ) {
        return ResponseEntity.ok(moduleService.updateModule(moduleId, dto));
    }

    @Operation(summary = "Modulni soft-delete qilish (faolmas deb belgilanadi)")
    @DeleteMapping("/delete/{moduleId}")
    public ResponseEntity<ResponseDto<Void>> deleteModule(
            @PathVariable(value = "moduleId") UUID moduleId
    ) {
        return ResponseEntity.ok(moduleService.softDeleteModule(moduleId));
    }

    @Operation(summary = "Kurs bo‘yicha hard-delete qilingan modullar ro‘yxatini olish")
    @GetMapping("/hard-deleted/{courseId}")
    public ResponseEntity<ResponseDto<List<AdminModuleResponseDto>>> getHardDeletedModules(
            @PathVariable("courseId") UUID courseId
    ) {
        return ResponseEntity.ok(moduleService.getModulesByTypeByCourseId(
                AdminModuleTypeRequestDto.HARD_DELETE, courseId
        ));
    }

    @Operation(summary = "Kurs bo‘yicha hard-delete qilingan modullarni pagination bilan olish")
    @GetMapping("/hard-deleted/{courseId}/pagination")
    public ResponseEntity<ResponseDto<Page<AdminModuleResponseDto>>> getHardDeletedModulesPaged(
            @PathVariable(value = "courseId") UUID courseId,
            @RequestParam(defaultValue = "0", value = "page") int page,
            @RequestParam(defaultValue = "10", value = "size") int size
    ) {
        return ResponseEntity.ok(moduleService.getModulesByTypeByCourseId(AdminModuleTypeRequestDto.HARD_DELETE, courseId, page, size));
    }


    @Operation(summary = "Kurs bo‘yicha hard-delete qilingan modullar ro‘yxatini olish")
    @GetMapping("/soft-deleted/{courseId}")
    public ResponseEntity<ResponseDto<List<AdminModuleResponseDto>>> getSoftDeletedModules(
            @PathVariable("courseId") UUID courseId
    ) {
        return ResponseEntity.ok(moduleService.getModulesByTypeByCourseId(
                AdminModuleTypeRequestDto.SOFT_DELETE, courseId
        ));
    }

    @Operation(summary = "Kurs bo‘yicha hard-delete qilingan modullarni pagination bilan olish")
    @GetMapping("/soft-deleted/{courseId}/pagination")
    public ResponseEntity<ResponseDto<Page<AdminModuleResponseDto>>> getSoftDeletedModulesPaged(
            @PathVariable(value = "courseId") UUID courseId,
            @RequestParam(defaultValue = "0", value = "page") int page,
            @RequestParam(defaultValue = "10", value = "size") int size
    ) {
        return ResponseEntity.ok(moduleService.getModulesByTypeByCourseId(AdminModuleTypeRequestDto.SOFT_DELETE, courseId, page, size));
    }

    @Operation(
            summary = "Soft-delete qilingan modulni tiklash",
            description = "Ushbu API yordamida oldin soft-delete qilingan modulni qayta aktiv holatga qaytarish mumkin."
    )
    @PostMapping("/restore/{moduleId}")
    public ResponseEntity<ResponseDto<AdminModuleResponseDto>> restoreModule(
            @PathVariable(value = "moduleId") UUID moduleId
    ) {
        return ResponseEntity.ok(moduleService.restoreFromSoftDelete(moduleId));
    }


    @Operation(
            summary = "Modulni butunlay hard-delete qilish",
            description = "Soft-delete emas, balki modulni bazadan butunlay o‘chiradi. Bu amal qaytarilmaydi!"
    )
    @DeleteMapping("/hard-delete/{moduleId}")
    public ResponseEntity<ResponseDto<Void>> hardDeleteModule(
            @PathVariable(value = "moduleId") UUID moduleId
    ) {
        return ResponseEntity.ok(moduleService.hardDelete(moduleId));
    }


}
