package uz.codebyz.onlinecoursebackend.telegram.service;

import org.springframework.stereotype.Service;
import uz.codebyz.onlinecoursebackend.common.ResponseDto;
import uz.codebyz.onlinecoursebackend.telegram.bot.TelegramBot;
import uz.codebyz.onlinecoursebackend.telegram.dto.TelegramUpdateData;
import uz.codebyz.onlinecoursebackend.telegram.entity.EventCode;
import uz.codebyz.onlinecoursebackend.telegram.entity.TelegramUser;
import uz.codebyz.onlinecoursebackend.telegram.util.TelegramUpdateExtractor;

import java.util.Map;

@Service
public class TelegramUsersService {

    private final UsersTelegramBotFunction functions;
    private final BotUserService botUserService;
    private final TelegramBot telegramBot;

    public TelegramUsersService(UsersTelegramBotFunction functions, BotUserService botUserService, TelegramBot telegramBot) {
        this.functions = functions;
        this.botUserService = botUserService;
        this.telegramBot = telegramBot;
    }

    @SuppressWarnings("unchecked")
    public void handleUpdate(Map<String, Object> update) {
        TelegramUpdateData updateData = TelegramUpdateExtractor.extract(update);
        /* ================= CALLBACK QUERY ================= */
        if (updateData.getType() == TelegramUpdateData.MessageType.CALLBACK) {

            Map<String, Object> callback =
                    (Map<String, Object>) update.get("callback_query");

            String data = updateData.getCallbackData();
            if (data.equals("confirm_user")) {
                telegramBot.deleteMessage(updateData.getChatId(), updateData.getMessageId());
                functions.start(updateData.getChatId(), updateData.getFirstName(), updateData.getLastName(), updateData.getUsername());
            } else {
us
            }
        }

        /* ================= MESSAGE ================= */
        if (!update.containsKey("message")) return;

        Map<String, Object> message =
                (Map<String, Object>) update.get("message");

        String text = (String) message.get("text");
        if (text == null) return;

        Map<String, Object> chat =
                (Map<String, Object>) message.get("chat");

        Long chatId = Long.valueOf(chat.get("id").toString());

        /* ===== COMMAND DISPATCH ===== */
        if (text.equals("/start")) {
            functions.start(chatId, updateData.getFirstName(), updateData.getLastName(), updateData.getUsername());
        } else {
            ResponseDto<TelegramUser> checkUser = botUserService.getUser(chatId);
            if (!checkUser.isSuccess()) return;
            TelegramUser user = checkUser.getData();
            EventCode eventCode = user.getEventCode();
            if (eventCode == EventCode.MENU) {
                functions.menu(user);
            }
        }
    }

}
