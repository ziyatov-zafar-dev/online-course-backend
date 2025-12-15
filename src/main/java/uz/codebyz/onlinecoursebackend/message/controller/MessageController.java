package uz.codebyz.onlinecoursebackend.message.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.web.bind.annotation.*;
import uz.codebyz.onlinecoursebackend.common.ResponseDto;
import uz.codebyz.onlinecoursebackend.message.dto.request.*;
import uz.codebyz.onlinecoursebackend.message.dto.response.MessageResponseDto;
import uz.codebyz.onlinecoursebackend.message.service.MessageService;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/messages")
public class MessageController {

    private final MessageService messageService;

    public MessageController(MessageService messageService) {
        this.messageService = messageService;
    }

    /* =========================================================
       SEND MESSAGE
       ========================================================= */

    @Operation(
            summary = "Chatga oddiy matnli xabar yuborish",
            description = """
                    Ushbu endpoint orqali foydalanuvchi chatga **TEXT turidagi xabar** yuboradi.
                    
                    📌 Qoidalar:
                    - Foydalanuvchi chat a’zosi bo‘lishi shart
                    - PRIVATE chatda bloklangan bo‘lsa xabar yuborilmaydi
                    - Xabar uzunligi 5000 belgidan oshmasligi kerak
                    - Fayl yuborish bu endpoint orqali **mumkin emas**
                    
                    🔒 Autentifikatsiya:
                    - `X-USER-ID` header orqali user aniqlanadi
                    
                    📨 Qo‘llanilishi:
                    - Oddiy chat xabarlari
                    - Reply xabarlar
                    - Matnli suhbatlar
                    """
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Xabar muvaffaqiyatli yuborildi",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = MessageResponseDto.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Noto‘g‘ri so‘rov (validation xatosi yoki noto‘g‘ri ma’lumot)"
            ),
            @ApiResponse(
                    responseCode = "403",
                    description = "Foydalanuvchi chat a’zosi emas yoki bloklangan"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Chat topilmadi"
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Server ichki xatosi"
            )
    })
    @PostMapping("/chat/{chatId}")
    public ResponseDto<MessageResponseDto> sendMessage(

            @Parameter(
                    description = "Xabar yuboriladigan chat ID",
                    required = true,
                    example = "c1a2b3d4-e5f6-7890-a123-b4567890abcd"
            )
            @PathVariable("chatId") UUID chatId,

            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Yuboriladigan xabar ma’lumotlari",
                    required = true,
                    content = @Content(
                            schema = @Schema(implementation = MessageCreateDto.class)
                    )
            )
            @RequestBody MessageCreateDto dto,

            @Parameter(
                    description = "Xabar yuborayotgan foydalanuvchi ID (auth header)",
                    required = true,
                    example = "11111111-2222-3333-4444-555555555555"
            )
            @RequestHeader("X-USER-ID") UUID userId
    ) {
        return messageService.sendMessage(chatId, dto, userId);
    }


    @Operation(
            summary = "Chatga fayl bilan xabar yuborish",
            description = """
                    Ushbu endpoint orqali foydalanuvchi chatga **fayl biriktirilgan xabar**
                    (IMAGE / VIDEO / FILE) yuboradi.
                    
                    📌 Muhim qoidalar:
                    - Bu endpoint **TEXT xabarlar uchun emas**
                    - `messageType` faqat IMAGE, VIDEO yoki FILE bo‘lishi mumkin
                    - Fayl frontend tomonidan oldindan upload qilingan bo‘lishi kerak
                    - `fileUrl` majburiy
                    - PRIVATE chatda bloklangan foydalanuvchi xabar yubora olmaydi
                    - SYSTEM message yuborish bu endpoint orqali **mumkin emas**
                    
                    🔒 Autentifikatsiya:
                    - `X-USER-ID` header orqali foydalanuvchi aniqlanadi
                    
                    📨 Qo‘llanilishi:
                    - Rasm yuborish
                    - Video yuborish
                    - Hujjat (PDF, ZIP, DOCX va boshqalar) yuborish
                    """
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Fayl bilan xabar muvaffaqiyatli yuborildi",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = MessageResponseDto.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Noto‘g‘ri so‘rov (messageType yoki fileUrl xato)"
            ),
            @ApiResponse(
                    responseCode = "403",
                    description = "Foydalanuvchi chat a’zosi emas yoki bloklangan"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Chat topilmadi"
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Server ichki xatosi"
            )
    })
    @PostMapping("/chat/{chatId}/file")
    public ResponseDto<MessageResponseDto> sendMessageWithFile(

            @Parameter(
                    description = "Xabar yuboriladigan chat ID",
                    required = true,
                    example = "c1a2b3d4-e5f6-7890-a123-b4567890abcd"
            )
            @PathVariable("chatId") UUID chatId,

            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Fayl bilan yuboriladigan xabar ma’lumotlari",
                    required = true,
                    content = @Content(
                            schema = @Schema(implementation = MessageFileCreateDto.class)
                    )
            )
            @RequestBody MessageFileCreateDto dto,

            @Parameter(
                    description = "Xabar yuborayotgan foydalanuvchi ID (auth header)",
                    required = true,
                    example = "11111111-2222-3333-4444-555555555555"
            )
            @RequestHeader("X-USER-ID") UUID userId
    ) {
        return messageService.sendMessageWithFile(chatId, dto, userId);
    }


    @Operation(
            summary = "Xabarga javob (reply) yuborish",
            description = """
                    Ushbu endpoint orqali foydalanuvchi chatdagi mavjud xabarga
                    **javob (reply)** yuboradi.
                    
                    📌 Qoidalar:
                    - Reply qilinayotgan xabar **shu chatga tegishli bo‘lishi shart**
                    - Foydalanuvchi chat a’zosi bo‘lishi shart
                    - PRIVATE chatda bloklangan bo‘lsa reply yuborib bo‘lmaydi
                    - Reply faqat **TEXT xabar** orqali yuboriladi
                    - Reply xabar SYSTEM xabar bo‘lishi mumkin emas
                    
                    🔒 Autentifikatsiya:
                    - `X-USER-ID` header orqali foydalanuvchi aniqlanadi
                    
                    📨 Qo‘llanilishi:
                    - Xabarga javob berish
                    - Muayyan xabarga izoh yozish
                    """
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Reply xabar muvaffaqiyatli yuborildi",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = MessageResponseDto.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Noto‘g‘ri so‘rov (reply xabar yoki DTO noto‘g‘ri)"
            ),
            @ApiResponse(
                    responseCode = "403",
                    description = "Foydalanuvchi chat a’zosi emas yoki bloklangan"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Chat yoki reply qilinayotgan xabar topilmadi"
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Server ichki xatosi"
            )
    })
    @PostMapping("/chat/{chatId}/reply/{replyToMessageId}")
    public ResponseDto<MessageResponseDto> replyMessage(

            @Parameter(
                    description = "Reply yuboriladigan chat ID",
                    required = true,
                    example = "c1a2b3d4-e5f6-7890-a123-b4567890abcd"
            )
            @PathVariable("chatId") UUID chatId,

            @Parameter(
                    description = "Reply qilinayotgan xabar ID",
                    required = true,
                    example = "aa11bb22-cc33-dd44-ee55-ff6677889900"
            )
            @PathVariable("replyToMessageId") UUID replyToMessageId,

            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Reply sifatida yuboriladigan xabar ma’lumotlari",
                    required = true,
                    content = @Content(
                            schema = @Schema(implementation = MessageCreateDto.class)
                    )
            )
            @RequestBody MessageCreateDto dto,

            @Parameter(
                    description = "Reply yuborayotgan foydalanuvchi ID (auth header)",
                    required = true,
                    example = "11111111-2222-3333-4444-555555555555"
            )
            @RequestHeader("X-USER-ID") UUID userId
    ) {
        return messageService.replyMessage(chatId, replyToMessageId, dto, userId);
    }

    /* =========================================================
       GET MESSAGES
       ========================================================= */

    @Operation(
            summary = "Chat xabarlarini olish (pagination bilan)",
            description = """
                    Ushbu endpoint orqali foydalanuvchi tanlangan chatdagi xabarlarni
                    **sahifalab (pagination)** oladi.
                    
                    📌 Qoidalar:
                    - Foydalanuvchi chat a’zosi bo‘lishi shart
                    - PRIVATE chatda bloklangan bo‘lsa xabarlar ko‘rinmasligi mumkin
                    - `page` 0 dan boshlanadi
                    - `size` sahifadagi maksimal xabarlar soni
                    - Xabarlar **oxirgi yuborilgan vaqt bo‘yicha tartiblangan**
                      (odatda eng yangi xabarlar yuqorida yoki pastda — frontendga bog‘liq)
                    
                    🔒 Autentifikatsiya:
                    - `X-USER-ID` header orqali foydalanuvchi aniqlanadi
                    
                    📨 Qo‘llanilishi:
                    - Chat ochilganda xabarlarni yuklash
                    - Scroll qilganda eski xabarlarni yuklash (infinite scroll)
                    """
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Chat xabarlari muvaffaqiyatli olindi",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(
                                    implementation = MessageResponseDto.class
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Noto‘g‘ri pagination parametrlari (page yoki size)"
            ),
            @ApiResponse(
                    responseCode = "403",
                    description = "Foydalanuvchi chat a’zosi emas yoki ruxsat yo‘q"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Chat topilmadi"
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Server ichki xatosi"
            )
    })
    @GetMapping("/chat/{chatId}")
    public ResponseDto<?> getMessagesByChat(

            @Parameter(
                    description = "Xabarlar olinadigan chat ID",
                    required = true,
                    example = "c1a2b3d4-e5f6-7890-a123-b4567890abcd"
            )
            @PathVariable("chatId") UUID chatId,

            @Parameter(
                    description = "Sahifa raqami (0 dan boshlanadi)",
                    required = true,
                    example = "0"
            )
            @RequestParam("page") int page,

            @Parameter(
                    description = "Sahifadagi xabarlar soni",
                    required = true,
                    example = "20"
            )
            @RequestParam("size") int size,

            @Parameter(
                    description = "Xabarlarni ko‘rayotgan foydalanuvchi ID (auth header)",
                    required = true,
                    example = "11111111-2222-3333-4444-555555555555"
            )
            @RequestHeader("X-USER-ID") UUID userId
    ) {
        return messageService.getMessagesByChat(chatId, page, size, userId);
    }

    @Operation(
            summary = "Xabarni ID orqali olish",
            description = """
                    Ushbu endpoint orqali foydalanuvchi **aniq bitta xabarni**
                    uning ID si orqali olishi mumkin.
                    
                    📌 Qoidalar:
                    - Foydalanuvchi xabar tegishli bo‘lgan chat a’zosi bo‘lishi shart
                    - SYSTEM xabarlar ham qaytarilishi mumkin
                    - Agar xabar o‘chirilgan bo‘lsa, ruxsat etilmaydi
                    - PRIVATE chatda bloklangan foydalanuvchi xabarni ko‘ra olmaydi
                    
                    🔒 Autentifikatsiya:
                    - `X-USER-ID` header orqali foydalanuvchi aniqlanadi
                    
                    📨 Qo‘llanilishi:
                    - Reply bosilganda original xabarni ko‘rsatish
                    - Notification orqali xabarni ochish
                    - Xabarni alohida sahifada ko‘rish
                    """
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Xabar muvaffaqiyatli topildi",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = MessageResponseDto.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "403",
                    description = "Foydalanuvchi ushbu xabarni ko‘rish huquqiga ega emas"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Xabar topilmadi"
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Server ichki xatosi"
            )
    })
    @GetMapping("/{messageId}")
    public ResponseDto<MessageResponseDto> getMessageById(

            @Parameter(
                    description = "Olinadigan xabar ID",
                    required = true,
                    example = "aa11bb22-cc33-dd44-ee55-ff6677889900"
            )
            @PathVariable("messageId") UUID messageId,

            @Parameter(
                    description = "Xabarni ko‘rayotgan foydalanuvchi ID (auth header)",
                    required = true,
                    example = "11111111-2222-3333-4444-555555555555"
            )
            @RequestHeader("X-USER-ID") UUID userId
    ) {
        return messageService.getMessageById(messageId, userId);
    }

    @Operation(
            summary = "Chatdagi oxirgi xabarni olish",
            description = """
                    Ushbu endpoint orqali foydalanuvchi tanlangan chatdagi
                    **eng oxirgi yuborilgan xabarni** oladi.
                    
                    📌 Qoidalar:
                    - Foydalanuvchi chat a’zosi bo‘lishi shart
                    - Agar chatda hali xabar bo‘lmasa `null` qaytishi mumkin
                    - SYSTEM xabarlar ham oxirgi xabar sifatida qaytishi mumkin
                    - PRIVATE chatda bloklangan foydalanuvchi xabarni ko‘ra olmaydi
                    
                    🔒 Autentifikatsiya:
                    - `X-USER-ID` header orqali foydalanuvchi aniqlanadi
                    
                    📨 Qo‘llanilishi:
                    - Chat listda oxirgi xabar preview
                    - Chatlar ro‘yxatini sort qilish
                    - Realtime (WebSocket) yangilanishlardan keyin UI update
                    """
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Chatdagi oxirgi xabar muvaffaqiyatli olindi",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = MessageResponseDto.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "403",
                    description = "Foydalanuvchi chat a’zosi emas yoki ruxsat yo‘q"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Chat topilmadi"
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Server ichki xatosi"
            )
    })
    @GetMapping("/chat/{chatId}/last")
    public ResponseDto<MessageResponseDto> getLastMessage(

            @Parameter(
                    description = "Oxirgi xabar olinadigan chat ID",
                    required = true,
                    example = "c1a2b3d4-e5f6-7890-a123-b4567890abcd"
            )
            @PathVariable("chatId") UUID chatId,

            @Parameter(
                    description = "Xabarni ko‘rayotgan foydalanuvchi ID (auth header)",
                    required = true,
                    example = "11111111-2222-3333-4444-555555555555"
            )
            @RequestHeader("X-USER-ID") UUID userId
    ) {
        return messageService.getLastMessage(chatId, userId);
    }


    /* =========================================================
       READ STATUS
       ========================================================= */

    @Operation(
            summary = "Chatdagi barcha xabarlarni o‘qilgan deb belgilash",
            description = """
                    Ushbu endpoint orqali foydalanuvchi tanlangan chatdagi
                    **o‘zi uchun barcha xabarlarni o‘qilgan (READ)** holatiga o‘tkazadi.
                    
                    📌 Qoidalar:
                    - Foydalanuvchi chat a’zosi bo‘lishi shart
                    - Faqat **foydalanuvchiga tegishli unread xabarlar** o‘qilgan qilinadi
                    - Boshqa userlar uchun read status o‘zgarmaydi
                    - SYSTEM xabarlar unread countga ta’sir qilmaydi
                    
                    🔒 Autentifikatsiya:
                    - `X-USER-ID` header orqali foydalanuvchi aniqlanadi
                    
                    📨 Qo‘llanilishi:
                    - Chat ochilganda avtomatik chaqiriladi
                    - “Mark as read” tugmasi bosilganda
                    - Unread badge’ni tozalash uchun
                    """
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Chatdagi barcha xabarlar o‘qilgan deb belgilandi"
            ),
            @ApiResponse(
                    responseCode = "403",
                    description = "Foydalanuvchi chat a’zosi emas yoki ruxsat yo‘q"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Chat topilmadi"
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Server ichki xatosi"
            )
    })
    @PostMapping("/chat/{chatId}/read")
    public ResponseDto<Void> markChatAsRead(

            @Parameter(
                    description = "O‘qilgan deb belgilinadigan chat ID",
                    required = true,
                    example = "c1a2b3d4-e5f6-7890-a123-b4567890abcd"
            )
            @PathVariable("chatId") UUID chatId,

            @Parameter(
                    description = "Chatni o‘qiyotgan foydalanuvchi ID (auth header)",
                    required = true,
                    example = "11111111-2222-3333-4444-555555555555"
            )
            @RequestHeader("X-USER-ID") UUID userId
    ) {
        return messageService.markChatAsRead(chatId, userId);
    }


    @Operation(
            summary = "Bitta xabarni o‘qilgan deb belgilash",
            description = """
                    Ushbu endpoint orqali foydalanuvchi **aniq bitta xabarni**
                    o‘zi uchun **o‘qilgan (READ)** holatiga o‘tkazadi.
                    
                    📌 Qoidalar:
                    - Foydalanuvchi xabar tegishli bo‘lgan chat a’zosi bo‘lishi shart
                    - Xabar boshqa foydalanuvchi tomonidan yuborilgan bo‘lishi mumkin
                    - SYSTEM xabarlar o‘qilgan/o‘qilmagan hisobiga ta’sir qilmaydi
                    - Agar xabar allaqachon o‘qilgan bo‘lsa, qayta belgilash xato bermaydi
                    
                    🔒 Autentifikatsiya:
                    - `X-USER-ID` header orqali foydalanuvchi aniqlanadi
                    
                    📨 Qo‘llanilishi:
                    - Notification ustiga bosilganda
                    - Chat ichida bitta xabarni ko‘rganda
                    - Deep-link orqali xabar ochilganda
                    """
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Xabar o‘qilgan deb belgilandi"
            ),
            @ApiResponse(
                    responseCode = "403",
                    description = "Foydalanuvchi ushbu xabarni o‘qish huquqiga ega emas"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Xabar topilmadi"
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Server ichki xatosi"
            )
    })
    @PostMapping("/{messageId}/read")
    public ResponseDto<Void> markMessageAsRead(

            @Parameter(
                    description = "O‘qilgan deb belgilinadigan xabar ID",
                    required = true,
                    example = "aa11bb22-cc33-dd44-ee55-ff6677889900"
            )
            @PathVariable("messageId") UUID messageId,

            @Parameter(
                    description = "Xabarni o‘qiyotgan foydalanuvchi ID (auth header)",
                    required = true,
                    example = "11111111-2222-3333-4444-555555555555"
            )
            @RequestHeader("X-USER-ID") UUID userId
    ) {
        return messageService.markMessageAsRead(messageId, userId);
    }


    @Operation(
            summary = "Chatdagi o‘qilmagan xabarlar sonini olish",
            description = """
                    Ushbu endpoint orqali foydalanuvchi tanlangan chatdagi
                    **o‘qilmagan (UNREAD) xabarlar sonini** oladi.
                    
                    📌 Qoidalar:
                    - Foydalanuvchi chat a’zosi bo‘lishi shart
                    - Faqat **foydalanuvchiga tegishli unread xabarlar** hisoblanadi
                    - O‘zi yuborgan xabarlar unread hisoblanmaydi
                    - SYSTEM xabarlar unread countga kirmaydi
                    - Agar chatda xabar bo‘lmasa, `0` qaytadi
                    
                    🔒 Autentifikatsiya:
                    - `X-USER-ID` header orqali foydalanuvchi aniqlanadi
                    
                    📨 Qo‘llanilishi:
                    - Chat listda unread badge ko‘rsatish
                    - Mobil/web ilovalarda real-time hisoblash
                    - Notification logikasida
                    """
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "O‘qilmagan xabarlar soni muvaffaqiyatli olindi",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = Long.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "403",
                    description = "Foydalanuvchi chat a’zosi emas yoki ruxsat yo‘q"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Chat topilmadi"
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Server ichki xatosi"
            )
    })
    @GetMapping("/chat/{chatId}/unread-count")
    public ResponseDto<Long> getUnreadCount(

            @Parameter(
                    description = "Unread xabarlar soni olinadigan chat ID",
                    required = true,
                    example = "c1a2b3d4-e5f6-7890-a123-b4567890abcd"
            )
            @PathVariable("chatId") UUID chatId,

            @Parameter(
                    description = "Unread xabarlarni ko‘rayotgan foydalanuvchi ID (auth header)",
                    required = true,
                    example = "11111111-2222-3333-4444-555555555555"
            )
            @RequestHeader("X-USER-ID") UUID userId
    ) {
        return messageService.getUnreadCount(chatId, userId);
    }


    /* =========================================================
       EDIT / DELETE
       ========================================================= */

    @Operation(
            summary = "Yuborilgan xabarni tahrirlash",
            description = """
                    Ushbu endpoint orqali foydalanuvchi **o‘zi yuborgan xabarni**
                    tahrirlashi (edit) mumkin.
                    
                    📌 Qoidalar:
                    - Faqat **o‘zining yuborgan xabari** tahrirlanadi
                    - SYSTEM xabarlar tahrirlanmaydi
                    - O‘chirilgan (deleted) xabarlar tahrirlanmaydi
                    - Faqat TEXT xabarlar tahrirlanadi
                    - Xabar tahrirlangandan so‘ng `edited=true` bo‘ladi
                    
                    🔒 Autentifikatsiya:
                    - `X-USER-ID` header orqali foydalanuvchi aniqlanadi
                    
                    📨 Qo‘llanilishi:
                    - Xatolikni tuzatish
                    - Matnni yangilash
                    - Telegram / WhatsApp’dagi “Edit message” funksiyasi
                    """
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Xabar muvaffaqiyatli tahrirlandi",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = MessageResponseDto.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Noto‘g‘ri so‘rov (DTO yoki content xato)"
            ),
            @ApiResponse(
                    responseCode = "403",
                    description = "Foydalanuvchi bu xabarni tahrirlash huquqiga ega emas"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Xabar topilmadi"
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Server ichki xatosi"
            )
    })
    @PutMapping("/{messageId}")
    public ResponseDto<MessageResponseDto> editMessage(

            @Parameter(
                    description = "Tahrirlanadigan xabar ID",
                    required = true,
                    example = "aa11bb22-cc33-dd44-ee55-ff6677889900"
            )
            @PathVariable("messageId") UUID messageId,

            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Xabarni tahrirlash uchun yangi matn",
                    required = true,
                    content = @Content(
                            schema = @Schema(implementation = MessageEditDto.class)
                    )
            )
            @RequestBody MessageEditDto dto,

            @Parameter(
                    description = "Xabarni tahrirlayotgan foydalanuvchi ID (auth header)",
                    required = true,
                    example = "11111111-2222-3333-4444-555555555555"
            )
            @RequestHeader("X-USER-ID") UUID userId
    ) {
        return messageService.editMessage(messageId, dto, userId);
    }


    @Operation(
            summary = "Xabarni o‘chirish",
            description = """
                    Ushbu endpoint orqali foydalanuvchi **o‘zi yuborgan xabarni**
                    o‘chirishi mumkin.
                    
                    📌 Qoidalar:
                    - Faqat **o‘zining yuborgan xabari** o‘chiriladi
                    - SYSTEM xabarlar o‘chirilmaydi
                    - O‘chirilgan xabarlar qayta o‘chirilmaydi
                    - Xabar **soft delete** qilinadi (DB’dan o‘chmaydi)
                    - O‘chirilgan xabar o‘rnida frontendda
                      "Xabar o‘chirildi" degan yozuv ko‘rsatiladi
                    
                    🔒 Autentifikatsiya:
                    - `X-USER-ID` header orqali foydalanuvchi aniqlanadi
                    
                    📨 Qo‘llanilishi:
                    - Xato yuborilgan xabarni olib tashlash
                    - Chatni tozalash
                    - Telegram / WhatsApp’dagi “Delete message” funksiyasi
                    """
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Xabar muvaffaqiyatli o‘chirildi"
            ),
            @ApiResponse(
                    responseCode = "403",
                    description = "Foydalanuvchi bu xabarni o‘chirish huquqiga ega emas"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Xabar topilmadi"
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Server ichki xatosi"
            )
    })
    @DeleteMapping("delete/{messageId}")
    public ResponseDto<Void> deleteMessage(

            @Parameter(
                    description = "O‘chiriladigan xabar ID",
                    required = true,
                    example = "aa11bb22-cc33-dd44-ee55-ff6677889900"
            )
            @PathVariable("messageId") UUID messageId,

            @Parameter(
                    description = "Xabarni o‘chirayotgan foydalanuvchi ID (auth header)",
                    required = true,
                    example = "11111111-2222-3333-4444-555555555555"
            )
            @RequestHeader("X-USER-ID") UUID userId
    ) {
        return messageService.deleteMessage(messageId, userId);
    }


    /* =========================================================
       SYSTEM MESSAGE (INTERNAL)
       ========================================================= */

    @Operation(
            summary = "Chatga tizim (SYSTEM) xabar yuborish",
            description = """
                    Ushbu endpoint orqali chatga **SYSTEM turidagi xabar**
                    yuboriladi. Bu xabarlar **foydalanuvchi tomonidan emas**,
                    balki tizim tomonidan yaratiladi.
                    
                    📌 Qoidalar:
                    - Xabar turi avtomatik ravishda `SYSTEM` bo‘ladi
                    - `sender` bo‘lmaydi (null)
                    - Xabarlar o‘qilmagan/o‘qilgan hisobiga kirmaydi
                    - Tahrirlab bo‘lmaydi
                    - O‘chirib bo‘lmaydi
                    - Frontendda odatda markazda yoki kulrang ko‘rinishda chiqadi
                    
                    🔒 Xavfsizlik:
                    - Ushbu endpoint **faqat admin yoki internal servislar**
                      tomonidan chaqirilishi kerak
                    - Odatda frontendga ochib qo‘yilmaydi
                    
                    📨 Qo‘llanilishi:
                    - Foydalanuvchi chatga qo‘shildi / chiqdi
                    - Guruh yaratildi
                    - Admin tayinlandi
                    - Foydalanuvchi bloklandi
                    """
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "System xabar muvaffaqiyatli yuborildi"
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Xabar matni bo‘sh yoki noto‘g‘ri"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Chat topilmadi"
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Server ichki xatosi"
            )
    })
    @PostMapping("/chat/{chatId}/system")
    public ResponseDto<Void> sendSystemMessage(

            @Parameter(
                    description = "System xabar yuboriladigan chat ID",
                    required = true,
                    example = "c1a2b3d4-e5f6-7890-a123-b4567890abcd"
            )
            @PathVariable("chatId") UUID chatId,

            @Parameter(
                    description = "Yuboriladigan system xabar matni",
                    required = true,
                    example = "👤 Foydalanuvchi chatga qo‘shildi"
            )
            @RequestParam("content") String content
    ) {
        return messageService.sendSystemMessage(chatId, content);
    }

}
