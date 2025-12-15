package uz.codebyz.onlinecoursebackend.message.mapper;

import org.springframework.stereotype.Component;
import uz.codebyz.onlinecoursebackend.message.dto.res.ChatResponseDto;
import uz.codebyz.onlinecoursebackend.message.dto.res.MessageResponseDto;
import uz.codebyz.onlinecoursebackend.message.entity.Chat;
import uz.codebyz.onlinecoursebackend.message.entity.Message;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class ChatMapper {
    private final MessageMapper messageMapper;

    public ChatMapper(MessageMapper messageMapper) {
        this.messageMapper = messageMapper;
    }

    public ChatResponseDto toDto(Chat chat) {
        ChatResponseDto dto = new ChatResponseDto();
        dto.setMessages(messageMapper.toDto(chat.getMessages().stream().filter(Message::getActive).toList()));
        dto.setChatId(chat.getId());
        dto.setUser1Id(chat.getUser1Id());
        dto.setUser2Id(chat.getUser2Id());
        return dto;
    }
    public List<ChatResponseDto> toDto(List<Chat> chats) {
        return chats.stream().map(this::toDto).collect(Collectors.toList());
    }
}
