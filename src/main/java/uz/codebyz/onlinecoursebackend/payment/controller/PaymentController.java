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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uz.codebyz.onlinecoursebackend.common.ResponseDto;
import uz.codebyz.onlinecoursebackend.payment.dto.request.PaymentRequest;
import uz.codebyz.onlinecoursebackend.payment.dto.response.PaymentResponse;
import uz.codebyz.onlinecoursebackend.payment.entity.PaymentStatus;
import uz.codebyz.onlinecoursebackend.payment.service.PaymentService;

import java.util.UUID;

@RestController
@RequestMapping("/api/payments")
@Tag(name = "Payment Management", description = "Umumiy to'lov operatsiyalari bilan ishlash uchun API endpoints. " +
        "Barcha to'lov providerlari (Payme, Click, Uzum, Paylov, Hamkor, PayPal) uchun umumiy interfeys.")
public class PaymentController {

    @Autowired
    private PaymentService paymentService;

    @PostMapping
    @Operation(
            summary = "Yangi to'lov yaratish",
            description = "Yangi to'lov transaktsiyasini yaratadi. " +
                    "To'lov dastlab PENDING holatida yaratiladi va tegishli payment provider orqali amalga oshirilishi kerak. " +
                    "To'lov o'qituvchi obunasi yoki kurs to'lovi uchun bo'lishi mumkin."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "To'lov transaktsiyasi muvaffaqiyatli yaratildi",
                    content = @Content(schema = @Schema(implementation = ResponseDto.class))
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Yaroqsiz so'rov, validatsiya xatosi yoki provider parametrlari noto'g'ri",
                    content = @Content(schema = @Schema(implementation = ResponseDto.class))
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "O'qituvchi obunasi yoki kurs to'lovi topilmadi",
                    content = @Content(schema = @Schema(implementation = ResponseDto.class))
            ),
            @ApiResponse(
                    responseCode = "409",
                    description = "Transaktsiya ID bilan to'lov allaqachon mavjud",
                    content = @Content(schema = @Schema(implementation = ResponseDto.class))
            )
    })
    public ResponseEntity<ResponseDto<PaymentResponse>> createPayment(
            @Parameter(
                    description = "To'lov ma'lumotlari. O'qituvchi obunasi ID yoki kurs to'lovi ID dan birini ko'rsatish kerak.",
                    required = true,
                    schema = @Schema(implementation = PaymentRequest.class)
            )
            @Valid @RequestBody PaymentRequest request) {
        PaymentResponse response = paymentService.createPayment(request);
        return ResponseEntity.ok(ResponseDto.ok("To'lov yaratildi", response));
    }

    @GetMapping("/{id}")
    @Operation(
            summary = "To'lov ma'lumotlarini ID bo'yicha olish",
            description = "To'lovning UUID orqali batafsil ma'lumotlarini olish. " +
                    "To'lov provider responsi, transaktsiya holati va bog'langan obuna/kurs ma'lumotlarini o'z ichiga oladi."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "To'lov ma'lumotlari topildi",
                    content = @Content(schema = @Schema(implementation = ResponseDto.class))
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Berilgan ID bilan to'lov topilmadi",
                    content = @Content(schema = @Schema(implementation = ResponseDto.class))
            )
    })
    public ResponseEntity<ResponseDto<PaymentResponse>> getPayment(
            @Parameter(
                    description = "To'lovning UUID identifikatori",
                    required = true,
                    example = "123e4567-e89b-12d3-a456-426614174000"
            )
            @PathVariable UUID id) {
        PaymentResponse response = paymentService.getPaymentById(id);
        return ResponseEntity.ok(ResponseDto.ok("To'lov ma'lumotlari", response));
    }

    @GetMapping("/transaction/{transactionId}")
    @Operation(
            summary = "To'lov ma'lumotlarini transaktsiya ID bo'yicha olish",
            description = "Tizim tomonidan generatsiya qilingan transaktsiya ID orqali to'lov ma'lumotlarini olish. " +
                    "Bu ID to'lov yaratilganda avtomatik generatsiya qilinadi (format: TXN-123456789)."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "To'lov transaktsiyasi topildi",
                    content = @Content(schema = @Schema(implementation = ResponseDto.class))
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Berilgan transaktsiya ID bilan to'lov topilmadi",
                    content = @Content(schema = @Schema(implementation = ResponseDto.class))
            )
    })
    public ResponseEntity<ResponseDto<PaymentResponse>> getPaymentByTransactionId(
            @Parameter(
                    description = "Tizim tomonidan generatsiya qilingan transaktsiya ID",
                    required = true,
                    example = "TXN-1689324567890"
            )
            @PathVariable String transactionId) {
        PaymentResponse response = paymentService.getPaymentByTransactionId(transactionId);
        return ResponseEntity.ok(ResponseDto.ok("To'lov ma'lumotlari", response));
    }

    @GetMapping
    @Operation(
            summary = "Barcha to'lovlarni sahifalangan holda olish",
            description = "Barcha to'lov transaktsiyalarini sahifalangan holda olish. " +
                    "Pageable parametrlari orqali sahifalash, tartiblash va filtratsiya mumkin."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "To'lovlar ro'yxati muvaffaqiyatli qaytarildi",
                    content = @Content(schema = @Schema(implementation = ResponseDto.class))
            )
    })
    public ResponseEntity<ResponseDto<Page<PaymentResponse>>> getAllPayments(
            @Parameter(
                    description = "Sahifalash parametrlari: page, size, sort. " +
                            "Misol: ?page=0&size=20&sort=createdAt,desc",
                    required = false,
                    schema = @Schema(implementation = Pageable.class)
            )
            Pageable pageable) {
        Page<PaymentResponse> payments = paymentService.getAllPayments(pageable);
        return ResponseEntity.ok(ResponseDto.ok("To'lovlar ro'yxati", payments));
    }

    @GetMapping("/status/{status}")
    @Operation(
            summary = "Holat bo'yicha to'lovlarni filtrlash",
            description = "To'lov holati bo'yicha filtr qo'llab, natijalarni sahifalangan holda olish. " +
                    "Holatlar: PENDING, SUCCESS, FAILED, CANCELLED, REFUNDED."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Filtr qilingan to'lovlar ro'yxati",
                    content = @Content(schema = @Schema(implementation = ResponseDto.class))
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Yaroqsiz holat parametri",
                    content = @Content(schema = @Schema(implementation = ResponseDto.class))
            )
    })
    public ResponseEntity<ResponseDto<Page<PaymentResponse>>> getPaymentsByStatus(
            @Parameter(
                    description = "To'lov holati (PENDING, SUCCESS, FAILED, CANCELLED, REFUNDED)",
                    required = true,
                    example = "SUCCESS"
            )
            @PathVariable PaymentStatus status,
            @Parameter(
                    description = "Sahifalash parametrlari",
                    required = false,
                    schema = @Schema(implementation = Pageable.class)
            )
            Pageable pageable) {
        Page<PaymentResponse> payments = paymentService.getPaymentsByStatus(status, pageable);
        return ResponseEntity.ok(ResponseDto.ok("To'lovlar ro'yxati", payments));
    }

    @PutMapping("/{id}/status")
    @Operation(
            summary = "To'lov holatini yangilash",
            description = "To'lov holatini yangilash (masalan, PENDING → SUCCESS). " +
                    "Payment provider tomonidan qaytarilgan transaktsiya ID ni ham saqlash mumkin. " +
                    "To'lov SUCCESS holatiga o'tganda, bog'langan obuna yoki kurs to'lovi aktivlashtiriladi."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "To'lov holati muvaffaqiyatli yangilandi",
                    content = @Content(schema = @Schema(implementation = ResponseDto.class))
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Yaroqsiz holat o'tish yoki providerTransactionId allaqachon mavjud",
                    content = @Content(schema = @Schema(implementation = ResponseDto.class))
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "To'lov topilmadi",
                    content = @Content(schema = @Schema(implementation = ResponseDto.class))
            )
    })
    public ResponseEntity<ResponseDto<PaymentResponse>> updatePaymentStatus(
            @Parameter(
                    description = "To'lov UUID identifikatori",
                    required = true,
                    example = "123e4567-e89b-12d3-a456-426614174000"
            )
            @PathVariable UUID id,
            @Parameter(
                    description = "Yangi to'lov holati",
                    required = true,
                    example = "SUCCESS"
            )
            @RequestParam PaymentStatus status,
            @Parameter(
                    description = "Payment provider tomonidan qaytarilgan transaktsiya ID (masalan, Payme transaktsiya ID)",
                    required = false,
                    example = "65f7a8b9c1d2e3f4a5b6c7d8"
            )
            @RequestParam(required = false) String providerTransactionId) {
        PaymentResponse response = paymentService.updatePaymentStatus(id, status, providerTransactionId);
        return ResponseEntity.ok(ResponseDto.ok("To'lov holati yangilandi", response));
    }

    @PostMapping("/{id}/refund")
    @Operation(
            summary = "To'lovni qaytarish (refund)",
            description = "Amalga oshirilgan to'lovni qaytarish. " +
                    "Faqat SUCCESS holatidagi to'lovlar qaytarilishi mumkin. " +
                    "To'lov REFUNDED holatiga o'tadi va bog'langan obuna bekor qilinadi yoki kurs to'lovi qaytariladi."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "To'lov muvaffaqiyatli qaytarildi",
                    content = @Content(schema = @Schema(implementation = ResponseDto.class))
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "To'lov qaytarish mumkin emas (holati SUCCESS emas yoki allaqachon qaytarilgan)",
                    content = @Content(schema = @Schema(implementation = ResponseDto.class))
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "To'lov topilmadi",
                    content = @Content(schema = @Schema(implementation = ResponseDto.class))
            )
    })
    public ResponseEntity<ResponseDto<PaymentResponse>> refundPayment(
            @Parameter(
                    description = "Qaytariladigan to'lovning UUID identifikatori",
                    required = true,
                    example = "123e4567-e89b-12d3-a456-426614174000"
            )
            @PathVariable UUID id) {
        PaymentResponse response = paymentService.refundPayment(id);
        return ResponseEntity.ok(ResponseDto.ok("To'lov qaytarildi", response));
    }

    @GetMapping("/verify/{transactionId}")
    @Operation(
            summary = "To'lovni tasdiqlash",
            description = "Transaktsiya ID bo'yicha to'lovning muvaffaqiyatli amalga oshirilganligini tekshirish. " +
                    "Bu metod odatda payment provider callback'larini qayta ishlashda yoki to'lov holatini tekshirishda ishlatiladi."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "To'lov tasdiqlandi yoki tasdiqlanmadi",
                    content = @Content(schema = @Schema(implementation = ResponseDto.class))
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Transaktsiya ID bilan to'lov topilmadi",
                    content = @Content(schema = @Schema(implementation = ResponseDto.class))
            )
    })
    public ResponseEntity<ResponseDto<Boolean>> verifyPayment(
            @Parameter(
                    description = "Tekshiriladigan transaktsiya ID",
                    required = true,
                    example = "TXN-1689324567890"
            )
            @PathVariable String transactionId) {
        boolean verified = paymentService.verifyPayment(transactionId);
        if (verified) {
            return ResponseEntity.ok(ResponseDto.ok("To'lov tasdiqlandi", true));
        } else {
            return ResponseEntity.ok(ResponseDto.error("To'lov tasdiqlanmadi", false));
        }
    }
}