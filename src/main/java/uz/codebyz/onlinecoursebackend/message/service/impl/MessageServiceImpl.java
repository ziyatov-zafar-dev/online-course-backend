package uz.codebyz.onlinecoursebackend.message.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import uz.codebyz.onlinecoursebackend.common.ResponseDto;
import uz.codebyz.onlinecoursebackend.helper.FileHelper;
import uz.codebyz.onlinecoursebackend.message.dto.req.CreateChatRequestDto;
import uz.codebyz.onlinecoursebackend.message.dto.req.CreateMessageRequestDto;
import uz.codebyz.onlinecoursebackend.message.dto.res.MessageResponseDto;
import uz.codebyz.onlinecoursebackend.message.dto.res.UploadFileResponseDto;
import uz.codebyz.onlinecoursebackend.message.entity.Chat;
import uz.codebyz.onlinecoursebackend.message.entity.FileType;
import uz.codebyz.onlinecoursebackend.message.entity.Message;
import uz.codebyz.onlinecoursebackend.message.mapper.MessageMapper;
import uz.codebyz.onlinecoursebackend.message.repository.ChatRepository;
import uz.codebyz.onlinecoursebackend.message.repository.MessageRepository;
import uz.codebyz.onlinecoursebackend.message.service.ChatService;
import uz.codebyz.onlinecoursebackend.message.service.MessageService;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class MessageServiceImpl implements MessageService {
    private final ChatRepository chatRepository;
    private final MessageRepository messageRepository;
    private final MessageMapper messageMapper;
    private final Logger log = LoggerFactory.getLogger(MessageServiceImpl.class);
    @Value("${upload.messages.files}")
    private String uploadFileUrl;

    public MessageServiceImpl(ChatRepository chatRepository, MessageRepository messageRepository, ChatService chatService, MessageMapper messageMapper) {
        this.chatRepository = chatRepository;
        this.messageRepository = messageRepository;
        this.messageMapper = messageMapper;
    }

    @Override
    public ResponseDto<UploadFileResponseDto> uploadFile(MultipartFile file, FileType fileType) {
        ResponseDto<uz.codebyz.onlinecoursebackend.admin.course.dto.UploadFileResponseDto> uploadFile;
        if (fileType == FileType.VIDEO) {
            uploadFile = FileHelper.uploadFile(file, uploadFileUrl, true);
        } else {
            uploadFile = FileHelper.uploadFile(file, uploadFileUrl, false);
        }
        if (uploadFile.isSuccess()) {
            uz.codebyz.onlinecoursebackend.admin.course.dto.UploadFileResponseDto data = uploadFile.getData();
            return new ResponseDto<>(true, "Success", new UploadFileResponseDto(
                    data.getFileUrl(), data.getFileSize(), FileHelper.getFileSize(file),
                    data.getFileName(), fileType
            ));
        }
        log.error(uploadFile.getMessage());
        return ResponseDto.error(uploadFile.getMessage());
    }

    @Override
    @Transactional
    public ResponseDto<MessageResponseDto> sendMessage(CreateMessageRequestDto dto) {

        // 1️⃣ Chat mavjudligini tekshirish
        Chat chat = chatRepository.findById(dto.getChatId())
                .orElseThrow(() -> new RuntimeException("Chat topilmadi"));

        if (Boolean.TRUE.equals(chat.getDeleted())) {
            throw new RuntimeException("Chat topilmadi");
        }

        // 2️⃣ Sender chat a’zosi ekanini tekshirish
        UUID senderId = dto.getSenderId();
        if (!senderId.equals(chat.getUser1Id()) && !senderId.equals(chat.getUser2Id())) {
            return new ResponseDto<>(false, "Siz bu chatga xabar yubora olmaysiz");
        }

        // 3️⃣ Reply message (agar bo‘lsa)
        Message replyMessage = null;
        if (dto.getReplyMessageId() != null) {
            replyMessage = messageRepository.findById(dto.getReplyMessageId())
                    .orElseThrow(() -> new RuntimeException("Reply message topilmadi"));
        }

        // 4️⃣ Message yaratish
        Message message = new Message();
        message.setChat(chat);
        message.setSenderId(senderId);
        message.setCaption(dto.getCaption());
        assert replyMessage != null;
        message.setReplyMessageId(replyMessage.getId());
        message.setActive(true);

        // 5️⃣ File mavjud bo‘lsa
        if (dto.isHasFile()) {

            if (dto.getImgUrl() == null || dto.getImgSize() == null) {
                return new ResponseDto<>(false, "File ma'lumotlari to‘liq emas");
            }
            message.setHasFile(true);
            message.setFileUrl(dto.getImgUrl());
            message.setFileName(dto.getImgName());
            message.setFileSize(dto.getImgSize());
            message.setFileSizeMB(dto.getImgSizeMB());
        } else {
            message.setHasFile(false);
        }

        // 6️⃣ Saqlash

        message = messageRepository.save(message);

        // 7️⃣ Response
        MessageResponseDto responseDto = messageMapper.toDto(message);
        log.info("Send message successfully");
        return ResponseDto.ok("Xabar muvaffaqiyatli yuborildi", responseDto);
    }

    @Override
    public ResponseDto<Void> deleteMessage(UUID messageId) {
        Optional<Message> mOp = messageRepository.findById(messageId);
        if (mOp.isPresent()) {
            Message message = mOp.get();
            if (message.getActive()) {
                message.setActive(false);
                return new ResponseDto<>(true, "Success");
            }
        }
        log.error("Xabar topilmadi");
        return new ResponseDto<>(false, "Xabar topilmadi");
    }
}
