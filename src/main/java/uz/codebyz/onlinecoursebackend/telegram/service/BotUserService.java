package uz.codebyz.onlinecoursebackend.telegram.service;

import uz.codebyz.onlinecoursebackend.common.ResponseDto;
import uz.codebyz.onlinecoursebackend.telegram.entity.TelegramUser;
import uz.codebyz.onlinecoursebackend.user.UserRole;


public interface BotUserService {

    public ResponseDto<Void> checkUser(Long chatId) ;
    ResponseDto<TelegramUser>checkTelegramUser(Long chatId);
    ResponseDto<TelegramUser> createUser(Long chatId,String nickname,String username);

    TelegramUser save(TelegramUser user);

    ResponseDto<TelegramUser> getUser(Long chatId);

    UserRole getRole(Long chatId);
}
