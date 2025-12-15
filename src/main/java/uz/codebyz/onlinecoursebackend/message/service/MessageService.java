package uz.codebyz.onlinecoursebackend.message.service;

import org.springframework.web.multipart.MultipartFile;
import uz.codebyz.onlinecoursebackend.common.ResponseDto;
import uz.codebyz.onlinecoursebackend.message.dto.req.CreateMessageRequestDto;
import uz.codebyz.onlinecoursebackend.message.dto.res.MessageResponseDto;
import uz.codebyz.onlinecoursebackend.message.dto.res.UploadFileResponseDto;
import uz.codebyz.onlinecoursebackend.message.entity.FileType;
import uz.codebyz.onlinecoursebackend.message.entity.Message;

import java.util.List;
import java.util.UUID;

public interface MessageService {
    ResponseDto<UploadFileResponseDto> uploadFile(MultipartFile file, FileType fileType);

    ResponseDto<MessageResponseDto> sendMessage(CreateMessageRequestDto dto);

    ResponseDto<Void> deleteMessage(UUID messageId);
}
