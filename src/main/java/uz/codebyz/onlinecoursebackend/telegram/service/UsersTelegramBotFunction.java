package uz.codebyz.onlinecoursebackend.telegram.service;

import org.springframework.beans.factory.annotation.Value;
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
    @Value("${auth-frontend.login-url}")
    private String frontendBaseUrl;
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
            System.out.println("User mavjud");
            ResponseDto<TelegramUser> checkTelegramUser = userService.checkTelegramUser(chatId);
            TelegramUser user;
            if (checkTelegramUser.isSuccess()) {
                System.err.println("Telegram User mavjud");
                if (checkTelegramUser.getData().getStatus() == BotUserStatus.BLOCK) {
                    System.err.println("User blok");
                    bot.sendMessage(chatId, checkTelegramUser.getMessage(), true);
                    return;
                } else {
                    System.err.println("User blok emas");
                    user = checkTelegramUser.getData();
                    user.setEventCode(EventCode.MENU);
                    user = userService.save(user);
                }
            } else {
                System.err.println("User mavjud emas");
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
        System.err.println("User mavjud emas");
        try {
            String loginUrl =
                    frontendBaseUrl
                            + "/auth/telegram?chatId=" + chatId;
//            String text;
//            try {
//                text = """
//                        👋 Assalomu alaykum! <b>%s</b>
//
//                        Bu botdan foydalanish uchun shahsingizni tasdiqlashingiz kerak
//                        """.formatted(firstName + (lastName.isEmpty() ? "" : " " + lastName));
//            } catch (Exception e) {
//                text = """
//                        👋 Assalomu alaykum! <b>%s</b>
//
//                        Bu botdan foydalanish uchun shahsingizni tasdiqlashingiz kerak
//                        """.formatted(firstName);
//
//            }
            String text = """
                    👋 Assalomu alaykum! <b>%s</b>
                    
                    Bu botdan foydalanish uchun shahsingizni tasdiqlashingiz kerak
                    """.formatted(firstName + ((lastName == null || lastName.isEmpty()) ? "" : " " + lastName));
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
        } catch (Exception e) {
            bot.sendMessage(chatId, e.getMessage());
        }
    }

    public void menu(TelegramUser user) {
        bot.sendMessage(user.getChatId(), "Asosiy menyudasiz");
    }
}
