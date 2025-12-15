package uz.codebyz.onlinecoursebackend.message.rest;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import uz.codebyz.onlinecoursebackend.common.ResponseDto;
import uz.codebyz.onlinecoursebackend.message.dto.req.CreateChatRequestDto;
import uz.codebyz.onlinecoursebackend.message.dto.res.ChatResponseDto;
import uz.codebyz.onlinecoursebackend.message.service.ChatService;
import uz.codebyz.onlinecoursebackend.security.UserPrincipal;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/chats")
public class ChatRestController {
    private final ChatService chatService;

    public ChatRestController(ChatService chatService) {
        this.chatService = chatService;
    }

    @GetMapping("get-my-chats")
    public ResponseDto<List<ChatResponseDto>> getMyChats(@AuthenticationPrincipal UserPrincipal user) {
        return chatService.myChats(user.getUser().getId());
    }

    @GetMapping("/{chatId}/messages")
    public ResponseDto<ChatResponseDto> getAllChatMessages(@PathVariable(value = "chatId") UUID chatId) {
        return chatService.getAllChatMessages(chatId);
    }

    @PatchMapping("/has-chat")
    public ResponseDto<ChatResponseDto> hasChat(@RequestBody CreateChatRequestDto req) {
        return chatService.haveChat(req);
    }

    @DeleteMapping("delete/{chatId}")
    public ResponseDto<ChatResponseDto> deleteChat(@PathVariable("chatId") UUID chatId) {
        return chatService.deleteChat(chatId);
    }

    @PostMapping("create-chat")
    public ResponseDto<ChatResponseDto> deleteChat(@RequestBody CreateChatRequestDto req) {
        return chatService.createChat(req);
    }
}
