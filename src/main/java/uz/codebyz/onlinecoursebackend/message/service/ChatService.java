package uz.codebyz.onlinecoursebackend.message.service;

import org.springframework.web.multipart.MultipartFile;
import uz.codebyz.onlinecoursebackend.common.ResponseDto;
import uz.codebyz.onlinecoursebackend.message.dto.req.CreateChatRequestDto;
import uz.codebyz.onlinecoursebackend.message.dto.res.ChatResponseDto;
import uz.codebyz.onlinecoursebackend.message.dto.res.MessageResponseDto;
import uz.codebyz.onlinecoursebackend.message.dto.res.UploadFileResponseDto;
import uz.codebyz.onlinecoursebackend.message.entity.FileType;

import java.util.List;
import java.util.UUID;

public interface ChatService {

    ResponseDto<ChatResponseDto> createChat(CreateChatRequestDto req);

    ResponseDto<ChatResponseDto> deleteChat(UUID chatId);

    ResponseDto<ChatResponseDto> haveChat(CreateChatRequestDto req);

    ResponseDto<ChatResponseDto> getAllChatMessages(UUID chatId);

    ResponseDto<List<ChatResponseDto>> myChats(UUID userId);

}
