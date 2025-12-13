package uz.codebyz.onlinecoursebackend.payment.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
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
import uz.codebyz.onlinecoursebackend.payment.dto.request.TeacherSubscriptionRequest;
import uz.codebyz.onlinecoursebackend.payment.dto.response.TeacherSubscriptionResponse;
import uz.codebyz.onlinecoursebackend.payment.entity.SubscriptionStatus;
import uz.codebyz.onlinecoursebackend.payment.service.TeacherSubscriptionService;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/teacher-subscriptions")
@Tag(name = "Teacher Subscription Management",
        description = "O'qituvchi obunalarini boshqarish uchun API endpoints. " +
                "O'qituvchilar platformadan foydalanish uchun obuna sotib olishadi: " +
                "1 oylik, 3 oylik, 6 oylik, 1 yillik, 2 yillik, 3 yillik obunalar mavjud.")
public class TeacherSubscriptionController {

    @Autowired
    private TeacherSubscriptionService teacherSubscriptionService;

    @PostMapping
    @Operation(
            summary = "Yangi o'qituvchi obunasi yaratish",
            description = "O'qituvchi uchun yangi obuna yaratadi. " +
                    "Obuna dastlab PENDING holatida yaratiladi va to'lov amalga oshgandan keyin ACTIVE holatiga o'tadi. " +
                    "Boshlanish va tugash sanalari avtomatik hisoblanadi."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "O'qituvchi obunasi muvaffaqiyatli yaratildi",
                    content = @Content(schema = @Schema(implementation = ResponseDto.class))
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Yaroqsiz so'rov yoki validatsiya xatosi. " +
                            "O'qituvchida allaqachon faol obuna mavjud bo'lishi mumkin.",
                    content = @Content(schema = @Schema(implementation = ResponseDto.class))
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "O'qituvchi yoki obuna rejasi topilmadi",
                    content = @Content(schema = @Schema(implementation = ResponseDto.class))
            )
    })
    public ResponseEntity<ResponseDto<TeacherSubscriptionResponse>> createTeacherSubscription(
            @Parameter(
                    description = "Yangi obuna parametrlari. O'qituvchi ID va obuna rejasi ID kerak.",
                    required = true,
                    schema = @Schema(implementation = TeacherSubscriptionRequest.class)
            )
            @Valid @RequestBody TeacherSubscriptionRequest request) {
        TeacherSubscriptionResponse response = teacherSubscriptionService.createTeacherSubscription(request);
        return ResponseEntity.ok(ResponseDto.ok("O'qituvchi obunasi yaratildi", response));
    }

    @GetMapping("/{id}")
    @Operation(
            summary = "O'qituvchi obunasi ma'lumotlarini olish",
            description = "UUID orqali o'qituvchi obunasining batafsil ma'lumotlarini olish. " +
                    "Obuna holati, boshlanish/tugash sanalari, bog'langan to'lov va reja ma'lumotlari."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "O'qituvchi obunasi ma'lumotlari topildi",
                    content = @Content(schema = @Schema(implementation = ResponseDto.class))
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Berilgan ID bilan obuna topilmadi",
                    content = @Content(schema = @Schema(implementation = ResponseDto.class))
            )
    })
    public ResponseEntity<ResponseDto<TeacherSubscriptionResponse>> getTeacherSubscription(
            @Parameter(
                    description = "O'qituvchi obunasi UUID identifikatori",
                    required = true,
                    example = "123e4567-e89b-12d3-a456-426614174000"
            )
            @PathVariable UUID id) {
        TeacherSubscriptionResponse response = teacherSubscriptionService.getTeacherSubscriptionById(id);
        return ResponseEntity.ok(ResponseDto.ok("O'qituvchi obunasi ma'lumotlari", response));
    }

    @GetMapping("/teacher/{teacherId}")
    @Operation(
            summary = "O'qituvchining barcha obunalarini olish",
            description = "O'qituvchi ID bo'yicha uning barcha obunalari tarixini olish. " +
                    "Tartib: eng yangisi birinchi."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "O'qituvchi obunalari ro'yxati",
                    content = @Content(array = @ArraySchema(schema = @Schema(implementation = TeacherSubscriptionResponse.class)))
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "O'qituvchi topilmadi",
                    content = @Content(schema = @Schema(implementation = ResponseDto.class))
            )
    })
    public ResponseEntity<ResponseDto<List<TeacherSubscriptionResponse>>> getTeacherSubscriptions(
            @Parameter(
                    description = "O'qituvchi ID identifikatori",
                    required = true,
                    example = "123"
            )
            @PathVariable Long teacherId) {
        List<TeacherSubscriptionResponse> subscriptions = teacherSubscriptionService
                .getTeacherSubscriptionsByTeacherId(teacherId);
        return ResponseEntity.ok(ResponseDto.ok("O'qituvchi obunalari", subscriptions));
    }

    @GetMapping("/teacher/{teacherId}/active")
    @Operation(
            summary = "O'qituvchining faol obunasini olish",
            description = "O'qituvchining hozirda faol (ACTIVE) holatdagi obunasini olish. " +
                    "Platforma foydalanish huquqini tekshirish uchun ishlatiladi."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Faol obuna ma'lumotlari topildi",
                    content = @Content(schema = @Schema(implementation = ResponseDto.class))
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "O'qituvchi uchun faol obuna topilmadi",
                    content = @Content(schema = @Schema(implementation = ResponseDto.class))
            )
    })
    public ResponseEntity<ResponseDto<TeacherSubscriptionResponse>> getActiveTeacherSubscription(
            @Parameter(
                    description = "O'qituvchi ID identifikatori",
                    required = true,
                    example = "123"
            )
            @PathVariable Long teacherId) {
        TeacherSubscriptionResponse response = teacherSubscriptionService.getActiveTeacherSubscription(teacherId);
        return ResponseEntity.ok(ResponseDto.ok("Faol obuna ma'lumotlari", response));
    }

    @GetMapping("/status/{status}")
    @Operation(
            summary = "Holat bo'yicha obunalarni filtrlash",
            description = "Obuna holati bo'yicha filtr qo'llab, natijalarni olish. " +
                    "Holatlar: ACTIVE, EXPIRED, CANCELLED, PENDING. " +
                    "EXPIRED holatdagi obunalar har kuni tizim tomonidan avtomatik yangilanadi."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Filtr qilingan obunalar ro'yxati",
                    content = @Content(array = @ArraySchema(schema = @Schema(implementation = TeacherSubscriptionResponse.class)))
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Yaroqsiz holat parametri",
                    content = @Content(schema = @Schema(implementation = ResponseDto.class))
            )
    })
    public ResponseEntity<ResponseDto<List<TeacherSubscriptionResponse>>> getSubscriptionsByStatus(
            @Parameter(
                    description = "Obuna holati (ACTIVE, EXPIRED, CANCELLED, PENDING)",
                    required = true,
                    example = "ACTIVE",
                    schema = @Schema(implementation = SubscriptionStatus.class)
            )
            @PathVariable SubscriptionStatus status) {
        List<TeacherSubscriptionResponse> subscriptions = teacherSubscriptionService
                .getSubscriptionsByStatus(status);
        return ResponseEntity.ok(ResponseDto.ok("Obunalar ro'yxati", subscriptions));
    }

    @PatchMapping("/{id}/status")
    @Operation(
            summary = "Obuna holatini yangilash",
            description = "O'qituvchi obunasining holatini yangilash. " +
                    "Masalan, to'lov tasdiqlanganda PENDING → ACTIVE holatiga o'tkazish. " +
                    "Yoki admin tomonidan CANCELLED holatiga o'tkazish."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Obuna holati muvaffaqiyatli yangilandi",
                    content = @Content(schema = @Schema(implementation = ResponseDto.class))
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Yaroqsiz holat o'tish yoki operatsiya",
                    content = @Content(schema = @Schema(implementation = ResponseDto.class))
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Obuna topilmadi",
                    content = @Content(schema = @Schema(implementation = ResponseDto.class))
            )
    })
    public ResponseEntity<ResponseDto<TeacherSubscriptionResponse>> updateSubscriptionStatus(
            @Parameter(
                    description = "Yangilanadigan obuna UUID",
                    required = true,
                    example = "123e4567-e89b-12d3-a456-426614174000"
            )
            @PathVariable UUID id,
            @Parameter(
                    description = "Yangi obuna holati",
                    required = true,
                    example = "ACTIVE",
                    schema = @Schema(implementation = SubscriptionStatus.class)
            )
            @RequestParam SubscriptionStatus status) {
        TeacherSubscriptionResponse response = teacherSubscriptionService.updateSubscriptionStatus(id, status);
        return ResponseEntity.ok(ResponseDto.ok("Obuna holati yangilandi", response));
    }

    @DeleteMapping("/{id}")
    @Operation(
            summary = "O'qituvchi obunasini bekor qilish",
            description = "O'qituvchi obunasini bekor qilish (CANCELLED holatiga o'tkazish). " +
                    "Bu obuna muddati tugamaguncha platforma foydalanish huquqini bekor qiladi."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Obuna muvaffaqiyatli bekor qilindi",
                    content = @Content(schema = @Schema(implementation = ResponseDto.class))
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Obuna topilmadi",
                    content = @Content(schema = @Schema(implementation = ResponseDto.class))
            )
    })
    public ResponseEntity<ResponseDto<Void>> cancelTeacherSubscription(
            @Parameter(
                    description = "Bekor qilinadigan obuna UUID",
                    required = true,
                    example = "123e4567-e89b-12d3-a456-426614174000"
            )
            @PathVariable UUID id) {
        teacherSubscriptionService.cancelTeacherSubscription(id);
        return ResponseEntity.ok(ResponseDto.ok("Obuna bekor qilindi"));
    }

    @GetMapping("/teacher/{teacherId}/has-active")
    @Operation(
            summary = "O'qituvchining faol obunasini tekshirish",
            description = "O'qituvchida hozirda faol obuna mavjudligini tekshirish. " +
                    "Bu metod o'qituvchi platformaga kirish huquqini tekshirishda ishlatiladi."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Faol obuna mavjudligi natijasi",
                    content = @Content(schema = @Schema(implementation = ResponseDto.class))
            )
    })
    public ResponseEntity<ResponseDto<Boolean>> hasActiveSubscription(
            @Parameter(
                    description = "O'qituvchi ID identifikatori",
                    required = true,
                    example = "123"
            )
            @PathVariable Long teacherId) {
        boolean hasActive = teacherSubscriptionService.hasActiveSubscription(teacherId);
        return ResponseEntity.ok(ResponseDto.ok("Faol obuna mavjudligi", hasActive));
    }
    // ... oldingi endpointlar ...

    @PostMapping("/check-expired")
    @Operation(
            summary = "Muddati tugagan obunalarni tekshirish va yangilash",
            description = """
                    Muddati tugagan (endDate < hozirgi vaqt) faol obunalarni avtomatik tekshirib, 
                    ularni EXPIRED holatiga o'tkazadi.
                    
                    ## Mexanizm:
                    1. Tizim har kuni 00:00 da @Scheduled(cron = "0 0 0 * * ?") orqali avtomatik ishga tushadi
                    2. EndDate sanasi o'tgan, ammo hali ACTIVE holatdagi obunalarni topadi
                    3. Ushbu obunalarni EXPIRED holatiga o'tkazadi
                    4. Admin panel orqali qo'lda ham ishga tushirish mumkin
                    
                    ## Qachon ishlatiladi:
                    - Har kuni avtomatik (cron job)
                    - Admin panel orqali qo'lda tekshirish kerak bo'lganda
                    - Obuna holatlarini sinxronlashtirish kerak bo'lganda
                    """
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Muddati tugagan obunalar muvaffaqiyatli tekshirildi va yangilandi",
                    content = @Content(schema = @Schema(implementation = Object.class))
            ),
            @ApiResponse(
                    responseCode = "403",
                    description = "Faqat ADMIN rolidagi foydalanuvchilar ushbu operatsiyani bajarishi mumkin",
                    content = @Content(schema = @Schema(implementation = ResponseDto.class))
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Server xatosi yoki ma'lumotlar bazasi bilan muammo",
                    content = @Content(schema = @Schema(implementation = ResponseDto.class))
            )
    })
    @io.swagger.v3.oas.annotations.security.SecurityRequirement(name = "bearerAuth")
    public ResponseEntity<ResponseDto<String>> checkAndUpdateExpiredSubscriptions() {
        teacherSubscriptionService.checkAndUpdateExpiredSubscriptions();
        return ResponseEntity.ok(ResponseDto.ok("Muddati tugagan obunalar yangilandi"));
    }
}