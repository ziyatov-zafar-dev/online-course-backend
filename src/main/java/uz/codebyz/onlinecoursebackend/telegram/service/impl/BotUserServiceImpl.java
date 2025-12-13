package uz.codebyz.onlinecoursebackend.telegram.service.impl;

import org.springframework.stereotype.Service;
import uz.codebyz.onlinecoursebackend.common.ResponseDto;
import uz.codebyz.onlinecoursebackend.helper.CurrentTime;
import uz.codebyz.onlinecoursebackend.telegram.entity.BotUserStatus;
import uz.codebyz.onlinecoursebackend.telegram.entity.TelegramUser;
import uz.codebyz.onlinecoursebackend.telegram.repository.TelegramUserRepository;
import uz.codebyz.onlinecoursebackend.telegram.service.BotUserService;
import uz.codebyz.onlinecoursebackend.user.UserRepository;

import java.util.Optional;

@Service
public class BotUserServiceImpl implements BotUserService {
    private final UserRepository userRepository;
    private final TelegramUserRepository telegramUserRepository;

    public BotUserServiceImpl(UserRepository userRepository, TelegramUserRepository telegramUserRepository) {
        this.userRepository = userRepository;
        this.telegramUserRepository = telegramUserRepository;
    }

    @Override
    public ResponseDto<Void> checkUser(Long chatId) {
        if (userRepository.findByChatId(chatId).isPresent()) {
            return new ResponseDto<>(true, "Success");
        } else {
            return new ResponseDto<>(false, "Fail");
        }
    }

    @Override
    public ResponseDto<TelegramUser> checkTelegramUser(Long chatId) {
        Optional<TelegramUser> uOp = telegramUserRepository.findByChatId(chatId);
        if (uOp.isPresent()) {
            TelegramUser telegramUser = uOp.get();
            if (telegramUser.getStatus() == BotUserStatus.BLOCK) {
                return new ResponseDto<>(false,
                        "Kechirasiz, siz botdan foydalana olmaysiz siz admin tomonidan bloklangansiz", telegramUser
                );
            }
            return new ResponseDto<>(true, "Success", telegramUser);
        } else return new ResponseDto<>(false, "Fail", null);
    }

    @Override
    public ResponseDto<TelegramUser> createUser(Long chatId, String nickname, String username) {
        try {
            TelegramUser user = new TelegramUser();
            user.setNickname(nickname);
            user.setUsername(username);
            user.setChatId(chatId);
            user.setCreatedAt(CurrentTime.currentTime());
            return new ResponseDto<>(true, "Success", telegramUserRepository.save(user));

        } catch (Exception e) {
            return new ResponseDto<>(false, e.getMessage());
        }
    }

    @Override
    public TelegramUser save(TelegramUser user) {
        return telegramUserRepository.save(user);
    }

    @Override
    public ResponseDto<TelegramUser> getUser(Long chatId) {
        Optional<TelegramUser> checkUser = telegramUserRepository.findByChatId(chatId);
        if (checkUser.isPresent()) {
            TelegramUser telegramUser = checkUser.get();
            return new ResponseDto<>(true, "Success", telegramUser);
        }
        return new ResponseDto<>(false, "Fail");
    }
}
