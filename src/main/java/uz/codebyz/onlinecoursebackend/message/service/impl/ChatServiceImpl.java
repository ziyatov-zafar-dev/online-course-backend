package uz.codebyz.onlinecoursebackend.message.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import uz.codebyz.onlinecoursebackend.common.ResponseDto;
import uz.codebyz.onlinecoursebackend.message.dto.request.ChatCreateDto;
import uz.codebyz.onlinecoursebackend.message.dto.response.ChatMemberResponseDto;
import uz.codebyz.onlinecoursebackend.message.dto.response.ChatResponseDto;
import uz.codebyz.onlinecoursebackend.message.dto.response.MessagePreviewDto;
import uz.codebyz.onlinecoursebackend.message.entity.*;
import uz.codebyz.onlinecoursebackend.message.repository.ChatMemberRepository;
import uz.codebyz.onlinecoursebackend.message.repository.ChatRepository;
import uz.codebyz.onlinecoursebackend.message.repository.MessageRepository;
import uz.codebyz.onlinecoursebackend.message.service.ChatService;
import uz.codebyz.onlinecoursebackend.message.service.MessageService;
import uz.codebyz.onlinecoursebackend.user.User;
import uz.codebyz.onlinecoursebackend.user.UserRepository;

import java.time.LocalDateTime;
import java.util.*;

@Service
public class ChatServiceImpl implements ChatService {
    private final Logger log = LoggerFactory.getLogger(ChatServiceImpl.class);
    private final UserRepository userRepository;
    private final ChatRepository chatRepository;
    private final MessageService messageService;
    private final ChatMemberRepository chatMemberRepository;
    private final MessageRepository messageRepository;

    public ChatServiceImpl(UserRepository userRepository, ChatRepository chatRepository, MessageService messageService, ChatMemberRepository chatMemberRepository, MessageRepository messageRepository) {
        this.userRepository = userRepository;
        this.chatRepository = chatRepository;
        this.messageService = messageService;
        this.chatMemberRepository = chatMemberRepository;
        this.messageRepository = messageRepository;
    }

    @Override
    @Transactional
    public ResponseDto<ChatResponseDto> createPrivateChat(
            UUID requesterId,
            UUID otherUserId
    ) {

        if (requesterId == null || otherUserId == null) {
            return new ResponseDto<>(false, "User ID bo‘sh bo‘lishi mumkin emas");
        }

        if (requesterId.equals(otherUserId)) {
            return new ResponseDto<>(false, "O‘zingiz bilan chat ochib bo‘lmaydi");
        }

        User requester = userRepository.findById(requesterId)
                .orElseThrow(() -> new RuntimeException("Requester topilmadi"));

        User otherUser = userRepository.findById(otherUserId)
                .orElseThrow(() -> new RuntimeException("User topilmadi"));

        /* ===== EXISTING CHAT CHECK ===== */
        Optional<Chat> existing =
                chatRepository.findPrivateChatBetweenUsers(requesterId, otherUserId);

        if (existing.isPresent()) {
            return ResponseDto.ok(
                    "Private chat mavjud",
                    new ChatResponseDto(existing.get())
            );
        }

        /* ===== CREATE CHAT ===== */
        Chat chat = new Chat();
        chat.setType(ChatType.PRIVATE);
        chat.setCreatedAt(LocalDateTime.now());

        chat = chatRepository.save(chat);

        /* ===== MEMBERS ===== */
        ChatMember member1 = new ChatMember();
        member1.setChat(chat);
        member1.setUser(requester);
        member1.setRole(ChatMemberRole.MEMBER);
        member1 = chatMemberRepository.save(member1);

        ChatMember member2 = new ChatMember();
        member2.setChat(chat);
        member2.setUser(otherUser);
        member2.setRole(ChatMemberRole.MEMBER);
        member2 = chatMemberRepository.save(member2);


        /* ===== SYSTEM MESSAGE ===== */
        messageService.sendSystemMessage(chat.getId(), "💬 Private chat yaratildi");

        return ResponseDto.ok(
                "Private chat muvaffaqiyatli yaratildi",
                new ChatResponseDto(chat)
        );
    }


    @Override
    @Transactional
    public ResponseDto<ChatResponseDto> createGroupChat(
            UUID creatorId,
            ChatCreateDto dto
    ) {

        /* ===================== VALIDATION ===================== */
        if (creatorId == null) {
            return new ResponseDto<>(false, "Creator ID bo‘sh bo‘lishi mumkin emas");
        }

        if (dto == null) {
            return new ResponseDto<>(false, "So‘rov noto‘g‘ri");
        }

        if (dto.getType() != ChatType.GROUP) {
            return new ResponseDto<>(false, "Faqat GROUP chat yaratish mumkin");
        }

        if (dto.getTitle() == null || dto.getTitle().isBlank()) {
            return new ResponseDto<>(false, "Group nomi majburiy");
        }

        if (dto.getMemberIds() == null || dto.getMemberIds().isEmpty()) {
            return new ResponseDto<>(false, "Group a’zolari bo‘lishi shart");
        }

        /* ===================== CREATOR ===================== */
        User creator = userRepository.findById(creatorId)
                .orElseThrow(() -> new RuntimeException("Creator topilmadi"));

        /* ===================== CREATE CHAT ===================== */
        Chat chat = new Chat();
        chat.setType(ChatType.GROUP);
        chat.setTitle(dto.getTitle().trim());
        chat.setCreatedAt(LocalDateTime.now());

        chatRepository.save(chat);

        /* ===================== ADD CREATOR ===================== */
        ChatMember creatorMember = new ChatMember();
        creatorMember.setChat(chat);
        creatorMember.setUser(creator);
        creatorMember.setJoinedAt(LocalDateTime.now());
        chatMemberRepository.save(creatorMember);

        /* ===================== ADD MEMBERS ===================== */
        // duplikatlarni oldini olish
        Set<UUID> uniqueMemberIds = new HashSet<>(dto.getMemberIds());

        for (UUID memberId : uniqueMemberIds) {

            // creator allaqachon qo‘shilgan
            if (memberId.equals(creatorId)) {
                continue;
            }

            User member = userRepository.findById(memberId)
                    .orElseThrow(() ->
                            new RuntimeException("User topilmadi: " + memberId));

            ChatMember chatMember = new ChatMember();
            chatMember.setChat(chat);
            chatMember.setUser(member);
            chatMember.setJoinedAt(LocalDateTime.now());

            chatMemberRepository.save(chatMember);
        }

        /* ===================== SYSTEM MESSAGE ===================== */
        messageService.sendSystemMessage(
                chat.getId(),
                "👥 Guruh yaratildi: " + chat.getTitle()
        );

        /* ===================== RESPONSE ===================== */
        ChatResponseDto responseDto = new ChatResponseDto(chat);

        return ResponseDto.ok("Group chat muvaffaqiyatli yaratildi", responseDto);
    }

    @Override
    @Transactional(readOnly = true)
    public ResponseDto<List<ChatMemberResponseDto>> getGroupMembers(
            UUID chatId,
            UUID requesterId
    ) {

        /* ================= VALIDATION ================= */
        if (chatId == null || requesterId == null) {
            return new ResponseDto<>(false, "ChatId yoki UserId bo‘sh");
        }

        Chat chat = chatRepository.findById(chatId)
                .orElseThrow(() -> new RuntimeException("Chat topilmadi"));

        if (chat.getType() != ChatType.GROUP) {
            return new ResponseDto<>(false, "Faqat GROUP chatlar uchun ruxsat etiladi");
        }

        /* ================= MEMBER CHECK ================= */
        boolean isMember =
                chatMemberRepository.existsByChatIdAndUserIdAndLeftAtIsNull(chatId, requesterId);

        if (!isMember) {
            return new ResponseDto<>(false, "Siz bu chat a’zosi emassiz");
        }

        /* ================= GET MEMBERS ================= */
        List<ChatMember> members =
                chatMemberRepository.findActiveMembersByChatId(chatId);

        List<ChatMemberResponseDto> response = members.stream()
                .map(ChatMemberResponseDto::new)
                .toList();
        return ResponseDto.ok("Guruh a’zolari", response);
    }

    @Override
    @Transactional(readOnly = true)
    public ResponseDto<List<ChatResponseDto>> getMyGroups(UUID userId) {

        if (userId == null) {
            return new ResponseDto<>(false, "User ID bo‘sh bo‘lishi mumkin emas");
        }

        // user mavjudligini tekshirish (optional, lekin yaxshi)
        userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User topilmadi"));

        List<Chat> chats =
                chatMemberRepository.findActiveGroupChatsByUserId(userId);

        List<ChatResponseDto> response = chats.stream()
                .map(ChatResponseDto::new)
                .toList();

        return ResponseDto.ok("User guruhlari", response);
    }

    @Override
    @Transactional
    public ResponseDto<Void> addMemberToGroup(UUID chatId, UUID requesterId, UUID newMemberId) {

        if (chatId == null || requesterId == null || newMemberId == null) {
            return new ResponseDto<>(false, "chatId/requesterId/newMemberId bo‘sh bo‘lishi mumkin emas");
        }
        if (requesterId.equals(newMemberId)) {
            return new ResponseDto<>(false, "O‘zingizni qayta qo‘sha olmaysiz");
        }

        /* ================= CHAT ================= */
        Chat chat = chatRepository.findById(chatId)
                .orElseThrow(() -> new RuntimeException("Chat topilmadi"));

        if (chat.getType() != ChatType.GROUP) {
            return new ResponseDto<>(false, "Faqat GROUP chatga a’zo qo‘shish mumkin");
        }

        /* ================= REQUESTER MEMBER + ROLE CHECK ================= */
        ChatMember requesterMember = chatMemberRepository.findActiveMember(chatId, requesterId)
                .orElse(null);

        if (requesterMember == null) {
            return new ResponseDto<>(false, "Siz bu guruh a’zosi emassiz");
        }

        ChatMemberRole role = requesterMember.getRole();
        if (role != ChatMemberRole.OWNER && role != ChatMemberRole.ADMIN) {
            return new ResponseDto<>(false, "A’zo qo‘shish uchun OWNER yoki ADMIN bo‘lishingiz kerak");
        }

        /* ================= NEW MEMBER EXISTS ================= */
        User newMemberUser = userRepository.findById(newMemberId)
                .orElseThrow(() -> new RuntimeException("Qo‘shilayotgan user topilmadi"));

        /* ================= ALREADY MEMBER? ================= */
        // Avvaldan active bo‘lsa — qaytaramiz
        if (chatMemberRepository.existsByChatIdAndUserIdAndLeftAtIsNull(chatId, newMemberId)) {
            return ResponseDto.ok("Foydalanuvchi allaqachon guruh a’zosi");
        }

        // Oldin kirib chiqib ketgan bo‘lsa — leftAt ni null qilib qayta aktiv qilamiz
        ChatMember existingAny = chatMemberRepository.findByChatIdAndUserId(chatId, newMemberId)
                .orElse(null);

        if (existingAny != null) {
            existingAny.setLeftAt(null);
            existingAny.setRole(ChatMemberRole.MEMBER); // qayta kirsa default MEMBER
            existingAny.setJoinedAt(LocalDateTime.now());
            chatMemberRepository.save(existingAny);
        } else {
            ChatMember newMember = new ChatMember();
            newMember.setChat(chat);
            newMember.setUser(newMemberUser);
            newMember.setRole(ChatMemberRole.MEMBER); // default
            // joinedAt @PrePersist ham qo‘yadi, lekin aniq bo‘lsin desang:
            newMember.setJoinedAt(LocalDateTime.now());
            chatMemberRepository.save(newMember);
        }

        /* ================= SYSTEM MESSAGE ================= */
        // (ixtiyoriy) Chat tarixida ko‘rinsin
        messageService.sendSystemMessage(
                chatId,
                "➕ @" + safeUsername(newMemberUser) + " guruhga qo‘shildi"
        );

        return ResponseDto.ok("A’zo guruhga qo‘shildi");
    }

    private String safeUsername(User u) {
        if (u == null) return "user";
        String username = u.getUsername();
        if (username == null || username.isBlank()) return "user";
        return username.trim();
    }


    @Override
    public ResponseDto<List<ChatResponseDto>> searchChats(String query, UUID userId) {

        if (query == null || query.isBlank()) {
            return ResponseDto.ok("Natija yo‘q", List.of());
        }

        List<Chat> chats = chatRepository.searchChats(query, userId);

        List<ChatResponseDto> result = chats.stream()
                .map(ChatResponseDto::new)
                .toList();

        return ResponseDto.ok("Chatlar va guruhlar", result);
    }
    /*private ChatResponseDto toSearchDto(Chat chat, UUID userId) {

        ChatResponseDto dto = new ChatResponseDto(chat);

        *//* ==== USER MEMBERMI? ==== *//*
        List<ChatMember> activeMembers =
                chatMemberRepository.findActiveMembers(chat.getId());

        boolean isMember = activeMembers.stream()
                .anyMatch(cm -> cm.getUser().getId().equals(userId));

        *//* ==== FAQAT AGAR A’ZO BO‘LSA MEMBERS QAYTARAMIZ ==== *//*
        if (isMember) {
            List<ChatMemberResponseDto> memberDtos = activeMembers.stream()
                    .map(ChatMemberResponseDto::new)
                    .toList();

            dto.setMembers(memberDtos);
        } else {
            dto.setMembers(null); // ❗ MUHIM
        }

        *//* ==== OXIRGI XABAR ==== *//*
        Message last = messageRepository.findLastMessage(chat.getId()).orElse(null);
        if (last != null) {
            dto.setLastMessage(new MessagePreviewDto(last));
        }

        return dto;
    }*/


}
