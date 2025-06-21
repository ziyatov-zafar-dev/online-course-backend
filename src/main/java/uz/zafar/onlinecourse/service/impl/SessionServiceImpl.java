package uz.zafar.onlinecourse.service.impl;

import org.springframework.stereotype.Service;
import uz.zafar.onlinecourse.dto.ResponseDto;
import uz.zafar.onlinecourse.dto.ResponseDtoNotData;
import uz.zafar.onlinecourse.service.SessionService;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Service
public class SessionServiceImpl implements SessionService {

    // <userId, List of sessions>
    private final Map<String, List<Map<String, String>>> sessionMap = new ConcurrentHashMap<>();

    @Override
    public ResponseDtoNotData saveSession(String userId, String token, String userAgent, String ipAddress) {
        Map<String, String> session = new HashMap<>();
        session.put("token", token);
        session.put("userAgent", userAgent);
        session.put("ipAddress", ipAddress);
        session.put("loginTime", new Date().toString());

        // Avvalgi sessiyalarni olib, yangisini qo‘shamiz
        sessionMap.computeIfAbsent(userId, k -> new ArrayList<>()).add(session);

        return new ResponseDtoNotData(true, "Session saqlandi");
    }

    @Override
    public ResponseDtoNotData deleteSession(String userId, String token) {
        List<Map<String, String>> sessions = sessionMap.get(userId);
        if (sessions != null) {
            sessions.removeIf(session -> token.equals(session.get("token")));
            return new ResponseDtoNotData(true, "Session o‘chirildi");
        }
        return new ResponseDtoNotData(false, "Session topilmadi");
    }

    @Override
    public ResponseDto<List<Map<String, String>>> getSessions(String userId) {
        List<Map<String, String>> sessions = sessionMap.getOrDefault(userId, Collections.emptyList());
        return new ResponseDto<>(true, "Success", sessions);
    }
}
