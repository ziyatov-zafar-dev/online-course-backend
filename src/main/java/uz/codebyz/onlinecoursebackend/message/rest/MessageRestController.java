package uz.codebyz.onlinecoursebackend.message.rest;

import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import uz.codebyz.onlinecoursebackend.common.ResponseDto;
import uz.codebyz.onlinecoursebackend.message.dto.req.CreateMessageRequestDto;
import uz.codebyz.onlinecoursebackend.message.dto.res.MessageResponseDto;
import uz.codebyz.onlinecoursebackend.message.dto.res.UploadFileResponseDto;
import uz.codebyz.onlinecoursebackend.message.entity.FileType;
import uz.codebyz.onlinecoursebackend.message.service.MessageService;

@RestController
@RequestMapping("/api/messages")
public class MessageRestController {

    private final MessageService messageService;

    public MessageRestController(MessageService messageService) {
        this.messageService = messageService;
    }

    /**
     * 📎 File upload (IMAGE / VIDEO / FILE)
     */
    @PostMapping(value = "/upload", consumes = "multipart/form-data")
    public ResponseDto<UploadFileResponseDto> uploadFile(
            @RequestParam("file") MultipartFile file,
            @RequestParam("fileType") String fileType
    ) {
        FileType type;
        try {
            type = FileType.valueOf(fileType.toUpperCase());
        } catch (IllegalArgumentException e) {
            return new ResponseDto<>(false, "Noto‘g‘ri fileType: " + fileType);
        }

        return messageService.uploadFile(file, type);
    }


    /**
     * ✉️ Xabar yuborish
     */
    @PostMapping("/send")
    public ResponseDto<MessageResponseDto> sendMessage(
            @RequestBody CreateMessageRequestDto dto
    ) {
        return messageService.sendMessage(dto);
    }
}
