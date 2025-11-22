package uz.zafar.onlinecourse.bot;

import org.springframework.stereotype.Service;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.io.File;
@Service
public class TelegramBot extends TelegramLongPollingBot {

    @Override
    public void onUpdateReceived(Update update) {
        File file = new java.io.File("uploads/img.png");
        SendMessage message = new SendMessage();
        message.setChatId(update.getMessage().getChatId().toString());
        message.setText(file.getAbsolutePath());
        try {
            execute(message);
        } catch (TelegramApiException e) {
            System.out.println(e.getMessage());
        }

    }

    @Override
    public String getBotToken() {
        return "8321636533:AAGfimEVdryNlVjCYOI5Y8x21h3kSTmaaDA";
    }

    @Override
    public String getBotUsername() {
        return "@CodeByZBot";
    }
}
