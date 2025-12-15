package uz.codebyz.onlinecoursebackend.message.service;

import org.springframework.data.domain.Page;
import uz.codebyz.onlinecoursebackend.common.ResponseDto;
import uz.codebyz.onlinecoursebackend.message.dto.request.MessageCreateDto;
import uz.codebyz.onlinecoursebackend.message.dto.request.MessageEditDto;
import uz.codebyz.onlinecoursebackend.message.dto.request.MessageFileCreateDto;


import uz.codebyz.onlinecoursebackend.message.dto.response.MessageResponseDto;

import java.util.List;
import java.util.UUID;

public interface MessageService {

    /* ===================== SEND ===================== */

    /**
     * Private yoki Group chatga xabar yuborish
     */
    ResponseDto<MessageResponseDto> sendMessage(
            UUID chatId,
            MessageCreateDto dto,
            UUID senderId
    );

    /**
     * Fayl bilan xabar yuborish (image, video, file)
     */
    ResponseDto<MessageResponseDto> sendMessageWithFile(
            UUID chatId,
            MessageFileCreateDto dto,
            UUID senderId
    );


    /* ===================== GET ===================== */

    /**
     * Chat bo‘yicha barcha xabarlarni olish (pagination bilan)
     */

    public ResponseDto<Page<MessageResponseDto>> getMessagesByChat(
            UUID chatId,
            int page,
            int size,
            UUID requesterId
    );


    /**
     * Bitta xabarni ID bo‘yicha olish
     */
    ResponseDto<MessageResponseDto> getMessageById(
            UUID messageId,
            UUID requesterId
    );

    /**
     * Chatning oxirgi xabarini olish
     */
    ResponseDto<MessageResponseDto> getLastMessage(
            UUID chatId,
            UUID requesterId
    );


    /* ===================== READ ===================== */

    /**
     * Chatdagi barcha xabarlarni o‘qilgan deb belgilash
     * (PRIVATE chatlar uchun)
     */
    ResponseDto<Void> markChatAsRead(
            UUID chatId,
            UUID userId
    );

    /**
     * Bitta xabarni o‘qilgan deb belgilash
     */
    ResponseDto<Void> markMessageAsRead(
            UUID messageId,
            UUID userId
    );

    /**
     * O‘qilmagan xabarlar sonini olish
     */
    ResponseDto<Long> getUnreadCount(
            UUID chatId,
            UUID userId
    );


    /* ===================== EDIT / DELETE ===================== */

    /**
     * Xabarni tahrirlash (faqat o‘z xabari)
     */
    ResponseDto<MessageResponseDto> editMessage(
            UUID messageId,
            MessageEditDto dto,
            UUID userId
    );

    /**
     * Xabarni o‘chirish (soft delete)
     */
    ResponseDto<Void> deleteMessage(
            UUID messageId,
            UUID userId
    );


    /* ===================== REPLY ===================== */

    /**
     * Xabarga reply qilib javob berish
     */
    ResponseDto<MessageResponseDto> replyMessage(
            UUID chatId,
            UUID replyToMessageId,
            MessageCreateDto dto,
            UUID senderId
    );


    /* ===================== SYSTEM ===================== */

    /**
     * System message yuborish (join/leave, admin changed)
     */
    ResponseDto<Void> sendSystemMessage(
            UUID chatId,
            String content
    );



}
