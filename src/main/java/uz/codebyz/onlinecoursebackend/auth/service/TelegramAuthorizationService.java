package uz.codebyz.onlinecoursebackend.auth.service;

import uz.codebyz.onlinecoursebackend.auth.dto.TelegramAuthorizationRequest;
import uz.codebyz.onlinecoursebackend.auth.dto.TelegramAuthorizationResponse;
import uz.codebyz.onlinecoursebackend.common.ResponseDto;

public interface TelegramAuthorizationService {

    ResponseDto<TelegramAuthorizationResponse>
    authorize(TelegramAuthorizationRequest request);

}
