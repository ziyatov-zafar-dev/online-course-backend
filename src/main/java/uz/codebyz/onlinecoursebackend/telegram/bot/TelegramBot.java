package uz.codebyz.onlinecoursebackend.telegram.bot;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import uz.codebyz.onlinecoursebackend.telegram.config.TelegramProperties;
import uz.codebyz.onlinecoursebackend.telegram.dto.ButtonDto;
import uz.codebyz.onlinecoursebackend.telegram.dto.ButtonType;

import java.util.*;

@Component

public class TelegramBot {
    private static final Logger log =
            LoggerFactory.getLogger(TelegramBot.class);
    private final TelegramProperties props;
    private final RestTemplate restTemplate = new RestTemplate();

    public TelegramBot(TelegramProperties props) {
        this.props = props;
    }

    /* ================= PUBLIC API ================= */

    public void sendMessage(Long chatId, String text) {
        send(chatId, text, null, false);
    }

    public void sendMessage(Long chatId, String text, List<List<ButtonDto>> buttons) {
        send(chatId, text, buttons, false);
    }

    /**
     * 🔥 YANGI: keyboardni olib tashlash
     */
    public void sendMessage(Long chatId, String text, boolean removeKeyboard) {
        send(chatId, text, null, removeKeyboard);
    }

    /* ================= CORE SEND ================= */

    private void send(
            Long chatId,
            String text,
            List<List<ButtonDto>> buttons,
            boolean removeKeyboard
    ) {

        Map<String, Object> body = new HashMap<>();
        body.put("chat_id", chatId);
        body.put("text", text);
        body.put("parse_mode", "HTML");
        body.put("disable_web_page_preview", true);

        if (removeKeyboard) {
            body.put("reply_markup", Map.of("remove_keyboard", true));
        } else if (buttons != null && !buttons.isEmpty()) {
            body.put("reply_markup", buildKeyboard(buttons));
        }

        post("/sendMessage", body);
    }

    /* ================= DELETE MESSAGE ================= */

    public void deleteMessage(Long chatId, Integer messageId) {
        try {

            Map<String, Object> body = new HashMap<>();
            body.put("chat_id", chatId);
            body.put("message_id", messageId);

            post("/deleteMessage", body);
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }

    /* ================= EDIT MESSAGE ================= */

    public void editMessageText(
            Long chatId,
            Integer messageId,
            String newText,
            List<List<ButtonDto>> buttons
    ) {

        Map<String, Object> body = new HashMap<>();
        body.put("chat_id", chatId);
        body.put("message_id", messageId);
        body.put("text", newText);
        body.put("parse_mode", "HTML");
        body.put("disable_web_page_preview", true);

        if (buttons != null && !buttons.isEmpty()) {
            body.put("reply_markup", buildKeyboard(buttons));
        }

        post("/editMessageText", body);
    }

    /* ================= KEYBOARD BUILDER ================= */

    private Map<String, Object> buildKeyboard(List<List<ButtonDto>> rows) {
        boolean hasInline = rows.stream()
                .flatMap(List::stream)
                .anyMatch(b -> b.getType() != ButtonType.TEXT);
        return hasInline ? inlineKeyboard(rows) : replyKeyboard(rows);
    }

    /* ================= REPLY KEYBOARD ================= */

    private Map<String, Object> replyKeyboard(List<List<ButtonDto>> rows) {

        List<List<Map<String, Object>>> keyboard = new ArrayList<>();

        for (List<ButtonDto> row : rows) {
            List<Map<String, Object>> r = new ArrayList<>();
            for (ButtonDto b : row) {
                r.add(Map.of("text", b.getText()));
            }
            keyboard.add(r);
        }

        return Map.of(
                "keyboard", keyboard,
                "resize_keyboard", true,
                "one_time_keyboard", false
        );
    }

    /* ================= INLINE KEYBOARD ================= */

    private Map<String, Object> inlineKeyboard(List<List<ButtonDto>> rows) {

        List<List<Map<String, Object>>> keyboard = new ArrayList<>();

        for (List<ButtonDto> row : rows) {
            List<Map<String, Object>> r = new ArrayList<>();

            for (ButtonDto b : row) {
                Map<String, Object> btn = new HashMap<>();
                btn.put("text", b.getText());

                if (b.getType() == ButtonType.URL) {
                    btn.put("url", b.getValue());
                } else {
                    btn.put("callback_data", b.getValue());
                }
                r.add(btn);
            }
            keyboard.add(r);
        }

        return Map.of("inline_keyboard", keyboard);
    }

    /* ================= HTTP ================= */

    private void post(String method, Map<String, Object> body) {
        String url = "https://api.telegram.org/bot"
                + props.getUsers().getBot().getToken()
                + method;

        restTemplate.postForObject(url, body, String.class);
    }

    @SuppressWarnings("unchecked")
    public void alertMessage(Map<String, Object> callback, String alertMessageText) {

        try {
            // callback_query.id ni olish
            String callbackQueryId = (String) callback.get("id");

            Map<String, Object> body = new HashMap<>();
            body.put("callback_query_id", callbackQueryId);
            body.put("text", alertMessageText);
            body.put("show_alert", true); // 🔥 popup (alert)
            post("/answerCallbackQuery", body);
            log.info("Alert sent to callbackQueryId={}", callbackQueryId);

        } catch (Exception e) {
            log.error("Error while sending alertMessage: {}", e.getMessage(), e);
        }
    }

    public void sendPhoto(
            Long chatId,
            String photoUrl,
            String caption,
            List<List<ButtonDto>> buttons
    ) {

        Map<String, Object> body = new HashMap<>();
        body.put("chat_id", chatId);
        body.put("photo", photoUrl);
        body.put("caption", caption);
        body.put("parse_mode", "HTML");

        if (buttons != null && !buttons.isEmpty()) {
            body.put("reply_markup", buildKeyboard(buttons));
        }

        post("/sendPhoto", body);
    }


}
