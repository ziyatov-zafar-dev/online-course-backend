package uz.codebyz.onlinecoursebackend.message.mapper;

import org.springframework.stereotype.Component;
import uz.codebyz.onlinecoursebackend.message.dto.response.MessageResponseDto;
import uz.codebyz.onlinecoursebackend.message.entity.Message;

import java.util.UUID;

@Component
public class MessageMapper {
    public MessageResponseDto toDto(Message message, UUID senderId) {
        MessageResponseDto messageResponseDto = new MessageResponseDto();
        messageResponseDto.setId(message.getId());
        messageResponseDto.setChatId(message.getChat().getId());
        messageResponseDto.setSenderId(senderId);
        messageResponseDto.setMessageType(message.getMessageType());
        messageResponseDto.setContent(message.getContent());
        messageResponseDto.setReplyToMessageId(message.getId());
        messageResponseDto.setRead(message.getRead());
        messageResponseDto.setCreatedAt(message.getCreatedAt().toString());
        messageResponseDto.setEditedAt(message.getEditedAt().toString());
        return messageResponseDto;
    }
}
