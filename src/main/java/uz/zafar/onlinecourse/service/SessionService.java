package uz.zafar.onlinecourse.service;

import uz.zafar.onlinecourse.dto.ResponseDto;
import uz.zafar.onlinecourse.dto.ResponseDtoNotData;

import java.util.List;
import java.util.Map;

public interface SessionService {

    public ResponseDtoNotData saveSession(String userId, String token, String userAgent, String ipAddress) ;
    public ResponseDtoNotData deleteSession(String userId, String token) ;
    public ResponseDto<List<Map<String, String>>> getSessions(String userId);
}
