package uz.codebyz.onlinecoursebackend.message.mapper;

import org.springframework.stereotype.Component;
import uz.codebyz.onlinecoursebackend.helper.CurrentTime;
import uz.codebyz.onlinecoursebackend.message.dto.res.MessageFileType;
import uz.codebyz.onlinecoursebackend.message.dto.res.MessageResponseDto;
import uz.codebyz.onlinecoursebackend.message.entity.Message;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class MessageMapper {
    public MessageResponseDto toDto(Message message) {
        MessageResponseDto dto = new MessageResponseDto();
        dto.setMessageId(message.getId());
        dto.setReplyMessageId(message.getReplyMessageId());
        dto.setChatId(message.getChat().getId());
        dto.setSenderId(message.getSenderId());
        dto.setCaption(message.getCaption());
        dto.setHasFile(message.isHasFile());
        dto.setImgUrl(message.getFileUrl());
        dto.setImgName(message.getFileName());
        dto.setImgSize(message.getFileSize());
        dto.setImgSizeMB(message.getFileSizeMB());
        dto.setFileType(new MessageFileType(
                message.getFileType().getName(),
                message.getFileType().getDescription(),
                message.getFileType().getMimeTypes()
        ));
        LocalDateTime created = message.getCreated();
        Boolean edited = message.getEdited();
        LocalDateTime updated = message.getUpdated();
        dto.setCreated(created.toString());
        dto.setEdited(edited);
        dto.setUpdated(updated.toString());
        return dto;
    }

    public List<MessageResponseDto> toDto(List<Message> messages) {
        return messages.stream().map(this::toDto).collect(Collectors.toList());
    }
}
