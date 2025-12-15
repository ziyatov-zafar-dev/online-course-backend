package uz.codebyz.onlinecoursebackend.message.service;

import uz.codebyz.onlinecoursebackend.common.ResponseDto;
import uz.codebyz.onlinecoursebackend.message.dto.request.ChatCreateDto;
import uz.codebyz.onlinecoursebackend.message.dto.response.ChatMemberResponseDto;
import uz.codebyz.onlinecoursebackend.message.dto.response.ChatResponseDto;

import java.util.List;
import java.util.UUID;

public interface ChatService {
    ResponseDto<ChatResponseDto> createPrivateChat(UUID requesterId, UUID otherUserId);

    ResponseDto<ChatResponseDto> createGroupChat(UUID creatorId, ChatCreateDto dto);

    ResponseDto<List<ChatMemberResponseDto>> getGroupMembers(UUID chatId, UUID requesterId);

    ResponseDto<List<ChatResponseDto>> getMyGroups(UUID userId);

    ResponseDto<Void> addMemberToGroup(UUID chatId, UUID requesterId, UUID newMemberId);

    ResponseDto<List<ChatResponseDto>> searchChats(String query, UUID userId);

}
