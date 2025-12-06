package uz.codebyz.onlinecoursebackend.admin.rest;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import uz.codebyz.onlinecoursebackend.admin.category.promoCodeDtos.AdminCourseAndPromoCodeResponseDto;
import uz.codebyz.onlinecoursebackend.admin.category.promoCodeDtos.AdminCreatePromoCodeRequestDto;
import uz.codebyz.onlinecoursebackend.admin.course.dto.AdminCourseCreateRequestDto;
import uz.codebyz.onlinecoursebackend.admin.course.dto.AdminCoursePricingRequestDto;
import uz.codebyz.onlinecoursebackend.admin.course.dto.AdminCourseResponseDto;
import uz.codebyz.onlinecoursebackend.admin.course.dto.AdminCourseUpdateRequestDto;
import uz.codebyz.onlinecoursebackend.admin.course.service.AdminCourseService;
import uz.codebyz.onlinecoursebackend.common.ResponseDto;
import uz.codebyz.onlinecoursebackend.course.entity.CourseStatus;
import uz.codebyz.onlinecoursebackend.security.UserPrincipal;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/admin/courses")
public class AdminCourseRestController {
    private final AdminCourseService courseService;

    public AdminCourseRestController(AdminCourseService courseService) {
        this.courseService = courseService;
    }

    @Operation(
            summary = "Create new course",
            description = "Yangi kurs yaratish uchun ishlatiladi. Kurs nomi, tavsifi, narxi va kategoriyasi kabi asosiy maʼlumotlarni qabul qiladi."
    )
    @ApiResponse(responseCode = "200", description = "Kurs muvaffaqiyatli yaratildi")
    @ApiResponse(responseCode = "400", description = "Noto‘g‘ri maʼlumot yuborilgan")
//    @PostMapping(
//            value = "add-course",
//            consumes = MediaType.MULTIPART_FORM_DATA_VALUE
//    )
//    public ResponseEntity<ResponseDto<AdminCourseResponseDto>> createCourse(
//            @RequestBody AdminCourseCreateRequestDto request,
//            @RequestParam(value = "image", required = false) MultipartFile image,
//            @RequestParam(value = "video", required = false) MultipartFile video
//    ) {
//        request.setImage(image);
//        request.setVideo(video);
//        return ResponseEntity.ok(courseService.createCourse(request));
//    }
    @PostMapping(
            value = "add-course"
    )
    public ResponseEntity<ResponseDto<AdminCourseResponseDto>> createCourse(
            @RequestBody AdminCourseCreateRequestDto request
    ) {
        return ResponseEntity.ok(courseService.createCourse(request));
    }

    @PatchMapping(
            value = "/{courseId}/add-video",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE
    )
    public ResponseEntity<ResponseDto<AdminCourseResponseDto>> addVideo(
            @PathVariable("courseId") UUID courseId,
            @RequestPart("video") MultipartFile video
    ) {
        ResponseDto<AdminCourseResponseDto> response = courseService.addVideo(courseId, video);
        return ResponseEntity.ok(response);
    }

    @PatchMapping(
            value = "/{courseId}/add-image",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE
    )
    public ResponseEntity<ResponseDto<AdminCourseResponseDto>> addImage(
            @PathVariable("courseId") UUID courseId,
            @RequestPart("image") MultipartFile image
    ) {
        ResponseDto<AdminCourseResponseDto> response = courseService.addImage(courseId, image);
        return ResponseEntity.ok(response);
    }

    @Operation(
            summary = "Update course",
            description = "Mavjud kurs maʼlumotlarini yangilash uchun ishlatiladi. Nom, tavsif, narx, kategoriya va boshqa asosiy maydonlarni o‘zgartirish imkonini beradi."
    )
    @ApiResponse(responseCode = "200", description = "Kurs muvaffaqiyatli yangilandi")
    @ApiResponse(responseCode = "400", description = "Noto‘g‘ri maʼlumot yuborilgan")
    @PutMapping("update-course/{courseId}")
    public ResponseEntity<ResponseDto<AdminCourseResponseDto>> updateCourse(@PathVariable(name = "courseId") UUID courseId, @RequestBody AdminCourseUpdateRequestDto request) {
        return ResponseEntity.ok(courseService.updateCourse(courseId, request));
    }

    @Operation(
            summary = "Get course by ID",
            description = "Kurs haqida to‘liq maʼlumotni ID orqali olish uchun ishlatiladi."
    )
    @ApiResponse(responseCode = "200", description = "Kurs maʼlumotlari qaytarildi")
    @ApiResponse(responseCode = "404", description = "Kurs topilmadi")

    @GetMapping("course/{courseId}")
    public ResponseEntity<ResponseDto<AdminCourseResponseDto>> getCourse(@PathVariable(name = "courseId") UUID courseId) {
        return ResponseEntity.ok(courseService.getCourseById(courseId));
    }

    @Operation(
            summary = "Get all courses (pagination)",
            description = "Barcha kurslarni sahifalab olish uchun ishlatiladi. Page va size parametrlarini qabul qiladi."
    )
    @ApiResponse(responseCode = "200", description = "Kurslar ro‘yxati qaytarildi")

    @GetMapping("get-all-courses-pagination")
    public ResponseEntity<ResponseDto<Page<AdminCourseResponseDto>>> getAllCoursePagination(
            @RequestParam(name = "page") int page, @RequestParam(name = "size") int size
    ) {
        return ResponseEntity.ok(courseService.getAllCourses(page, size));
    }

    @Operation(
            summary = "Get all courses",
            description = "Barcha kurslarning to‘liq ro‘yxatini olish uchun ishlatiladi."
    )
    @ApiResponse(responseCode = "200", description = "Kurslar ro‘yxati qaytarildi")

    @GetMapping("get-all-courses")
    public ResponseEntity<ResponseDto<List<AdminCourseResponseDto>>> getAllCourse() {
        return ResponseEntity.ok(courseService.getAllCourses());
    }

    @Operation(
            summary = "Get courses by status (pagination)",
            description = "Kurslarni holati bo‘yicha sahifalab olish uchun ishlatiladi."
    )
    @ApiResponse(responseCode = "200", description = "Kurslar ro‘yxati qaytarildi")
    @ApiResponse(responseCode = "400", description = "Noto‘g‘ri status qiymati yuborilgan")

    @GetMapping("get-all-courses-by-status-pagination")
    public ResponseEntity<ResponseDto<Page<AdminCourseResponseDto>>> getAllCourseByStatusPagination(
            @RequestParam(name = "status") CourseStatus status,
            @RequestParam(name = "page") int page, @RequestParam(name = "size") int size
    ) {
        return ResponseEntity.ok(courseService.getByStatus(status, page, size));
    }

    @Operation(
            summary = "Get courses by status",
            description = "Kurslarni belgilangan holati bo‘yicha olish uchun ishlatiladi."
    )
    @ApiResponse(responseCode = "200", description = "Kurslar ro‘yxati qaytarildi")
    @ApiResponse(responseCode = "400", description = "Noto‘g‘ri status qiymati yuborilgan")

    @GetMapping("get-all-courses-by-status")
    public ResponseEntity<ResponseDto<List<AdminCourseResponseDto>>> getAllCourseByStatus(
            @RequestParam(name = "status") CourseStatus status
    ) {
        return ResponseEntity.ok(courseService.getByStatus(status));
    }

    @Operation(
            summary = "Change course status",
            description = "Kursning holatini o‘zgartirish uchun ishlatiladi."
    )
    @ApiResponse(responseCode = "200", description = "Kurs holati muvaffaqiyatli o‘zgartirildi")
    @ApiResponse(responseCode = "400", description = "Noto‘g‘ri status qiymati yuborilgan")
    @ApiResponse(responseCode = "404", description = "Kurs topilmadi")

    @PutMapping("change-course-status/{courseId}")
    public ResponseEntity<ResponseDto<AdminCourseResponseDto>> changeCourseStatus(
            @PathVariable(name = "courseId") UUID courseId,
            @RequestParam(name = "status") CourseStatus status
    ) {
        return ResponseEntity.ok(courseService.changeCourseStatus(courseId, status));
    }

    @Operation(
            summary = "Soft delete course",
            description = "Kursni tizimdan butunlay o‘chirib yubormasdan, faqat soft delete qilish uchun ishlatiladi."
    )
    @ApiResponse(responseCode = "200", description = "Kurs soft delete qilindi")
    @ApiResponse(responseCode = "404", description = "Kurs topilmadi")

    @DeleteMapping("soft-delete/{courseId}")
    public ResponseEntity<ResponseDto<Void>> softDelete(@PathVariable(name = "courseId") UUID courseId) {
        return ResponseEntity.ok(courseService.softDelete(courseId));
    }

    @Operation(
            summary = "Hard delete course",
            description = "Kursni tizimdan butunlay o‘chirib tashlash uchun ishlatiladi."
    )
    @ApiResponse(responseCode = "200", description = "Kurs muvaffaqiyatli o‘chirildi")
    @ApiResponse(responseCode = "404", description = "Kurs topilmadi")

    @DeleteMapping("hard-delete/{courseId}")
    public ResponseEntity<ResponseDto<Void>> hardDelete(@PathVariable(name = "courseId") UUID courseId) {
        return ResponseEntity.ok(courseService.hardDelete(courseId));
    }

    @Operation(
            summary = "Update course pricing",
            description = "Kurs narxi va chegirma maʼlumotlarini yangilash uchun ishlatiladi."
    )
    @ApiResponse(responseCode = "200", description = "Kurs narxi muvaffaqiyatli yangilandi")
    @ApiResponse(responseCode = "400", description = "Noto‘g‘ri maʼlumot yuborilgan")
    @ApiResponse(responseCode = "404", description = "Kurs topilmadi")

    @PutMapping("update-course-pricing/{courseId}")
    public ResponseEntity<ResponseDto<AdminCourseResponseDto>> updateCoursePricing(@PathVariable(name = "courseId") UUID courseId, @RequestBody AdminCoursePricingRequestDto req) {
        return ResponseEntity.ok(courseService.updateCoursePricing(courseId, req));
    }

    @Operation(
            summary = "Restore course",
            description = "Soft delete qilingan kursni tiklash uchun ishlatiladi."
    )
    @ApiResponse(responseCode = "200", description = "Kurs muvaffaqiyatli tiklandi")
    @ApiResponse(responseCode = "404", description = "Kurs topilmadi")
    @PutMapping("restore-course/{courseId}")
    public ResponseEntity<ResponseDto<AdminCourseResponseDto>> restoreCourse(@PathVariable(name = "courseId") UUID courseId) {
        return ResponseEntity.ok(courseService.restoreCourse(courseId));
    }


    /// to'liqmaslarida xullase

    @GetMapping("/promo-code/{promoCodeId}")
    public ResponseEntity<ResponseDto<AdminCourseAndPromoCodeResponseDto>> findByPromoCodeById(@PathVariable(name = "promoCodeId") UUID promoCodeId) {
        return ResponseEntity.ok(courseService.findByPromoCodeId(promoCodeId));
    }

    @GetMapping("/promo-code/find-by-promo-code/{code}")

    public ResponseEntity<ResponseDto<AdminCourseAndPromoCodeResponseDto>> findByPromoCode(@PathVariable(name = "code") String code) {
        return ResponseEntity.ok(courseService.findByPromoCode(code));
    }

    @GetMapping("/promo-code/is-exists/{code}")
    public ResponseEntity<Boolean> existsByPromoCode(@PathVariable(name = "code") String code) {
        return ResponseEntity.ok(courseService.existsByPromoCode(code));
    }

    @GetMapping("/promo-code/get-all-promo-codes")
    public ResponseEntity<ResponseDto<List<AdminCourseAndPromoCodeResponseDto>>> getAllPromoCodes() {
        return ResponseEntity.ok(courseService.getAllPromoCodes());
    }

    @GetMapping("/promo-code/get-all-promo-codes-pagination")
    public ResponseEntity<ResponseDto<Page<AdminCourseAndPromoCodeResponseDto>>> getAllPromoCodes(
            @RequestParam(name = "page") int page,
            @RequestParam(name = "size") int size
    ) {
        return ResponseEntity.ok(courseService.getAllPromoCodes(page, size));
    }

    @GetMapping("/promo-code/get-all-promo-codes-by-course-id-pagination/{courseId}")
    public ResponseEntity<ResponseDto<Page<AdminCourseAndPromoCodeResponseDto>>> getAllPromoCodesByCourseId(
            @PathVariable(name = "courseId") UUID courseId,
            @RequestParam(name = "page") int page,
            @RequestParam(name = "size") int size
    ) {
        return ResponseEntity.ok(courseService.getAllPromoCodesByCourseId(courseId, page, size));
    }

    @GetMapping("/promo-code/get-all-promo-codes-by-course-id/{courseId}")
    public ResponseEntity<ResponseDto<List<AdminCourseAndPromoCodeResponseDto>>> getAllPromoCodesByCourseId(
            @PathVariable(name = "courseId") UUID courseId
    ) {
        return ResponseEntity.ok(courseService.getAllPromoCodesByCourseId(courseId));
    }

    @GetMapping("/promo-code/get-all-promo-codes-by-course-id-pagination/{teacherId}")
    public ResponseEntity<ResponseDto<Page<AdminCourseAndPromoCodeResponseDto>>> getAllPromoCodesByTeacherId(
            @PathVariable(name = "teacherId") Long teacherId,
            @RequestParam(name = "page") int page,
            @RequestParam(name = "size") int size
    ) {
        return ResponseEntity.ok(courseService.getAllPromoCodesByTeacherId(teacherId, page, size));
    }

    @GetMapping("/promo-code/get-all-promo-codes-by-course-id/{teacherId}")
    public ResponseEntity<ResponseDto<List<AdminCourseAndPromoCodeResponseDto>>> getAllPromoCodesByTeacherId(
            @PathVariable(name = "teacherId") Long teacherId
    ) {
        return ResponseEntity.ok(courseService.getAllPromoCodesByTeacherId(teacherId));
    }

    @PostMapping("/promo-code/add/{courseId}")
    public ResponseEntity<ResponseDto<AdminCourseAndPromoCodeResponseDto>> addPromoCodeToCourse(
            @PathVariable("courseId") UUID courseId,
            @RequestBody AdminCreatePromoCodeRequestDto req,
            @AuthenticationPrincipal UserPrincipal principal
    ) {
        req.setUser(principal.getUser());
        return ResponseEntity.ok(courseService.addPromoCodeToCourse(courseId, req));
    }
}
