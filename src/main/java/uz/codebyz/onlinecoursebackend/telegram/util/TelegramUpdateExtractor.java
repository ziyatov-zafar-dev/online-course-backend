package uz.codebyz.onlinecoursebackend.telegram.util;

import uz.codebyz.onlinecoursebackend.telegram.dto.TelegramUpdateData;
import uz.codebyz.onlinecoursebackend.telegram.dto.TelegramUpdateData.MessageType;

import java.util.Map;

@SuppressWarnings("unchecked")
public class TelegramUpdateExtractor {

    public static TelegramUpdateData extract(Map<String, Object> update) {

        TelegramUpdateData data = new TelegramUpdateData();

        /* ===== CALLBACK QUERY ===== */
        if (update.containsKey("callback_query")) {

            Map<String, Object> callback = (Map<String, Object>) update.get("callback_query");
            Map<String, Object> from = (Map<String, Object>) callback.get("from");
            Map<String, Object> message = (Map<String, Object>) callback.get("message");
            Map<String, Object> chat = (Map<String, Object>) message.get("chat");

            data.setType(MessageType.CALLBACK);
            data.setCallbackData((String) callback.get("data"));

            fillUser(data, from);
            data.setChatId(Long.valueOf(chat.get("id").toString()));

            return data;
        }

        /* ===== TEXT MESSAGE ===== */
        if (update.containsKey("message")) {

            Map<String, Object> message = (Map<String, Object>) update.get("message");
            Map<String, Object> chat = (Map<String, Object>) message.get("chat");
            Map<String, Object> from = (Map<String, Object>) message.get("from");

            data.setType(MessageType.TEXT);
            data.setText((String) message.get("text"));
            data.setChatId(Long.valueOf(chat.get("id").toString()));

            fillUser(data, from);

            return data;
        }

        data.setType(MessageType.UNKNOWN);
        return data;
    }

    private static void fillUser(TelegramUpdateData data, Map<String, Object> from) {

        if (from == null) return;

        data.setUserId(Long.valueOf(from.get("id").toString()));
        data.setUsername((String) from.get("username"));
        data.setFirstName((String) from.get("first_name"));
        data.setLastName((String) from.get("last_name"));
    }
}
