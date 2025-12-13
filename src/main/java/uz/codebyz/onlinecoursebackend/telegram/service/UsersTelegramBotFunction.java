package uz.codebyz.onlinecoursebackend.telegram.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import uz.codebyz.onlinecoursebackend.common.ResponseDto;
import uz.codebyz.onlinecoursebackend.course.entity.Course;
import uz.codebyz.onlinecoursebackend.course.repository.CourseRepository;
import uz.codebyz.onlinecoursebackend.telegram.bot.TelegramBot;
import uz.codebyz.onlinecoursebackend.telegram.bot.kyb.UserKyb;
import uz.codebyz.onlinecoursebackend.telegram.bot.msg.UserMsg;
import uz.codebyz.onlinecoursebackend.telegram.entity.BotUserStatus;
import uz.codebyz.onlinecoursebackend.telegram.entity.EventCode;
import uz.codebyz.onlinecoursebackend.telegram.entity.TelegramUser;
import uz.codebyz.onlinecoursebackend.user.UserRole;

import java.util.List;
import java.util.Map;

@Service
public class UsersTelegramBotFunction {
    private final UserKyb kyb;
    private final CourseRepository courseRepository;
    private final UserMsg msg;
    @Value("${auth-frontend.login-url}")
    private String frontendBaseUrl;
    private final TelegramBot bot;
    private final BotUserService userService;

    public UsersTelegramBotFunction(TelegramBot bot, BotUserService userService, UserKyb kyb, CourseRepository courseRepository, UserMsg userMsg) {
        this.bot = bot;
        this.userService = userService;
        this.kyb = kyb;
        this.courseRepository = courseRepository;
        this.msg = userMsg;
    }

    /* ================= START ================= */

    public void start(Long chatId, String firstName, String lastName, String username) {
        ResponseDto<Void> checkUser = userService.checkUser(chatId);
        if (checkUser.isSuccess()) {

            ResponseDto<TelegramUser> checkTelegramUser = userService.checkTelegramUser(chatId);
            TelegramUser user;
            if (checkTelegramUser.isSuccess()) {
                System.err.println("Telegram User mavjud");
                if (checkTelegramUser.getData().getStatus() == BotUserStatus.BLOCK) {
                    bot.sendMessage(chatId, checkTelegramUser.getMessage(), true);
                    return;
                } else {
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
            if (userService.getRole(user.getChatId()) == UserRole.STUDENT) {
                bot.sendMessage(user.getChatId(), "Asosiy menyudasiz", kyb.menu());
                user.setEventCode(EventCode.MENU);
                user = userService.save(user);
                return;
            } else {
                bot.sendMessage(
                        chatId,
                        """
                                ❌ <b>Kirish rad etildi</b>
                                
                                ⚠️ Ushbu bot <b>faqat talabalarga</b> mo‘ljallangan.
                                
                                Agar siz <b>o‘qituvchi</b> yoki <b>administrator</b> bo‘lsangiz,
                                iltimos, o‘zingizga mos botdan foydalaning.""",
                        true
                );
                return;
            }
        }
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
            bot.sendMessageWithWebAppStart(
                    chatId,
                    text,
                    loginUrl, "🔐 Shahsni tasdiqlash"
            );
        } catch (Exception e) {
            bot.sendMessage(chatId, e.getMessage());
        }
    }

    public void menu(TelegramUser user, String data, Map<String, Object> callback, Integer messageId) {
        switch (data) {

            case "all_courses" -> {
                List<Course> courses = courseRepository.getAllCoursesBot();
                if (courses.isEmpty()) {
                    bot.alertMessage(callback,
                            """
                                    🚀 CodeByZ Academy
                                    
                                    📚 Kurslar hozircha mavjud emas.
                                    ⏳ Tez orada yangi kurslar qo‘shiladi!""");
                    return;
                }
                bot.editMessageText(
                        user.getChatId(),
                        messageId, msg.aboutAllCourses(),
                        kyb.getAllCourses(courses, null)
                );

            }

            case "my_certificates", "all_payment" -> bot.alertMessage(callback, "⚠️ Bu funksiya hozircha mavjud emas");
            default -> bot.alertMessage(callback, "❌ Noma’lum buyruq");
        }

    }
}
