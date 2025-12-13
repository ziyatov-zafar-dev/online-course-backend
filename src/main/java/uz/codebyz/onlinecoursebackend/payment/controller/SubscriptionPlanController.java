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
import uz.codebyz.onlinecoursebackend.payment.dto.request.CreateSubscriptionPlanRequest;
import uz.codebyz.onlinecoursebackend.payment.dto.response.SubscriptionPlanResponse;
import uz.codebyz.onlinecoursebackend.payment.entity.SubscriptionPeriod;
import uz.codebyz.onlinecoursebackend.payment.service.SubscriptionPlanService;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/subscription-plans")
@Tag(name = "Subscription Plan Management",
        description = "O'qituvchi obuna rejalarini boshqarish uchun API endpoints. " +
                "Admin panel orqali 1 oylik, 3 oylik, 6 oylik, 1 yillik, 2 yillik, 3 yillik rejalarni boshqarish mumkin.")
public class SubscriptionPlanController {

    @Autowired
    private SubscriptionPlanService subscriptionPlanService;

    @PostMapping
    @Operation(
            summary = "Yangi obuna rejasi yaratish",
            description = "O'qituvchilar uchun yangi obuna rejasini yaratadi. " +
                    "Davr (period), narx, nom va tavsiflar o'zbek va ingliz tillarida kiritiladi. " +
                    "Admin panel orqali barcha parametrlarni o'zgartirish mumkin."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Obuna rejasi muvaffaqiyatli yaratildi",
                    content = @Content(schema = @Schema(implementation = ResponseDto.class))
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Yaroqsiz so'rov yoki validatsiya xatosi. " +
                            "Davr uchun faol reja allaqachon mavjud bo'lishi mumkin.",
                    content = @Content(schema = @Schema(implementation = ResponseDto.class))
            ),
            @ApiResponse(
                    responseCode = "403",
                    description = "Faqat ADMIN rolidagi foydalanuvchilar yangi reja yarata oladi",
                    content = @Content(schema = @Schema(implementation = ResponseDto.class))
            )
    })
    public ResponseEntity<ResponseDto<SubscriptionPlanResponse>> createSubscriptionPlan(
            @Parameter(
                    description = "Yangi obuna rejasi parametrlari. " +
                            "Davrlar: MONTHLY, QUARTERLY, SEMI_ANNUAL, ANNUAL, BIENNIAL, TRIENNIAL.",
                    required = true,
                    schema = @Schema(implementation = CreateSubscriptionPlanRequest.class)
            )
            @Valid @RequestBody CreateSubscriptionPlanRequest request) {
        SubscriptionPlanResponse response = subscriptionPlanService.createSubscriptionPlan(request);
        return ResponseEntity.ok(ResponseDto.ok("Obuna rejasi yaratildi", response));
    }

    @GetMapping("/{id}")
    @Operation(
            summary = "Obuna rejasi ma'lumotlarini olish",
            description = "UUID orqali obuna rejasining batafsil ma'lumotlarini olish. " +
                    "Davr, narx, nom va tavsiflar o'zbek va ingliz tillarida qaytariladi."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Obuna rejasi ma'lumotlari topildi",
                    content = @Content(schema = @Schema(implementation = ResponseDto.class))
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Berilgan ID bilan obuna rejasi topilmadi",
                    content = @Content(schema = @Schema(implementation = ResponseDto.class))
            )
    })
    public ResponseEntity<ResponseDto<SubscriptionPlanResponse>> getSubscriptionPlan(
            @Parameter(
                    description = "Obuna rejasi UUID identifikatori",
                    required = true,
                    example = "123e4567-e89b-12d3-a456-426614174000"
            )
            @PathVariable UUID id) {
        SubscriptionPlanResponse response = subscriptionPlanService.getSubscriptionPlanById(id);
        return ResponseEntity.ok(ResponseDto.ok("Obuna rejasi ma'lumotlari", response));
    }

    @GetMapping
    @Operation(
            summary = "Barcha obuna rejalarini olish",
            description = "Sistemadagi barcha obuna rejalarini ro'yxatini olish. " +
                    "Faol va nofaol rejalar ham kiritiladi."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Obuna rejalari ro'yxati muvaffaqiyatli qaytarildi",
                    content = @Content(array = @ArraySchema(schema = @Schema(implementation = SubscriptionPlanResponse.class)))
            )
    })
    public ResponseEntity<ResponseDto<List<SubscriptionPlanResponse>>> getAllSubscriptionPlans() {
        List<SubscriptionPlanResponse> plans = subscriptionPlanService.getAllSubscriptionPlans();
        return ResponseEntity.ok(ResponseDto.ok("Obuna rejalari ro'yxati", plans));
    }

    @GetMapping("/active")
    @Operation(
            summary = "Faol obuna rejalarini olish",
            description = "Faqat aktiv (active=true) holatdagi obuna rejalarini olish. " +
                    "O'qituvchilar uchun tanlash mumkin bo'lgan rejalar ro'yxati."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Faol obuna rejalari ro'yxati",
                    content = @Content(array = @ArraySchema(schema = @Schema(implementation = SubscriptionPlanResponse.class)))
            )
    })
    public ResponseEntity<ResponseDto<List<SubscriptionPlanResponse>>> getActiveSubscriptionPlans() {
        List<SubscriptionPlanResponse> plans = subscriptionPlanService.getActiveSubscriptionPlans();
        return ResponseEntity.ok(ResponseDto.ok("Faol obuna rejalari", plans));
    }

    @GetMapping("/period/{period}")
    @Operation(
            summary = "Davr bo'yicha obuna rejasini olish",
            description = "Muayyan davr uchun faol obuna rejasini olish. " +
                    "Davrlar: 1 oylik (MONTHLY), 3 oylik (QUARTERLY), 6 oylik (SEMI_ANNUAL), " +
                    "1 yillik (ANNUAL), 2 yillik (BIENNIAL), 3 yillik (TRIENNIAL)."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Davr bo'yicha obuna rejasi topildi",
                    content = @Content(schema = @Schema(implementation = ResponseDto.class))
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Berilgan davr uchun faol obuna rejasi topilmadi",
                    content = @Content(schema = @Schema(implementation = ResponseDto.class))
            )
    })
    public ResponseEntity<ResponseDto<SubscriptionPlanResponse>> getSubscriptionPlanByPeriod(
            @Parameter(
                    description = "Obuna davri (MONTHLY, QUARTERLY, SEMI_ANNUAL, ANNUAL, BIENNIAL, TRIENNIAL)",
                    required = true,
                    example = "MONTHLY",
                    schema = @Schema(implementation = SubscriptionPeriod.class)
            )
            @PathVariable SubscriptionPeriod period) {
        SubscriptionPlanResponse response = subscriptionPlanService.getSubscriptionPlanByPeriod(period);
        return ResponseEntity.ok(ResponseDto.ok("Obuna rejasi ma'lumotlari", response));
    }

    @PutMapping("/{id}")
    @Operation(
            summary = "Obuna rejasini yangilash",
            description = "Mavjud obuna rejasining barcha parametrlarini yangilash. " +
                    "Narxni admin panel orqali o'zgartirish mumkin. " +
                    "Davr o'zgartirilsa, yangi davr uchun faol reja mavjudligi tekshiriladi."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Obuna rejasi muvaffaqiyatli yangilandi",
                    content = @Content(schema = @Schema(implementation = ResponseDto.class))
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Yaroqsiz so'rov yoki validatsiya xatosi. " +
                            "Davr o'zgartirilsa, yangi davr uchun faol reja allaqachon mavjud bo'lishi mumkin.",
                    content = @Content(schema = @Schema(implementation = ResponseDto.class))
            ),
            @ApiResponse(
                    responseCode = "403",
                    description = "Faqat ADMIN rolidagi foydalanuvchilar rejani yangilashi mumkin",
                    content = @Content(schema = @Schema(implementation = ResponseDto.class))
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Obuna rejasi topilmadi",
                    content = @Content(schema = @Schema(implementation = ResponseDto.class))
            )
    })
    public ResponseEntity<ResponseDto<SubscriptionPlanResponse>> updateSubscriptionPlan(
            @Parameter(
                    description = "Yangilanadigan obuna rejasi UUID",
                    required = true,
                    example = "123e4567-e89b-12d3-a456-426614174000"
            )
            @PathVariable UUID id,
            @Parameter(
                    description = "Yangilangan obuna rejasi parametrlari",
                    required = true,
                    schema = @Schema(implementation = CreateSubscriptionPlanRequest.class)
            )
            @Valid @RequestBody CreateSubscriptionPlanRequest request) {
        SubscriptionPlanResponse response = subscriptionPlanService.updateSubscriptionPlan(id, request);
        return ResponseEntity.ok(ResponseDto.ok("Obuna rejasi yangilandi", response));
    }

    @DeleteMapping("/{id}")
    @Operation(
            summary = "Obuna rejasini o'chirish",
            description = "Obuna rejasini tizimdan butunlay o'chirish. " +
                    "E'tibor bering: faol obunalarga ulangan rejani o'chirish mumkin emas."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Obuna rejasi muvaffaqiyatli o'chirildi",
                    content = @Content(schema = @Schema(implementation = ResponseDto.class))
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Obuna rejasi o'chirilishi mumkin emas (faol obunalar mavjud)",
                    content = @Content(schema = @Schema(implementation = ResponseDto.class))
            ),
            @ApiResponse(
                    responseCode = "403",
                    description = "Faqat ADMIN rolidagi foydalanuvchilar rejani o'chirishi mumkin",
                    content = @Content(schema = @Schema(implementation = ResponseDto.class))
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Obuna rejasi topilmadi",
                    content = @Content(schema = @Schema(implementation = ResponseDto.class))
            )
    })
    public ResponseEntity<ResponseDto<Void>> deleteSubscriptionPlan(
            @Parameter(
                    description = "O'chiriladigan obuna rejasi UUID",
                    required = true,
                    example = "123e4567-e89b-12d3-a456-426614174000"
            )
            @PathVariable UUID id) {
        subscriptionPlanService.deleteSubscriptionPlan(id);
        return ResponseEntity.ok(ResponseDto.ok("Obuna rejasi o'chirildi"));
    }

    @PatchMapping("/{id}/status")
    @Operation(
            summary = "Obuna rejasi holatini o'zgartirish",
            description = "Obuna rejasini faollashtirish yoki nofaollashtirish. " +
                    "active=true bo'lsa faollashtiriladi, active=false bo'lsa nofaollashtiriladi."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Obuna rejasi holati muvaffaqiyatli o'zgartirildi",
                    content = @Content(schema = @Schema(implementation = ResponseDto.class))
            ),
            @ApiResponse(
                    responseCode = "403",
                    description = "Faqat ADMIN rolidagi foydalanuvchilar reja holatini o'zgartirishi mumkin",
                    content = @Content(schema = @Schema(implementation = ResponseDto.class))
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Obuna rejasi topilmadi",
                    content = @Content(schema = @Schema(implementation = ResponseDto.class))
            )
    })
    public ResponseEntity<ResponseDto<SubscriptionPlanResponse>> toggleSubscriptionPlanStatus(
            @Parameter(
                    description = "Holati o'zgartiriladigan obuna rejasi UUID",
                    required = true,
                    example = "123e4567-e89b-12d3-a456-426614174000"
            )
            @PathVariable UUID id,
            @Parameter(
                    description = "Yangi holat (true=faol, false=nofaol)",
                    required = true,
                    example = "true"
            )
            @RequestParam boolean active) {
        SubscriptionPlanResponse response = subscriptionPlanService.toggleSubscriptionPlanStatus(id, active);
        String message = active ? "Obuna rejasi faollashtirildi" : "Obuna rejasi nofaollashtirildi";
        return ResponseEntity.ok(ResponseDto.ok(message, response));
    }
}