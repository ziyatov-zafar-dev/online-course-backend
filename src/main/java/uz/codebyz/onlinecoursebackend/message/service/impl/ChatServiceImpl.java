package uz.codebyz.onlinecoursebackend.message.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import uz.codebyz.onlinecoursebackend.common.ResponseDto;
import uz.codebyz.onlinecoursebackend.helper.FileHelper;
import uz.codebyz.onlinecoursebackend.message.dto.req.CreateChatRequestDto;
import uz.codebyz.onlinecoursebackend.message.dto.res.ChatResponseDto;
import uz.codebyz.onlinecoursebackend.message.dto.res.UploadFileResponseDto;
import uz.codebyz.onlinecoursebackend.message.entity.Chat;
import uz.codebyz.onlinecoursebackend.message.entity.FileType;
import uz.codebyz.onlinecoursebackend.message.entity.Message;
import uz.codebyz.onlinecoursebackend.message.mapper.ChatMapper;
import uz.codebyz.onlinecoursebackend.message.repository.ChatRepository;
import uz.codebyz.onlinecoursebackend.message.repository.MessageRepository;
import uz.codebyz.onlinecoursebackend.message.service.ChatService;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class ChatServiceImpl implements ChatService {
    private final MessageRepository messageRepository;
    private final ChatRepository chatRepository;
    private final ChatMapper chatMapper;
    private final Logger log = LoggerFactory.getLogger(ChatServiceImpl.class);

    public ChatServiceImpl(MessageRepository messageRepository, ChatRepository chatRepository, ChatMapper chatMapper) {
        this.messageRepository = messageRepository;
        this.chatRepository = chatRepository;
        this.chatMapper = chatMapper;
    }

    @Override
    public ResponseDto<ChatResponseDto> deleteChat(UUID chatId) {
        Optional<Chat> chatOp = chatRepository.findById(chatId);
        if (chatOp.isPresent()) {
            if (chatOp.get().getDeleted() == false) {
                Chat chat = chatOp.get();
                chat.setDeleted(true);
                chatRepository.save(chat);
                return new ResponseDto<>(true, "Success");
            }
        }
        return new ResponseDto<>(false, "Allaqachon o'chiriilgan");
    }

    @Override
    public ResponseDto<ChatResponseDto> createChat(CreateChatRequestDto req) {
        Optional<Chat> cOp = chatRepository.findPrivateChatBetweenUsers(req.getUser1Id(), req.getUser2Id());
        if (cOp.isPresent()) {
            Chat chat = cOp.get();
            if (chat.getDeleted()) {
                chat.setDeleted(false);
                for (Message message : chat.getMessages()) {
                    message.setActive(false);
                    message = messageRepository.save(message);
                }
                return new ResponseDto<>(true, "Success", chatMapper.toDto(chatRepository.save(chat)));
            }
        } else {
            Chat chat = new Chat();
            chat.setUser1Id(req.getUser1Id());
            chat.setUser2Id(req.getUser2Id());
            chat.setDeleted(false);
            chat.setMessages(new ArrayList<>());
            return new ResponseDto<>(true, "Success", chatMapper.toDto(chatRepository.save(chat)));
        }
        return new ResponseDto<>(false, "Ushbu chat avvaldan mavjud");
    }

    @Override
    public ResponseDto<ChatResponseDto> haveChat(CreateChatRequestDto req) {
        Optional<Chat> cOp = chatRepository.findPrivateChatBetweenUsers(req.getUser1Id(), req.getUser2Id());
        return cOp.map(chat -> new ResponseDto<>(true, "Success", chatMapper.toDto(chat))).orElseGet(() -> new ResponseDto<>(false, "Ushbu chat mavjud emas"));
    }

    @Override
    public ResponseDto<ChatResponseDto> getAllChatMessages(UUID chatId) {
        Optional<Chat> cOp = chatRepository.findById(chatId);
        if (cOp.isEmpty()) {
            return new ResponseDto<>(false, "Chat topilmadi");
        }
        Chat chat = cOp.get();
        if (chat.getDeleted()) {
            return new ResponseDto<>(false, "Chat topilmadi");
        }
        return new ResponseDto<>(true, "Success", chatMapper.toDto(chat));
    }

    @Override
    public ResponseDto<List<ChatResponseDto>> myChats(UUID userId) {
        List<Chat> chats = chatRepository.getChatsByUsers(userId);
        return new ResponseDto<>(true, "Success", chatMapper.toDto(chats));
    }
}
