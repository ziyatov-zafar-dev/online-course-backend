package uz.zafar.onlinecourse.service;

import uz.zafar.onlinecourse.dto.ResponseDto;

import java.io.File;

public interface EmailService {
    ResponseDto<Void>sendSimpleMessage(String to, String subject, String text);
    ResponseDto<Void>sendCode(String toEmail, String code);
    public ResponseDto<Void> sendFile(String to, String subject, File file);
}
