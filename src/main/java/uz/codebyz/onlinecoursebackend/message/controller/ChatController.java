package uz.codebyz.onlinecoursebackend.message.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.web.bind.annotation.*;
import uz.codebyz.onlinecoursebackend.common.ResponseDto;
import uz.codebyz.onlinecoursebackend.message.dto.request.ChatCreateDto;
import uz.codebyz.onlinecoursebackend.message.dto.response.ChatMemberResponseDto;
import uz.codebyz.onlinecoursebackend.message.dto.response.ChatResponseDto;
import uz.codebyz.onlinecoursebackend.message.entity.Chat;
import uz.codebyz.onlinecoursebackend.message.service.ChatService;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/chats")
public class ChatController {

    private final ChatService chatService;

    public ChatController(ChatService chatService) {
        this.chatService = chatService;
    }

    /* =========================================================
       CREATE PRIVATE CHAT
       ========================================================= */

    @Operation(
            summary = "Private (1–1) chat yaratish yoki mavjudini olish",
            description = """
                    Ushbu endpoint orqali foydalanuvchi boshqa bir foydalanuvchi bilan
                    **PRIVATE (1–1) chat** yaratadi.
                    
                    📌 Qoidalar:
                    - O‘zingiz bilan private chat yaratib bo‘lmaydi
                    - Agar ikki foydalanuvchi o‘rtasida private chat allaqachon mavjud bo‘lsa,
                      **yangi chat yaratilmaydi**, mavjud chat qaytariladi
                    - Chat yaratilganda avtomatik ravishda SYSTEM xabar yuboriladi
                    - Ikkala foydalanuvchi ham chat a’zosi bo‘ladi
                    
                    🔒 Autentifikatsiya:
                    - `X-USER-ID` header orqali chatni boshlayotgan foydalanuvchi aniqlanadi
                    
                    📨 Qo‘llanilishi:
                    - Foydalanuvchi profilidan “Xabar yozish” tugmasi bosilganda
                    - Contact list’dan chat ochilganda
                    - Direct message (DM) funksiyasi
                    """
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Private chat muvaffaqiyatli yaratildi yoki mavjud chat qaytarildi",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ChatResponseDto.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Noto‘g‘ri so‘rov (o‘zingiz bilan chat ochish mumkin emas)"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Foydalanuvchi topilmadi"
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Server ichki xatosi"
            )
    })
    @PostMapping("/private/{otherUserId}")
    public ResponseDto<ChatResponseDto> createPrivateChat(

            @Parameter(
                    description = "Private chat ochiladigan foydalanuvchi ID",
                    required = true,
                    example = "99999999-8888-7777-6666-555555555555"
            )
            @PathVariable("otherUserId") UUID otherUserId,

            @Parameter(
                    description = "Chatni boshlayotgan foydalanuvchi ID (auth header)",
                    required = true,
                    example = "11111111-2222-3333-4444-555555555555"
            )
            @RequestHeader("X-USER-ID") UUID requesterId
    ) {
        return chatService.createPrivateChat(requesterId, otherUserId);
    }


    /* =========================================================
       CREATE GROUP CHAT
       ========================================================= */

    @Operation(
            summary = "Group chat yaratish",
            description = """
                    Ushbu endpoint orqali foydalanuvchi **GROUP turidagi chat**
                    yaratishi mumkin.
                    
                    📌 Qoidalar:
                    - Chat turi avtomatik ravishda `GROUP` bo‘ladi
                    - Chatni yaratgan foydalanuvchi **OWNER** rolida qo‘shiladi
                    - Dastlabki a’zolar (agar dto ichida berilgan bo‘lsa) chatga qo‘shiladi
                    - Chat yaratilgach, SYSTEM xabar yuboriladi (masalan: "Guruh yaratildi")
                    - Chat nomi (title) majburiy
                    
                    🔒 Autentifikatsiya:
                    - `X-USER-ID` header orqali chat yaratuvchi aniqlanadi
                    
                    📨 Qo‘llanilishi:
                    - Guruh yaratish (Telegram / WhatsApp uslubida)
                    - Kurs, loyiha yoki jamoa uchun group chat
                    - Bir nechta foydalanuvchini bitta chatga jamlash
                    """
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Group chat muvaffaqiyatli yaratildi",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ChatResponseDto.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Noto‘g‘ri so‘rov (chat nomi bo‘sh yoki noto‘g‘ri)"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "A’zolardan biri topilmadi"
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Server ichki xatosi"
            )
    })
    @PostMapping("/group")
    public ResponseDto<ChatResponseDto> createGroupChat(

            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Group chat yaratish uchun ma’lumotlar",
                    required = true,
                    content = @Content(
                            schema = @Schema(implementation = ChatCreateDto.class)
                    )
            )
            @RequestBody ChatCreateDto dto,

            @Parameter(
                    description = "Group chat yaratuvchi foydalanuvchi ID (auth header)",
                    required = true,
                    example = "11111111-2222-3333-4444-555555555555"
            )
            @RequestHeader("X-USER-ID") UUID creatorId
    ) {
        return chatService.createGroupChat(creatorId, dto);
    }


    /* =========================================================
       GET GROUP MEMBERS
       ========================================================= */

    @Operation(
            summary = "Group chat a’zolarini olish",
            description = """
                    Ushbu endpoint orqali foydalanuvchi **GROUP chat** ichidagi
                    barcha faol a’zolar ro‘yxatini oladi.
                    
                    📌 Qoidalar:
                    - Faqat `GROUP` turidagi chatlar uchun ishlaydi
                    - Foydalanuvchi chat a’zosi bo‘lishi shart
                    - Chatdan chiqqan (leftAt != null) a’zolar qaytarilmaydi
                    - A’zolar rollari bilan birga qaytariladi (OWNER / ADMIN / MEMBER)
                    
                    🔒 Autentifikatsiya:
                    - `X-USER-ID` header orqali foydalanuvchi aniqlanadi
                    
                    📨 Qo‘llanilishi:
                    - Group info sahifasi
                    - Admin panel
                    - A’zolarni boshqarish (add/remove/admin qilish)
                    """
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Group chat a’zolari muvaffaqiyatli olindi",
                    content = @Content(
                            mediaType = "application/json",
                            array = @ArraySchema(
                                    schema = @Schema(implementation = ChatMemberResponseDto.class)
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "403",
                    description = "Foydalanuvchi chat a’zosi emas yoki ruxsat yo‘q"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Chat topilmadi yoki GROUP chat emas"
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Server ichki xatosi"
            )
    })
    @GetMapping("/{chatId}/members")
    public ResponseDto<List<ChatMemberResponseDto>> getGroupMembers(

            @Parameter(
                    description = "A’zolari olinadigan group chat ID",
                    required = true,
                    example = "c1a2b3d4-e5f6-7890-a123-b4567890abcd"
            )
            @PathVariable("chatId") UUID chatId,

            @Parameter(
                    description = "Group chat a’zolarini so‘rayotgan foydalanuvchi ID (auth header)",
                    required = true,
                    example = "11111111-2222-3333-4444-555555555555"
            )
            @RequestHeader("X-USER-ID") UUID requesterId
    ) {
        return chatService.getGroupMembers(chatId, requesterId);
    }


    /* =========================================================
       GET MY GROUPS
       ========================================================= */
    @Operation(
            summary = "Foydalanuvchi a’zo bo‘lgan group chatlar ro‘yxatini olish",
            description = """
                    Ushbu endpoint orqali foydalanuvchi **a’zo bo‘lgan barcha GROUP chatlar**
                    ro‘yxatini oladi.
                    
                    📌 Qoidalar:
                    - Faqat `GROUP` turidagi chatlar qaytariladi
                    - Chatdan chiqilgan (leftAt != null) group chatlar qaytarilmaydi
                    - Chatlar **oxirgi aktivlik (oxirgi xabar)** bo‘yicha saralanadi
                    - Har bir chat uchun asosiy ma’lumotlar qaytariladi (title, memberCount, lastMessage)
                    
                    🔒 Autentifikatsiya:
                    - `X-USER-ID` header orqali foydalanuvchi aniqlanadi
                    
                    📨 Qo‘llanilishi:
                    - Chatlar ro‘yxati sahifasi
                    - Guruhlar tab’i
                    - Mobil ilovalarda chat list
                    """
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Foydalanuvchi group chatlari muvaffaqiyatli olindi",
                    content = @Content(
                            mediaType = "application/json",
                            array = @ArraySchema(
                                    schema = @Schema(implementation = ChatResponseDto.class)
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Server ichki xatosi"
            )
    })
    @GetMapping("/my-groups")
    public ResponseDto<List<ChatResponseDto>> getMyGroups(

            @Parameter(
                    description = "Group chatlari olinadigan foydalanuvchi ID (auth header)",
                    required = true,
                    example = "11111111-2222-3333-4444-555555555555"
            )
            @RequestHeader("X-USER-ID") UUID userId
    ) {
        return chatService.getMyGroups(userId);
    }


    @Operation(
            summary = "Guruhga a’zo qo‘shish",
            description = """
                    Ushbu endpoint orqali **GROUP chatga yangi a’zo qo‘shiladi**.
                    
                    📌 Qoidalar:
                    - Faqat GROUP chatlar uchun
                    - Faqat OWNER yoki ADMIN a’zo qo‘sha oladi
                    - A’zo allaqachon chatda bo‘lsa qayta qo‘shilmaydi
                    - Agar oldin chiqib ketgan bo‘lsa — qayta aktivlashtiriladi
                    - Guruh tarixiga SYSTEM xabar yoziladi
                    
                    🔒 Autentifikatsiya:
                    - `X-USER-ID` header orqali so‘rov yuboruvchi aniqlanadi
                    """
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "A’zo muvaffaqiyatli qo‘shildi"),
            @ApiResponse(responseCode = "400", description = "Noto‘g‘ri so‘rov"),
            @ApiResponse(responseCode = "403", description = "Ruxsat yo‘q (ADMIN/OWNER emas)"),
            @ApiResponse(responseCode = "404", description = "Chat yoki foydalanuvchi topilmadi"),
            @ApiResponse(responseCode = "500", description = "Server ichki xatosi")
    })
    @PostMapping("/{chatId}/members/{newMemberId}")
    public ResponseDto<Void> addMemberToGroup(

            @Parameter(
                    description = "A’zo qo‘shiladigan GROUP chat ID",
                    required = true,
                    example = "c1a2b3d4-e5f6-7890-a123-b4567890abcd"
            )
            @PathVariable UUID chatId,

            @Parameter(
                    description = "Guruhga qo‘shilayotgan foydalanuvchi ID",
                    required = true,
                    example = "99999999-8888-7777-6666-555555555555"
            )
            @PathVariable UUID newMemberId,

            @Parameter(
                    description = "So‘rov yuborayotgan foydalanuvchi ID (auth header)",
                    required = true,
                    example = "11111111-2222-3333-4444-555555555555"
            )
            @RequestHeader("X-USER-ID") UUID requesterId
    ) {
        return chatService.addMemberToGroup(chatId, requesterId, newMemberId);
    }

    @Operation(
            summary = "Chatlar va guruhlarni qidirish",
            description = """
                    Ushbu endpoint orqali foydalanuvchi **chatlar (PRIVATE) va guruhlar (GROUP)**
                    bo‘yicha qidiruv amalga oshiradi.
                    
                    📌 Qoidalar:
                    - PRIVATE va GROUP chatlar birga qidiriladi
                    - Agar PRIVATE chatda foydalanuvchilar bir-birini bloklagan bo‘lsa,
                      u chat qidiruv natijasida chiqmaydi
                    - GROUP chatlarda bloklash hisobga olinmaydi
                    - Qidiruv `title` yoki `username` bo‘yicha amalga oshiriladi
                    
                    🔒 Autentifikatsiya:
                    - `X-USER-ID` header orqali foydalanuvchi aniqlanadi
                    
                    📨 Qo‘llanilishi:
                    - Global qidiruv
                    - Yangi chat boshlash
                    - Guruhlarni topish
                    """
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Qidiruv natijalari muvaffaqiyatli olindi",
                    content = @Content(
                            mediaType = "application/json",
                            array = @ArraySchema(
                                    schema = @Schema(implementation = ChatResponseDto.class)
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Qidiruv matni noto‘g‘ri yoki bo‘sh"
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Server ichki xatosi"
            )
    })
    @GetMapping("/search")
    public ResponseDto<List<ChatResponseDto>> searchChats(

            @Parameter(
                    description = "Qidiruv matni (chat nomi yoki username)",
                    required = true,
                    example = "backend"
            )
            @RequestParam("q") String query,

            @Parameter(
                    description = "Qidiruvni amalga oshirayotgan foydalanuvchi ID (auth header)",
                    required = true,
                    example = "11111111-2222-3333-4444-555555555555"
            )
            @RequestHeader("X-USER-ID") UUID userId
    ) {
        return chatService.searchChats(query, userId);
    }


}
