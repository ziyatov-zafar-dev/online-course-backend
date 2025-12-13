package uz.codebyz.onlinecoursebackend.payment.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uz.codebyz.onlinecoursebackend.common.ResponseDto;
import uz.codebyz.onlinecoursebackend.payment.dto.request.CoursePaymentRequest;
import uz.codebyz.onlinecoursebackend.payment.dto.response.CoursePaymentResponse;
import uz.codebyz.onlinecoursebackend.payment.entity.CoursePaymentStatus;
import uz.codebyz.onlinecoursebackend.payment.service.CoursePaymentService;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/course-payments")
@Tag(name = "Course Payment Management", description = "Kurs to'lovlari bilan ishlash uchun API endpoints")
public class CoursePaymentController {

    @Autowired
    private CoursePaymentService coursePaymentService;

    @PostMapping
    @Operation(
            summary = "Yangi kurs to'lovi yaratish",
            description = "O'quvchi uchun yangi kurs to'lovi yaratadi. " +
                    "To'lov dastlab PENDING holatida yaratiladi va keyinchalik to'lov provideri orqali tasdiqlanishi kerak."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Kurs to'lovi muvaffaqiyatli yaratildi",
                    content = @Content(schema = @Schema(implementation = ResponseDto.class))
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Yaroqsiz so'rov yoki validatsiya xatosi",
                    content = @Content(schema = @Schema(implementation = ResponseDto.class))
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "O'quvchi yoki kurs topilmadi",
                    content = @Content(schema = @Schema(implementation = ResponseDto.class))
            ),
            @ApiResponse(
                    responseCode = "409",
                    description = "O'quvchi ushbu kurs uchun allaqachon to'lov qilgan",
                    content = @Content(schema = @Schema(implementation = ResponseDto.class))
            )
    })
    public ResponseEntity<ResponseDto<CoursePaymentResponse>> createCoursePayment(
            @Parameter(
                    description = "Kurs to'lovi ma'lumotlari",
                    required = true,
                    schema = @Schema(implementation = CoursePaymentRequest.class)
            )
            @Valid @RequestBody CoursePaymentRequest request) {
        CoursePaymentResponse response = coursePaymentService.createCoursePayment(request);
        return ResponseEntity.ok(ResponseDto.ok("Kurs to'lovi yaratildi", response));
    }

    @GetMapping("/{id}")
    @Operation(
            summary = "Kurs to'lovi ma'lumotlarini olish",
            description = "ID orqali kurs to'lovining batafsil ma'lumotlarini olish"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Kurs to'lovi ma'lumotlari",
                    content = @Content(schema = @Schema(implementation = ResponseDto.class))
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Berilgan ID bilan kurs to'lovi topilmadi",
                    content = @Content(schema = @Schema(implementation = ResponseDto.class))
            )
    })
    public ResponseEntity<ResponseDto<CoursePaymentResponse>> getCoursePayment(
            @Parameter(
                    description = "Kurs to'lovi UUID",
                    required = true,
                    example = "123e4567-e89b-12d3-a456-426614174000"
            )
            @PathVariable UUID id) {
        CoursePaymentResponse response = coursePaymentService.getCoursePaymentById(id);
        return ResponseEntity.ok(ResponseDto.ok("Kurs to'lovi ma'lumotlari", response));
    }

    @GetMapping("/student/{studentId}")
    @Operation(
            summary = "O'quvchining barcha kurs to'lovlari",
            description = "O'quvchi ID-si bo'yicha uning barcha kurs to'lovlarini olish"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "O'quvchi kurs to'lovlari ro'yxati",
                    content = @Content(schema = @Schema(implementation = ResponseDto.class))
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "O'quvchi topilmadi",
                    content = @Content(schema = @Schema(implementation = ResponseDto.class))
            )
    })
    public ResponseEntity<ResponseDto<List<CoursePaymentResponse>>> getCoursePaymentsByStudent(
            @Parameter(
                    description = "O'quvchi UUID",
                    required = true,
                    example = "123e4567-e89b-12d3-a456-426614174000"
            )
            @PathVariable UUID studentId) {
        List<CoursePaymentResponse> payments = coursePaymentService.getCoursePaymentsByStudentId(studentId);
        return ResponseEntity.ok(ResponseDto.ok("O'quvchi kurs to'lovlari", payments));
    }

    @GetMapping("/course/{courseId}")
    @Operation(
            summary = "Kursning barcha to'lovlari",
            description = "Kurs ID-si bo'yicha uning barcha to'lovlarini olish"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Kurs to'lovlari ro'yxati",
                    content = @Content(schema = @Schema(implementation = ResponseDto.class))
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Kurs topilmadi",
                    content = @Content(schema = @Schema(implementation = ResponseDto.class))
            )
    })
    public ResponseEntity<ResponseDto<List<CoursePaymentResponse>>> getCoursePaymentsByCourse(
            @Parameter(
                    description = "Kurs UUID",
                    required = true,
                    example = "123e4567-e89b-12d3-a456-426614174000"
            )
            @PathVariable UUID courseId) {
        List<CoursePaymentResponse> payments = coursePaymentService.getCoursePaymentsByCourseId(courseId);
        return ResponseEntity.ok(ResponseDto.ok("Kurs to'lovlari", payments));
    }

    @GetMapping("/student/{studentId}/course/{courseId}")
    @Operation(
            summary = "O'quvchi va kurs bo'yicha to'lov ma'lumotlari",
            description = "Muayyan o'quvchi va kurs uchun to'lov ma'lumotlarini olish"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Kurs to'lovi ma'lumotlari",
                    content = @Content(schema = @Schema(implementation = ResponseDto.class))
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "To'lov topilmadi",
                    content = @Content(schema = @Schema(implementation = ResponseDto.class))
            )
    })
    public ResponseEntity<ResponseDto<CoursePaymentResponse>> getCoursePaymentByStudentAndCourse(
            @Parameter(
                    description = "O'quvchi UUID",
                    required = true,
                    example = "123e4567-e89b-12d3-a456-426614174000"
            )
            @PathVariable UUID studentId,
            @Parameter(
                    description = "Kurs UUID",
                    required = true,
                    example = "123e4567-e89b-12d3-a456-426614174000"
            )
            @PathVariable UUID courseId) {
        CoursePaymentResponse response = coursePaymentService
                .getCoursePaymentByStudentAndCourse(studentId, courseId);
        return ResponseEntity.ok(ResponseDto.ok("Kurs to'lovi ma'lumotlari", response));
    }

    @PatchMapping("/{id}/status")
    @Operation(
            summary = "Kurs to'lovi holatini yangilash",
            description = "Kurs to'lovi holatini yangilash (masalan, PENDING → PAID yoki PAID → REFUNDED)"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Kurs to'lovi holati muvaffaqiyatli yangilandi",
                    content = @Content(schema = @Schema(implementation = ResponseDto.class))
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Yaroqsiz holat yoki operatsiya",
                    content = @Content(schema = @Schema(implementation = ResponseDto.class))
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Kurs to'lovi topilmadi",
                    content = @Content(schema = @Schema(implementation = ResponseDto.class))
            )
    })
    public ResponseEntity<ResponseDto<CoursePaymentResponse>> updateCoursePaymentStatus(
            @Parameter(
                    description = "Kurs to'lovi UUID",
                    required = true,
                    example = "123e4567-e89b-12d3-a456-426614174000"
            )
            @PathVariable UUID id,
            @Parameter(
                    description = "Yangi holat (PAID, PENDING, REFUNDED)",
                    required = true,
                    example = "PAID",
                    schema = @Schema(implementation = CoursePaymentStatus.class)
            )
            @RequestParam CoursePaymentStatus status) {
        CoursePaymentResponse response = coursePaymentService.updateCoursePaymentStatus(id, status);
        return ResponseEntity.ok(ResponseDto.ok("Kurs to'lovi holati yangilandi", response));
    }

    @GetMapping("/student/{studentId}/course/{courseId}/has-paid")
    @Operation(
            summary = "O'quvchining kurs uchun to'lashini tekshirish",
            description = "O'quvchi ma'lum kurs uchun to'lov qilganligini tekshirish"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "To'lov mavjudligi natijasi",
                    content = @Content(schema = @Schema(implementation = ResponseDto.class))
            )
    })
    public ResponseEntity<ResponseDto<Boolean>> hasPaidForCourse(
            @Parameter(
                    description = "O'quvchi UUID",
                    required = true,
                    example = "123e4567-e89b-12d3-a456-426614174000"
            )
            @PathVariable UUID studentId,
            @Parameter(
                    description = "Kurs UUID",
                    required = true,
                    example = "123e4567-e89b-12d3-a456-426614174000"
            )
            @PathVariable UUID courseId) {
        boolean hasPaid = coursePaymentService.hasPaidForCourse(studentId, courseId);
        return ResponseEntity.ok(ResponseDto.ok("Kurs uchun to'lov mavjudligi", hasPaid));
    }

    @PostMapping("/{id}/refund")
    @Operation(
            summary = "Kurs to'lovini qaytarish (refund)",
            description = "Amalga oshirilgan kurs to'lovini qaytarish. " +
                    "Bu operatsiya to'lovni REFUNDED holatiga o'tkazadi va tegishli yozuvlarni yangilaydi."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Kurs to'lovi muvaffaqiyatli qaytarildi",
                    content = @Content(schema = @Schema(implementation = ResponseDto.class))
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "To'lov qaytarish mumkin emas (masalan, to'lov PAID holatida emas)",
                    content = @Content(schema = @Schema(implementation = ResponseDto.class))
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Kurs to'lovi topilmadi",
                    content = @Content(schema = @Schema(implementation = ResponseDto.class))
            )
    })
    public ResponseEntity<ResponseDto<CoursePaymentResponse>> refundCoursePayment(
            @Parameter(
                    description = "Kurs to'lovi UUID",
                    required = true,
                    example = "123e4567-e89b-12d3-a456-426614174000"
            )
            @PathVariable UUID id) {
        CoursePaymentResponse response = coursePaymentService.refundCoursePayment(id);
        return ResponseEntity.ok(ResponseDto.ok("Kurs to'lovi qaytarildi", response));
    }

    @GetMapping("/student/{studentId}/paid-courses")
    @Operation(
            summary = "O'quvchining to'langan kurslari",
            description = "O'quvchi tomonidan to'langan barcha kurslar ro'yxati"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "To'langan kurslar ro'yxati",
                    content = @Content(schema = @Schema(implementation = ResponseDto.class))
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "O'quvchi topilmadi",
                    content = @Content(schema = @Schema(implementation = ResponseDto.class))
            )
    })
    public ResponseEntity<ResponseDto<List<CoursePaymentResponse>>> getPaidCoursesByStudent(
            @Parameter(
                    description = "O'quvchi UUID",
                    required = true,
                    example = "123e4567-e89b-12d3-a456-426614174000"
            )
            @PathVariable UUID studentId) {
        List<CoursePaymentResponse> payments = coursePaymentService.getPaidCoursesByStudent(studentId);
        return ResponseEntity.ok(ResponseDto.ok("To'langan kurslar", payments));
    }
}