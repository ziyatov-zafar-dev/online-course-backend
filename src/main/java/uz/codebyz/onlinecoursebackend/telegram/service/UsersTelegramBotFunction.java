package uz.codebyz.onlinecoursebackend.telegram.service;

import org.springframework.stereotype.Service;
import uz.codebyz.onlinecoursebackend.common.ResponseDto;
import uz.codebyz.onlinecoursebackend.telegram.bot.TelegramBot;
import uz.codebyz.onlinecoursebackend.telegram.dto.ButtonDto;
import uz.codebyz.onlinecoursebackend.telegram.dto.ButtonType;
import uz.codebyz.onlinecoursebackend.telegram.entity.BotUserStatus;
import uz.codebyz.onlinecoursebackend.telegram.entity.EventCode;
import uz.codebyz.onlinecoursebackend.telegram.entity.TelegramUser;

import java.util.List;

@Service
public class UsersTelegramBotFunction {

    private final TelegramBot bot;
    private final BotUserService userService;

    public UsersTelegramBotFunction(TelegramBot bot, BotUserService userService) {
        this.bot = bot;
        this.userService = userService;
    }

    /* ================= START ================= */

    public void start(Long chatId, String firstName, String lastName, String username) {
        ResponseDto<Void> checkUser = userService.checkUser(chatId);
        if (checkUser.isSuccess()) {
            ResponseDto<TelegramUser> checkTelegramUser = userService.checkTelegramUser(chatId);
            TelegramUser user;
            if (checkTelegramUser.isSuccess()) {
                if (checkTelegramUser.getData().getStatus() == BotUserStatus.BLOCK) {
                    bot.sendMessage(chatId, checkTelegramUser.getMessage(), true);
                    return;
                } else {
                    user = checkTelegramUser.getData();
                    user.setEventCode(EventCode.MENU);
                    user = userService.save(user);
                }
            } else {
                if (!lastName.isEmpty()) {
                    firstName = firstName.concat(" " + lastName);
                }
                ResponseDto<TelegramUser> savedUser = userService.createUser(chatId, firstName, username);
                if (!savedUser.isSuccess()) {
                    bot.sendMessage(chatId, "Kutilmagan xatolik");
                    return;
                }
                user = savedUser.getData();
            }
            menu(user);
            return;
        }

        String loginUrl =
                "https://online-course-tg-bot-for-users-up37-5p5csufu1.vercel.app"
                        + "/auth/telegram?chatId=" + chatId;
        String text = """
                👋 Assalomu alaykum! <b>%s</b>
                
                Bu botdan foydalanish uchun shahsingizni tasdiqlashingiz kerak
                """.formatted(firstName + (lastName.isEmpty() ? "" : " " + lastName));
        bot.sendMessage(
                chatId,
                text,
                List.of(
                        List.of(
                                new ButtonDto(
                                        "🔐 Shahsni tasdiqlash",
                                        ButtonType.URL,
                                        loginUrl
                                )
                        ),
                        List.of(
                                new ButtonDto("✅ Tasdiqlash", ButtonType.INLINE, "confirm_user")
                        )
                )
        );
    }

    public void menu(TelegramUser user) {
        bot.sendMessage(user.getChatId(), "Asosiy menyudasiz");
    }
}
