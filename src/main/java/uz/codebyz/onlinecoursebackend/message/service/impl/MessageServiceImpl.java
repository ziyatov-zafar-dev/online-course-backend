package uz.codebyz.onlinecoursebackend.message.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import uz.codebyz.onlinecoursebackend.common.ResponseDto;
import uz.codebyz.onlinecoursebackend.helper.CurrentTime;
import uz.codebyz.onlinecoursebackend.message.dto.request.ChatCreateDto;
import uz.codebyz.onlinecoursebackend.message.dto.request.MessageCreateDto;
import uz.codebyz.onlinecoursebackend.message.dto.request.MessageEditDto;
import uz.codebyz.onlinecoursebackend.message.dto.request.MessageFileCreateDto;
import uz.codebyz.onlinecoursebackend.message.dto.response.ChatMemberResponseDto;
import uz.codebyz.onlinecoursebackend.message.dto.response.ChatResponseDto;
import uz.codebyz.onlinecoursebackend.message.dto.response.MessageResponseDto;
import uz.codebyz.onlinecoursebackend.message.entity.*;
import uz.codebyz.onlinecoursebackend.message.mapper.MessageMapper;
import uz.codebyz.onlinecoursebackend.message.repository.*;
import uz.codebyz.onlinecoursebackend.message.service.MessageService;
import uz.codebyz.onlinecoursebackend.user.User;
import uz.codebyz.onlinecoursebackend.user.UserRepository;

import java.time.LocalDateTime;
import java.util.*;

@Service

public class MessageServiceImpl implements MessageService {
    private final Logger log = LoggerFactory.getLogger(MessageServiceImpl.class);
    private final ChatRepository chatRepository;
    private final UserRepository userRepository;
    private final ChatMemberRepository chatMemberRepository;
    private final BlockUserRepository blockUserRepository;
    private final MessageRepository messageRepository;
    private final MessageMapper messageMapper;
    private final MessageFileRepository messageFileRepository;

    public MessageServiceImpl(ChatRepository chatRepository, UserRepository userRepository, ChatMemberRepository chatMemberRepository, BlockUserRepository blockUserRepository, MessageRepository messageRepository, MessageMapper messageMapper, MessageFileRepository messageFileRepository) {
        this.chatRepository = chatRepository;
        this.userRepository = userRepository;
        this.chatMemberRepository = chatMemberRepository;
        this.blockUserRepository = blockUserRepository;
        this.messageRepository = messageRepository;
        this.messageMapper = messageMapper;
        this.messageFileRepository = messageFileRepository;
    }

    @Override
    public ResponseDto<MessageResponseDto> sendMessage(
            UUID chatId,
            MessageCreateDto dto,
            UUID senderId
    ) {

        /* ===================== CHAT ===================== */
        Chat chat = chatRepository.findById(chatId)
                .orElseThrow(() -> new RuntimeException("Chat topilmadi"));

        /* ===================== USER ===================== */
        User sender = userRepository.findById(senderId)
                .orElseThrow(() -> new RuntimeException("User topilmadi"));

        /* ===================== MEMBER CHECK ===================== */
        boolean isMember = chatMemberRepository
                .existsByChatIdAndUserId(chatId, senderId);
        if (!isMember) {
            return new ResponseDto<>(false, "Siz bu chatga xabar yubora olmaysiz");
        }

        /* ===================== BLOCK CHECK (PRIVATE) ===================== */
        if (chat.getType() == ChatType.PRIVATE) {

            Optional<ChatMember> otherMember =
                    chatMemberRepository.findOtherPrivateMember(chatId, senderId);

            if (otherMember.isPresent()) {
                User receiver = otherMember.get().getUser();

                boolean blocked =
                        blockUserRepository.existsByFromAndToAndActiveTrue(sender, receiver)
                                || blockUserRepository.existsByFromAndToAndActiveTrue(receiver, sender);
                if (blocked) {
                    return new ResponseDto<>(false, "Xabar yuborish mumkin emas (bloklangan)");
                }
            }
        }

        /* ===================== MESSAGE TYPE VALIDATION ===================== */
        if (dto.getMessageType() != MessageType.TEXT) {
            return new ResponseDto<>(false, "Bu endpoint faqat TEXT message uchun");
        }

        /* ===================== REPLY CHECK ===================== */
        Message replyMessage = null;
        if (dto.getReplyToMessageId() != null) {
            replyMessage = messageRepository
                    .findById(dto.getReplyToMessageId())
                    .orElseThrow(() -> new RuntimeException("Reply qilinayotgan xabar topilmadi"));

            if (!replyMessage.getChat().getId().equals(chatId)) {
                return new ResponseDto<>(false, "Reply boshqa chatdagi xabarga bo‘lishi mumkin emas");
            }
        }

        /* ===================== SAVE MESSAGE ===================== */
        Message message = new Message();
        message.setChat(chat);
        message.setSender(sender);
        message.setMessageType(MessageType.TEXT);
        message.setContent(dto.getContent());
        message.setReplyTo(replyMessage);
        message.setCreatedAt(LocalDateTime.now());
        message.setEdited(false);
        message.setRead(false);
        /* ===================== MAP TO RESPONSE ===================== */

        return ResponseDto.ok("Xabar yuborildi", messageMapper.toDto(
                messageRepository.save(message), senderId)
        );
    }

    @Override
    public ResponseDto<MessageResponseDto> sendMessageWithFile(
            UUID chatId,
            MessageFileCreateDto dto,
            UUID senderId
    ) {

        /* ===================== BASIC VALIDATION ===================== */
        if (dto == null) {
            return new ResponseDto<>(false, "So‘rov noto‘g‘ri (dto null)");
        }

        if (dto.getMessageType() == null) {
            return new ResponseDto<>(false, "messageType majburiy");
        }

        if (dto.getMessageType() == MessageType.TEXT || dto.getMessageType() == MessageType.SYSTEM) {
            return new ResponseDto<>(false, "Bu endpoint faqat fayl xabarlari uchun (IMAGE / VIDEO / FILE)");
        }

        if (dto.getFileUrl() == null || dto.getFileUrl().isBlank()) {
            return new ResponseDto<>(false, "fileUrl majburiy");
        }

        /* ===================== CHAT ===================== */
        Chat chat = chatRepository.findById(chatId)
                .orElseThrow(() -> new RuntimeException("Chat topilmadi"));

        /* ===================== USER ===================== */
        User sender = userRepository.findById(senderId)
                .orElseThrow(() -> new RuntimeException("User topilmadi"));

        /* ===================== MEMBER CHECK ===================== */
        if (!chatMemberRepository.existsByChatIdAndUserId(chatId, senderId)) {
            return new ResponseDto<>(false, "Siz bu chatga xabar yubora olmaysiz");
        }

        /* ===================== BLOCK CHECK (PRIVATE) ===================== */
        if (chat.getType() == ChatType.PRIVATE) {
            Optional<ChatMember> otherMember =
                    chatMemberRepository.findOtherPrivateMember(chatId, senderId);

            if (otherMember.isPresent()) {
                User receiver = otherMember.get().getUser();

                boolean blocked =
                        blockUserRepository.existsByFromAndToAndActiveTrue(sender, receiver)
                                || blockUserRepository.existsByFromAndToAndActiveTrue(receiver, sender);

                if (blocked) {
                    return new ResponseDto<>(false, "Xabar yuborish mumkin emas (bloklangan)");
                }
            }
        }

        /* ===================== REPLY CHECK ===================== */
        Message replyMessage = null;
        if (dto.getReplyToMessageId() != null) {
            replyMessage = messageRepository.findById(dto.getReplyToMessageId())
                    .orElseThrow(() -> new RuntimeException("Reply qilinayotgan xabar topilmadi"));

            if (!replyMessage.getChat().getId().equals(chatId)) {
                return new ResponseDto<>(false, "Reply boshqa chatdagi xabarga bo‘lishi mumkin emas");
            }
        }

        /* ===================== SAVE MESSAGE ===================== */
        Message message = new Message();
        message.setChat(chat);
        message.setSender(sender);
        message.setMessageType(dto.getMessageType());
        message.setContent(dto.getCaption()); // caption → content
        message.setReplyTo(replyMessage);
        message.setCreatedAt(LocalDateTime.now());
        message.setEdited(false);

        message = messageRepository.save(message);

        /* ===================== MAP MessageType -> MessageFileType ===================== */
        MessageFileType fileType;
        if (dto.getMessageType() == MessageType.IMAGE) {
            fileType = MessageFileType.IMAGE;
        } else if (dto.getMessageType() == MessageType.VIDEO) {
            fileType = MessageFileType.VIDEO;
        } else {
            fileType = MessageFileType.FILE;
        }

        /* ===================== SAVE FILE ===================== */
        MessageFile file = new MessageFile();
        file.setMessage(message);
        file.setFileUrl(dto.getFileUrl());
        file.setFileName(dto.getFileName());
        file.setFileSize(dto.getFileSize());
        file.setFileType(fileType);
        file = messageFileRepository.save(file);

        /* ===================== RESPONSE ===================== */
        MessageResponseDto response = messageMapper.toDto(message, senderId);

        return ResponseDto.ok("Fayl bilan xabar yuborildi", response);
    }


    @Override
    public ResponseDto<Page<MessageResponseDto>> getMessagesByChat(
            UUID chatId,
            int page,
            int size,
            UUID requesterId
    ) {

        /* ===================== VALIDATION ===================== */
        if (page < 0 || size <= 0) {
            return new ResponseDto<>(false, "Pagination parametrlari noto‘g‘ri");
        }

        /* ===================== CHAT ===================== */
        Chat chat = chatRepository.findById(chatId)
                .orElseThrow(() -> new RuntimeException("Chat topilmadi"));

        /* ===================== USER ===================== */
        User requester = userRepository.findById(requesterId)
                .orElseThrow(() -> new RuntimeException("User topilmadi"));

        /* ===================== MEMBER CHECK ===================== */
        if (!chatMemberRepository.existsByChatIdAndUserId(chatId, requesterId)) {
            return new ResponseDto<>(false, "Siz bu chat xabarlarini ko‘ra olmaysiz");
        }

        /* ===================== PAGEABLE ===================== */
        Pageable pageable = PageRequest.of(
                page,
                size,
                Sort.by(Sort.Direction.DESC, "createdAt")
        );

        /* ===================== GET PAGE ===================== */
        Page<Message> messagePage =
                messageRepository.findAllByChatId(chatId, pageable);

        /* ===================== MARK AS READ (PRIVATE) ===================== */
        if (chat.getType() == ChatType.PRIVATE) {
            messageRepository.markMessagesAsRead(chatId, requesterId);
        }

        /* ===================== MAP ENTITY -> DTO (PAGE) ===================== */
        Page<MessageResponseDto> dtoPage = messagePage.map(
                message -> messageMapper.toDto(message, requesterId)
        );
        return ResponseDto.ok("Xabarlar muvaffaqiyatli olindi", dtoPage);
    }


    @Override
    public ResponseDto<MessageResponseDto> getMessageById(
            UUID messageId,
            UUID requesterId
    ) {

        /* ===================== MESSAGE ===================== */
        Message message = messageRepository.findById(messageId)
                .orElseThrow(() -> new RuntimeException("Xabar topilmadi"));

        /* ===================== CHAT ===================== */
        Chat chat = message.getChat();
        if (chat == null) {
            return new ResponseDto<>(false, "Xabar chatga biriktirilmagan");
        }

        /* ===================== USER ===================== */
        User requester = userRepository.findById(requesterId)
                .orElseThrow(() -> new RuntimeException("User topilmadi"));

        /* ===================== MEMBER CHECK ===================== */
        boolean isMember =
                chatMemberRepository.existsByChatIdAndUserId(chat.getId(), requesterId);

        if (!isMember) {
            return new ResponseDto<>(false, "Siz bu xabarni ko‘ra olmaysiz");
        }

        /* ===================== MARK AS READ (PRIVATE) ===================== */
        if (chat.getType() == ChatType.PRIVATE) {
            // faqat boshqa user yuborgan bo‘lsa
            if (!message.getSender().getId().equals(requesterId)
                    && !Boolean.TRUE.equals(message.getRead())) {

                message.setRead(true);
                messageRepository.save(message);
            }
        }

        /* ===================== MAP TO DTO ===================== */
        MessageResponseDto response =
                messageMapper.toDto(message, requesterId);

        return ResponseDto.ok("Xabar muvaffaqiyatli olindi", response);
    }

    @Override
    public ResponseDto<MessageResponseDto> getLastMessage(
            UUID chatId,
            UUID requesterId
    ) {

        /* ===================== CHAT ===================== */
        Chat chat = chatRepository.findById(chatId)
                .orElseThrow(() -> new RuntimeException("Chat topilmadi"));

        /* ===================== USER ===================== */
        User requester = userRepository.findById(requesterId)
                .orElseThrow(() -> new RuntimeException("User topilmadi"));

        /* ===================== MEMBER CHECK ===================== */
        boolean isMember =
                chatMemberRepository.existsByChatIdAndUserId(chatId, requesterId);

        if (!isMember) {
            return new ResponseDto<>(false, "Siz bu chat xabarlarini ko‘ra olmaysiz");
        }

        /* ===================== GET LAST MESSAGE ===================== */
        Optional<Message> optionalMessage =
                messageRepository.findTopByChatIdOrderByCreatedAtDesc(chatId);

        if (optionalMessage.isEmpty()) {
            return new ResponseDto<>(true, "Chatda hali xabar yo‘q", null);
        }

        Message message = optionalMessage.get();

        /* ===================== MARK AS READ (PRIVATE) ===================== */
        if (chat.getType() == ChatType.PRIVATE) {
            if (!message.getSender().getId().equals(requesterId)
                    && !Boolean.TRUE.equals(message.getRead())) {

                message.setRead(true);
                messageRepository.save(message);
            }
        }

        /* ===================== MAP TO DTO ===================== */
        MessageResponseDto response =
                messageMapper.toDto(message, requesterId);

        return ResponseDto.ok("Oxirgi xabar olindi", response);
    }

    @Override
    @Transactional
    public ResponseDto<Void> markChatAsRead(UUID chatId, UUID userId) {

        /* ===================== CHAT ===================== */
        Chat chat = chatRepository.findById(chatId)
                .orElseThrow(() -> new RuntimeException("Chat topilmadi"));

        /* ===================== USER ===================== */
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User topilmadi"));

        /* ===================== MEMBER CHECK ===================== */
        boolean isMember =
                chatMemberRepository.existsByChatIdAndUserId(chatId, userId);

        if (!isMember) {
            return new ResponseDto<>(false, "Siz bu chatga tegishli emassiz");
        }

        /* ===================== CHAT TYPE CHECK ===================== */
        if (chat.getType() != ChatType.PRIVATE) {
            return new ResponseDto<>(false, "Bu amal faqat PRIVATE chatlar uchun");
        }

        /* ===================== MARK AS READ ===================== */
        messageRepository.markMessagesAsRead(chatId, userId);

        return ResponseDto.ok("Chatdagi barcha xabarlar o‘qildi");
    }


    @Override
    @Transactional
    public ResponseDto<Void> markMessageAsRead(UUID messageId, UUID userId) {

        /* ===================== MESSAGE ===================== */
        Message message = messageRepository.findById(messageId)
                .orElseThrow(() -> new RuntimeException("Xabar topilmadi"));

        /* ===================== CHAT ===================== */
        Chat chat = message.getChat();
        if (chat == null) {
            return new ResponseDto<>(false, "Xabar chatga biriktirilmagan");
        }

        /* ===================== USER ===================== */
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User topilmadi"));

        /* ===================== MEMBER CHECK ===================== */
        boolean isMember =
                chatMemberRepository.existsByChatIdAndUserId(chat.getId(), userId);

        if (!isMember) {
            return new ResponseDto<>(false, "Siz bu xabarni o‘qilgan deb belgilay olmaysiz");
        }

        /* ===================== CHAT TYPE CHECK ===================== */
        if (chat.getType() != ChatType.PRIVATE) {
            return new ResponseDto<>(false, "Bu amal faqat PRIVATE chatlar uchun");
        }

        /* ===================== SENDER CHECK ===================== */
        if (message.getSender().getId().equals(userId)) {
            // o‘z xabarini read qilish shart emas
            return ResponseDto.ok("Xabar allaqachon o‘qilgan");
        }

        /* ===================== ALREADY READ CHECK ===================== */
        if (Boolean.TRUE.equals(message.getRead())) {
            return ResponseDto.ok("Xabar allaqachon o‘qilgan");
        }

        /* ===================== MARK AS READ ===================== */
        message.setRead(true);
        messageRepository.save(message);

        return ResponseDto.ok("Xabar o‘qilgan deb belgilandi");
    }


    @Override
    public ResponseDto<Long> getUnreadCount(UUID chatId, UUID userId) {

        /* ===================== CHAT ===================== */
        Chat chat = chatRepository.findById(chatId)
                .orElseThrow(() -> new RuntimeException("Chat topilmadi"));

        /* ===================== USER ===================== */
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User topilmadi"));

        /* ===================== MEMBER CHECK ===================== */
        boolean isMember =
                chatMemberRepository.existsByChatIdAndUserId(chatId, userId);

        if (!isMember) {
            return new ResponseDto<>(false, "Siz bu chatga tegishli emassiz");
        }

        /* ===================== CHAT TYPE CHECK ===================== */
        if (chat.getType() != ChatType.PRIVATE) {
            return new ResponseDto<>(false, "Unread count faqat PRIVATE chatlar uchun");
        }

        /* ===================== COUNT ===================== */
        long unreadCount =
                messageRepository.countUnreadMessages(chatId, userId);

        return ResponseDto.ok("O‘qilmagan xabarlar soni", unreadCount);
    }


    @Override
    @Transactional
    public ResponseDto<MessageResponseDto> editMessage(
            UUID messageId,
            MessageEditDto dto,
            UUID userId
    ) {

        /* ===================== VALIDATION ===================== */
        if (dto == null || dto.getContent() == null || dto.getContent().isBlank()) {
            return new ResponseDto<>(false, "Tahrirlanayotgan matn bo‘sh bo‘lishi mumkin emas");
        }

        /* ===================== MESSAGE ===================== */
        Message message = messageRepository.findById(messageId)
                .orElseThrow(() -> new RuntimeException("Xabar topilmadi"));

        /* ===================== USER ===================== */
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User topilmadi"));

        /* ===================== CHAT ===================== */
        Chat chat = message.getChat();
        if (chat == null) {
            return new ResponseDto<>(false, "Xabar chatga biriktirilmagan");
        }

        /* ===================== MEMBER CHECK ===================== */
        boolean isMember =
                chatMemberRepository.existsByChatIdAndUserId(chat.getId(), userId);

        if (!isMember) {
            return new ResponseDto<>(false, "Siz bu xabarni tahrirlay olmaysiz");
        }

        /* ===================== OWNER CHECK ===================== */
        if (!message.getSender().getId().equals(userId)) {
            return new ResponseDto<>(false, "Faqat o‘z xabaringizni tahrirlashingiz mumkin");
        }

        /* ===================== MESSAGE TYPE CHECK ===================== */
        if (message.getMessageType() != MessageType.TEXT) {
            return new ResponseDto<>(false, "Faqat TEXT xabarlarni tahrirlash mumkin");
        }

        /* ===================== NO CHANGE CHECK ===================== */
        if (dto.getContent().equals(message.getContent())) {
            MessageResponseDto response =
                    messageMapper.toDto(message, userId);
            return ResponseDto.ok("Xabar o‘zgarmagan", response);
        }

        /* ===================== EDIT MESSAGE ===================== */
        message.setContent(dto.getContent());
        message.setEdited(true);
        message.setEditedAt(LocalDateTime.now());

        messageRepository.save(message);

        /* ===================== RESPONSE ===================== */
        MessageResponseDto response =
                messageMapper.toDto(message, userId);

        return ResponseDto.ok("Xabar tahrirlandi", response);
    }

    @Override
    @Transactional
    public ResponseDto<Void> deleteMessage(UUID messageId, UUID userId) {

        /* ===================== MESSAGE ===================== */
        Message message = messageRepository.findById(messageId)
                .orElseThrow(() -> new RuntimeException("Xabar topilmadi"));

        /* ===================== USER ===================== */
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User topilmadi"));

        /* ===================== CHAT ===================== */
        Chat chat = message.getChat();
        if (chat == null) {
            return new ResponseDto<>(false, "Xabar chatga biriktirilmagan");
        }

        /* ===================== MEMBER CHECK ===================== */
        boolean isMember =
                chatMemberRepository.existsByChatIdAndUserId(chat.getId(), userId);

        if (!isMember) {
            return new ResponseDto<>(false, "Siz bu xabarni o‘chira olmaysiz");
        }

        /* ===================== OWNER CHECK ===================== */
        if (!message.getSender().getId().equals(userId)) {
            return new ResponseDto<>(false, "Faqat o‘z xabaringizni o‘chirishingiz mumkin");
        }

        /* ===================== ALREADY DELETED CHECK ===================== */
        if (Boolean.TRUE.equals(message.getDeleted())) {
            return ResponseDto.ok("Xabar allaqachon o‘chirilgan");
        }

        /* ===================== SOFT DELETE ===================== */
        message.setDeleted(true);
        message.setDeletedAt(CurrentTime.currentTime());
        message.setContent("❌ Xabar o‘chirildi");

        messageRepository.save(message);

        return ResponseDto.ok("Xabar muvaffaqiyatli o‘chirildi");
    }


    @Override
    @Transactional
    public ResponseDto<MessageResponseDto> replyMessage(
            UUID chatId,
            UUID replyToMessageId,
            MessageCreateDto dto,
            UUID senderId
    ) {

        /* ===================== VALIDATION ===================== */
        if (dto == null || dto.getContent() == null || dto.getContent().isBlank()) {
            return new ResponseDto<>(false, "Reply matni bo‘sh bo‘lishi mumkin emas");
        }

        if (dto.getMessageType() != MessageType.TEXT) {
            return new ResponseDto<>(false, "Reply faqat TEXT xabar bilan mumkin");
        }

        /* ===================== CHAT ===================== */
        Chat chat = chatRepository.findById(chatId)
                .orElseThrow(() -> new RuntimeException("Chat topilmadi"));

        /* ===================== USER ===================== */
        User sender = userRepository.findById(senderId)
                .orElseThrow(() -> new RuntimeException("User topilmadi"));

        /* ===================== MEMBER CHECK ===================== */
        if (!chatMemberRepository.existsByChatIdAndUserId(chatId, senderId)) {
            return new ResponseDto<>(false, "Siz bu chatga xabar yubora olmaysiz");
        }

        /* ===================== BLOCK CHECK (PRIVATE) ===================== */
        if (chat.getType() == ChatType.PRIVATE) {
            Optional<ChatMember> otherMember =
                    chatMemberRepository.findOtherPrivateMember(chatId, senderId);

            if (otherMember.isPresent()) {
                User receiver = otherMember.get().getUser();

                boolean blocked =
                        blockUserRepository.existsByFromAndToAndActiveTrue(sender, receiver)
                                || blockUserRepository.existsByFromAndToAndActiveTrue(receiver, sender);

                if (blocked) {
                    return new ResponseDto<>(false, "Xabar yuborish mumkin emas (bloklangan)");
                }
            }
        }

        /* ===================== REPLY MESSAGE ===================== */
        Message replyTo = messageRepository.findById(replyToMessageId)
                .orElseThrow(() -> new RuntimeException("Reply qilinayotgan xabar topilmadi"));

        if (!replyTo.getChat().getId().equals(chatId)) {
            return new ResponseDto<>(false, "Reply boshqa chatdagi xabarga bo‘lishi mumkin emas");
        }

        /* ===================== CREATE MESSAGE ===================== */
        Message message = new Message();
        message.setChat(chat);
        message.setSender(sender);
        message.setMessageType(MessageType.TEXT);
        message.setContent(dto.getContent());
        message.setReplyTo(replyTo);
        message.setCreatedAt(LocalDateTime.now());
        message.setEdited(false);

        messageRepository.save(message);

        /* ===================== RESPONSE ===================== */
        MessageResponseDto response =
                messageMapper.toDto(message, senderId);

        return ResponseDto.ok("Reply xabar yuborildi", response);
    }


    @Override
    @Transactional
    public ResponseDto<Void> sendSystemMessage(UUID chatId, String content) {

        /* ===================== VALIDATION ===================== */
        if (content == null || content.isBlank()) {
            return new ResponseDto<>(false, "System xabar matni bo‘sh bo‘lishi mumkin emas");
        }

        /* ===================== CHAT ===================== */
        Chat chat = chatRepository.findById(chatId)
                .orElseThrow(() -> new RuntimeException("Chat topilmadi"));

        /* ===================== CREATE SYSTEM MESSAGE ===================== */
        Message message = new Message();
        message.setChat(chat);
        message.setSender(null);                     // ❗ SYSTEM message’da sender yo‘q
        message.setMessageType(MessageType.SYSTEM);  // ❗ SYSTEM
        message.setContent(content);
        message.setCreatedAt(LocalDateTime.now());
        message.setEdited(false);
        message.setRead(true);                     // SYSTEM xabar read status talab qilmaydi
        message.setDeleted(false);

        messageRepository.save(message);

        return ResponseDto.ok("System xabar yuborildi");
    }
}
