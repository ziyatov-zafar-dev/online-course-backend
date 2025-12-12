package uz.codebyz.onlinecoursebackend.teacher.rest;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import uz.codebyz.onlinecoursebackend.admin.course.dto.AdminCourseResponseDto;
import uz.codebyz.onlinecoursebackend.admin.course.dto.AdminCourseUpdateRequestDto;
import uz.codebyz.onlinecoursebackend.common.ResponseDto;
import uz.codebyz.onlinecoursebackend.course.entity.CourseStatus;
import uz.codebyz.onlinecoursebackend.security.UserPrincipal;
import uz.codebyz.onlinecoursebackend.teacher.dto.course.*;
import uz.codebyz.onlinecoursebackend.teacher.dto.promoCodeDtos.TeacherCourseAndPromoCodeResponseDto;
import uz.codebyz.onlinecoursebackend.teacher.dto.promoCodeDtos.TeacherCreatePromoCodeRequestDto;
import uz.codebyz.onlinecoursebackend.teacher.entity.Teacher;
import uz.codebyz.onlinecoursebackend.teacher.service.TeacherCourseService;
import uz.codebyz.onlinecoursebackend.user.User;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/teacher/course")
@Tag(
        name = "Teacher Course API",
        description = "Teacherlar uchun kurslar bilan ishlash API'lari. " +
                "Kurs yaratish, tahrirlash, ro‘yxatini ko‘rish va boshqa teacher funksiyalari shu yerdan boshqariladi."
)
public class TeacherCourseRestController {
    private final TeacherCourseService courseService;

    public TeacherCourseRestController(TeacherCourseService courseService) {
        this.courseService = courseService;
    }

    @GetMapping("/my-courses")
    @Operation(
            summary = "Teacherning kurslarini olish",
            description = "Tizimga kirgan teacher o‘ziga tegishli kurslar ro‘yxatini oladi."
    )
    public ResponseDto<List<TeacherCourseResponseDto>> myCourses(
            @AuthenticationPrincipal UserPrincipal principal
    ) {
        return courseService.myCourses(principal.getUser().getTeacher());
    }


    @GetMapping("/my-courses/pagination")
    @Operation(
            summary = "Teacherning kurslarini olish",
            description = "Tizimga kirgan teacher o‘ziga tegishli kurslar ro‘yxatini oladi. pagination bilan beriladi"
    )
    public ResponseDto<Page<TeacherCourseResponseDto>> myCourses(
            @AuthenticationPrincipal UserPrincipal principal,
            @RequestParam(value = "page") int page,
            @RequestParam(value = "size") int size
    ) {
        return courseService.myCourses(principal.getUser().getTeacher(), page, size);
    }

    @GetMapping("/by-id/{courseId}")
    @Operation(
            summary = "Course ID bo‘yicha teacherning kursini olish",
            description = "Tizimga kirgan teacher faqat o‘ziga tegishli kursni ID bo‘yicha oladi."
    )
    public ResponseEntity<ResponseDto<TeacherCourseResponseDto>> getByCourseId(
            @AuthenticationPrincipal UserPrincipal principal,
            @PathVariable("courseId") UUID courseId
    ) {
        return ResponseEntity.ok(
                courseService.findBySkillId(principal.getUser().getTeacher(), courseId)
        );
    }


    @GetMapping("/by-slug/{slug}")
    @Operation(
            summary = "Slug bo‘yicha kursni olish",
            description = "Kurs slug bo‘yicha olinadi."
    )
    public ResponseEntity<ResponseDto<TeacherCourseResponseDto>> getBySlug(
            @PathVariable("slug") String slug
    ) {
        return ResponseEntity.ok(
                courseService.findBySlug(slug)
        );
    }

    @GetMapping("/{categoryId}/all/pagination")
    @Operation(
            summary = "Category ID bo‘yicha kurslarni olish (pagination bilan)",
            description = "Teacher o‘ziga tegishli kurslarni category bo‘yicha pagination bilan oladi."
    )
    public ResponseEntity<ResponseDto<Page<TeacherCourseResponseDto>>> findAllPagedByCategoryId(
            @AuthenticationPrincipal UserPrincipal principal,
            @PathVariable("categoryId") UUID categoryId,
            @RequestParam("page") int page,
            @RequestParam("size") int size
    ) {
        return ResponseEntity.ok(
                courseService.findAllByCategoryId(
                        principal.getUser().getTeacher(), categoryId, page, size
                )
        );
    }

    @GetMapping("/{categoryId}/all/list")
    @Operation(
            summary = "Category ID bo‘yicha barcha kurslarni olish",
            description = "Teacher o‘ziga tegishli barcha kurslarni category bo‘yicha pagination-siz oladi."
    )
    public ResponseEntity<ResponseDto<List<TeacherCourseResponseDto>>> findAllByCategoryId(
            @AuthenticationPrincipal UserPrincipal principal,
            @PathVariable("categoryId") UUID categoryId
    ) {
        return ResponseEntity.ok(
                courseService.findAllByCategoryId(
                        principal.getUser().getTeacher(), categoryId
                )
        );
    }

    @PutMapping("/update-course/{courseId}")
    @Operation(
            summary = "Kursni yangilash (Teacher)",
            description = "Admin tomonidan kurs ma'lumotlari yangilanadi. Kurs ID orqali topiladi va berilgan ma'lumotlar asosida update qilinadi."
    )
    public ResponseEntity<ResponseDto<AdminCourseResponseDto>> updateCourse(
            @PathVariable("courseId") UUID courseId,
            @Valid @RequestBody AdminCourseUpdateRequestDto request,
            @AuthenticationPrincipal UserPrincipal principal
    ) {
        return ResponseEntity.ok(
                courseService.updateCourse(principal.getUser().getTeacher(), courseId, request)
        );
    }

    @GetMapping("/by-status/pagination")
    @Operation(
            summary = "Kurslarni status bo‘yicha olish",
            description = "Teacher o‘ziga tegishli kurslarni status bo‘yicha pagination bilan oladi. " +
                    "Status DRAFT, OPEN yoki CLOSE bo‘lishi mumkin."
    )
    public ResponseEntity<ResponseDto<Page<TeacherCourseResponseDto>>> getByStatus(
            @AuthenticationPrincipal UserPrincipal principal,
            @RequestParam("status") CourseStatus status,
            @RequestParam("page") int page,
            @RequestParam("size") int size
    ) {
        Teacher teacher = principal.getUser().getTeacher();
        return ResponseEntity.ok(
                courseService.getByStatus(teacher, status, page, size)
        );
    }

    @GetMapping("/by-status/list")
    @Operation(
            summary = "Kurslarni status bo‘yicha olish (pagination-siz)",
            description = "Teacher o‘ziga tegishli barcha kurslarni status bo‘yicha to‘liq ro‘yxat qilib oladi. " +
                    "Status DRAFT, OPEN yoki CLOSE bo‘lishi mumkin."
    )
    public ResponseEntity<ResponseDto<List<TeacherCourseResponseDto>>> getAllByStatus(
            @AuthenticationPrincipal UserPrincipal principal,
            @RequestParam("status") CourseStatus status
    ) {
        Teacher teacher = principal.getUser().getTeacher();

        return ResponseEntity.ok(
                courseService.getByStatus(teacher, status)
        );
    }

    @PatchMapping("/change-status/{courseId}")
    @Operation(
            summary = "Kurs statusini o‘zgartirish",
            description = "Berilgan kursning statusi (OPEN, DRAFT, CLOSE) yangilanadi."
    )
    public ResponseEntity<ResponseDto<TeacherCourseResponseDto>> changeCourseStatus(
            @PathVariable("courseId") UUID courseId,
            @RequestParam("status") CourseStatus status
    ) {
        return ResponseEntity.ok(
                courseService.changeCourseStatus(courseId, status)
        );
    }


    @PatchMapping("/soft-delete/{courseId}")
    @Operation(
            summary = "Kursni soft delete qilish",
            description = "Kurs o‘chirilgan deb belgilandi, lekin bazadan o‘chirib yuborilmaydi."
    )
    public ResponseEntity<ResponseDto<Void>> softDelete(
            @PathVariable("courseId") UUID courseId
    ) {
        return ResponseEntity.ok(
                courseService.softDelete(courseId)
        );
    }

    @DeleteMapping("/hard-delete/{courseId}")
    @Operation(
            summary = "Kursni hard delete qilish",
            description = "Kurs bazadan butunlay o‘chirib yuboriladi. Bu amal qaytarilmaydi."
    )
    public ResponseEntity<ResponseDto<Void>> hardDelete(
            @PathVariable("courseId") UUID courseId
    ) {
        return ResponseEntity.ok(
                courseService.hardDelete(courseId)
        );
    }

    @PostMapping("/create")
    @Operation(
            summary = "Yangi kurs yaratish (Teacher)",
            description = "Teacher tomonidan yangi kurs yaratiladi. Request body orqali kurs haqida barcha kerakli ma'lumotlar yuboriladi."
    )
    public ResponseEntity<ResponseDto<TeacherCourseResponseDto>> createCourse(
            @AuthenticationPrincipal UserPrincipal principal,
            @Valid @RequestBody TeacherCourseCreateRequestDto request
    ) {
        Teacher teacher = principal.getUser().getTeacher();
        request.setTeacherId(teacher.getId());
        return ResponseEntity.ok(
                courseService.createCourse(request)
        );
    }

    @PostMapping(
            value = "/{courseId}/add-video",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE
    )
    @Operation(
            summary = "Kursga video qo‘shish",
            description = "Teacher kursiga video fayl yuklaydi. Multipart/form-data orqali video yuboriladi."
    )
    public ResponseEntity<ResponseDto<TeacherCourseResponseDto>> addVideo(
            @PathVariable("courseId") UUID courseId,
            @RequestParam("video") MultipartFile video
    ) {
        return ResponseEntity.ok(
                courseService.addVideo(courseId, video)
        );
    }

    @PostMapping(
            value = "/{courseId}/add-image",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE
    )
    @Operation(
            summary = "Kursga rasm qo‘shish",
            description = "Teacher kursiga rasm faylini yuklaydi. Multipart/form-data orqali yuboriladi."
    )
    public ResponseEntity<ResponseDto<TeacherCourseResponseDto>> addImage(
            @PathVariable("courseId") UUID courseId,
            @RequestParam("image") MultipartFile image
    ) {
        return ResponseEntity.ok(
                courseService.addImage(courseId, image)
        );
    }

    @PatchMapping("/pricing/{courseId}")
    @Operation(
            summary = "Kurs narxini yangilash",
            description = "Teacher kursning narxini (price, discountPrice, discountPercent va boshqalar) yangilaydi."
    )
    public ResponseEntity<ResponseDto<TeacherCourseResponseDto>> updateCoursePricing(
            @PathVariable("courseId") UUID courseId,
            @Valid @RequestBody TeacherCoursePricingRequestDto request
    ) {
        return ResponseEntity.ok(
                courseService.updateCoursePricing(courseId, request)
        );
    }


    @PatchMapping("/restore/{courseId}")
    @Operation(
            summary = "O‘chirilgan kursni tiklash",
            description = "Soft delete qilingan kursni adminga yoki teacherdga qayta tiklash imkonini beradi."
    )
    public ResponseEntity<ResponseDto<TeacherCourseResponseDto>> restoreCourse(
            @PathVariable("courseId") UUID courseId
    ) {
        return ResponseEntity.ok(
                courseService.restoreCourse(courseId)
        );
    }

    @GetMapping("/by-promo")
    @Operation(
            summary = "Promo code orqali kursni topish",
            description = "Berilgan promo code orqali teacherning kursiga tegishli promo ma'lumotlarini qaytaradi."
    )
    public ResponseEntity<ResponseDto<TeacherCourseAndPromoCodeResponseDto>> findByPromoCode(
            @RequestParam("code") String code
    ) {
        return ResponseEntity.ok(
                courseService.findByPromoCode(code)
        );
    }

    @GetMapping("/promo/exists")
    @Operation(
            summary = "Promo code mavjudligini tekshirish",
            description = "Promo code mavjud yoki mavjud emasligini boolean qiymat sifatida qaytaradi."
    )
    public ResponseEntity<Boolean> existsByPromoCode(
            @RequestParam("code") String code
    ) {
        return ResponseEntity.ok(
                courseService.existsByPromoCode(code)
        );
    }

    @GetMapping("/promo/all")
    @Operation(
            summary = "Barcha promo kodlarni olish",
            description = "Teacherga tegishli barcha promo kodlar va ularga biriktirilgan kurslar ro‘yxatini qaytaradi."
    )
    public ResponseEntity<ResponseDto<List<TeacherCourseAndPromoCodeResponseDto>>> getAllPromoCodes(
            @AuthenticationPrincipal UserPrincipal user
    ) {
        return ResponseEntity.ok(
                courseService.getAllPromoCodes(user.getUser())
        );
    }

    @GetMapping("/promo")
    @Operation(
            summary = "Promo kodlar ro‘yxatini olish (pagination)",
            description = "Tizimga kirgan teacherga tegishli barcha promo kodlar va ularga bog‘langan kurslar ro‘yxatini pagination bilan qaytaradi."
    )
    public ResponseEntity<ResponseDto<Page<TeacherCourseAndPromoCodeResponseDto>>> getAllPromoCodes(
            @AuthenticationPrincipal UserPrincipal principal,
            @RequestParam int page,
            @RequestParam int size
    ) {
        User user = principal.getUser(); // teacher user
        return ResponseEntity.ok(
                courseService.getAllPromoCodes(user, page, size)
        );
    }

    @GetMapping("/promo/by-course/{courseId}")
    @Operation(
            summary = "Kursga tegishli barcha promo kodlarni olish",
            description = "Teacherning ma'lum bir kursiga tegishli barcha promo kodlarni qaytaradi."
    )
    public ResponseEntity<ResponseDto<List<TeacherCourseAndPromoCodeResponseDto>>> getAllPromoCodesByCourseId(
            @AuthenticationPrincipal UserPrincipal principal,
            @PathVariable("courseId") UUID courseId
    ) {
        Teacher teacher = principal.getUser().getTeacher();

        return ResponseEntity.ok(
                courseService.getAllPromoCodesByCourseId(teacher, courseId)
        );
    }

    @GetMapping("/promo/by-course/{courseId}/pagination")
    @Operation(
            summary = "Kursga tegishli promo kodlarni pagination bilan olish",
            description = "Teacherning ma'lum bir kursiga tegishli promo kodlarni sahifalangan tarzda qaytaradi."
    )
    public ResponseEntity<ResponseDto<Page<TeacherCourseAndPromoCodeResponseDto>>> getAllPromoCodesByCourseIdPaged(
            @AuthenticationPrincipal UserPrincipal principal,
            @PathVariable("courseId") UUID courseId,
            @RequestParam("page") int page,
            @RequestParam("size") int size
    ) {
        Teacher teacher = principal.getUser().getTeacher();

        return ResponseEntity.ok(
                courseService.getAllPromoCodesByCourseId(teacher, courseId, page, size)
        );
    }

    @GetMapping("/promo/by-teacher")
    @Operation(
            summary = "Teacherga tegishli barcha promo kodlarni olish",
            description = "Tizimga kirgan teacherning barcha kurslariga biriktirilgan promo kodlar ro‘yxatini qaytaradi."
    )
    public ResponseEntity<ResponseDto<List<TeacherCourseAndPromoCodeResponseDto>>> getAllPromoCodesByTeacher(
            @AuthenticationPrincipal UserPrincipal principal
    ) {
        Teacher teacher = principal.getUser().getTeacher();

        return ResponseEntity.ok(
                courseService.getAllPromoCodesByTeacherId(teacher)
        );
    }

    @GetMapping("/promo/by-teacher/{teacherId}")
    @Operation(
            summary = "Teacherga tegishli promo kodlarni pagination bilan olish",
            description = "Berilgan teacher ID bo‘yicha barcha promo kodlar ro‘yxati sahifalangan holatda qaytariladi."
    )
    public ResponseEntity<ResponseDto<Page<TeacherCourseAndPromoCodeResponseDto>>> getAllPromoCodesByTeacherIdPaged(
            @PathVariable("teacherId") Long teacherId,
            @RequestParam("page") int page,
            @RequestParam("size") int size
    ) {
        return ResponseEntity.ok(
                courseService.getAllPromoCodesByTeacherId(teacherId, page, size)
        );
    }

    @PostMapping("/promo/add/{courseId}")
    @Operation(
            summary = "Kursga promo code qo‘shish",
            description = "Teacher kursiga yangi promo code yaratadi va uni belgilangan kursga biriktiradi."
    )
    public ResponseEntity<ResponseDto<TeacherCourseAndPromoCodeResponseDto>> addPromoCodeToCourse(
            @PathVariable("courseId") UUID courseId,
            @Valid @RequestBody TeacherCreatePromoCodeRequestDto req,
            @AuthenticationPrincipal UserPrincipal principal) {
        req.setUser(principal.getUser());
        req.setCourseId(courseId);
        return ResponseEntity.ok(
                courseService.addPromoCodeToCourse(courseId, req)
        );
    }

    @GetMapping("/promo/{promoCodeId}")
    @Operation(
            summary = "Promo code ID orqali ma'lumot olish",
            description = "Promo code ID berilganda, promo code va unga tegishli kurs haqidagi ma'lumotlarni qaytaradi."
    )
    public ResponseEntity<ResponseDto<TeacherCourseAndPromoCodeResponseDto>> findByPromoCodeId(
            @PathVariable("promoCodeId") UUID promoCodeId
    ) {
        return ResponseEntity.ok(
                courseService.findByPromoCodeId(promoCodeId)
        );
    }

    @Operation(
            summary = "Skill ni ID orqali olish",
            description = "UUID orqali teacher course skill ma'lumotini qaytaradi"
    )
    @GetMapping("skill/{skillId}")
    public ResponseDto<TeacherCourseSkillResponseDto> findById(
            @Parameter(
                    description = "Skill UUID",
                    example = "c7a8f2d4-4d1b-4b4f-9c3e-2f6e1b7a9d01",
                    required = true
            )
            @PathVariable("skillId") UUID skillId
    ) {
        return courseService.findBySkillId(skillId);
    }

    @Operation(
            summary = "Course ID orqali skill'larni olish",
            description = "Berilgan course UUID ga tegishli barcha course skill'lar ro'yxatini qaytaradi"
    )
    @GetMapping("/skills/{courseId}")
    public ResponseDto<List<TeacherCourseSkillResponseDto>> findAllByCourseId(
            @Parameter(
                    description = "Course UUID",
                    example = "b3a1e1c2-7f9a-4e4f-b5b2-9d9c3f9a1234",
                    required = true
            )
            @PathVariable UUID courseId
    ) {
        return courseService.findAllByCourseId(courseId);
    }

    @Operation(
            summary = "Course ga yangi skill qo‘shish",
            description = "Teacher course uchun yangi skill yaratadi va course ga biriktiradi"
    )
    @PostMapping("skill/add")
    public ResponseDto<TeacherCourseSkillResponseDto> addSkillToCourse(
            @Parameter(
                    description = "Yangi skill yaratish uchun DTO",
                    required = true
            )
            @RequestBody CreateTeacherCourseSkillDto skill
    ) {
        return courseService.addSkillToCourse(skill);
    }

    @Operation(
            summary = "Course skill'ni tahrirlash",
            description = "Berilgan skill UUID bo‘yicha teacher course skill ma’lumotlarini yangilaydi"
    )
    @PutMapping("/{skillId}")
    public ResponseDto<TeacherCourseSkillResponseDto> editCourseSkill(
            @Parameter(
                    description = "Tahrirlanayotgan skill UUID",
                    example = "c7a8f2d4-4d1b-4b4f-9c3e-2f6e1b7a9d01",
                    required = true
            )
            @PathVariable("skillId") UUID skillId,

            @Parameter(
                    description = "Yangilangan skill ma’lumotlari",
                    required = true
            )
            @RequestBody UpdateTeacherCourseSkillDto skill
    ) {
        return courseService.editCourseSkill(skillId, skill);
    }

    @Operation(
            summary = "Course skill'ni o‘chirish",
            description = "Berilgan skill UUID bo‘yicha teacher course skill'ni o‘chiradi"
    )
    @DeleteMapping("/{skillId}")
    public ResponseDto<Void> deleteCourseSkill(
            @Parameter(
                    description = "O‘chirilayotgan skill UUID",
                    example = "c7a8f2d4-4d1b-4b4f-9c3e-2f6e1b7a9d01",
                    required = true
            )
            @PathVariable("skillId") UUID skillId
    ) {
        return courseService.deleteCourseSkill(skillId);
    }

}
