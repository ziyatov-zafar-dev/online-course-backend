package uz.codebyz.onlinecoursebackend.auth.service.impl;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import uz.codebyz.onlinecoursebackend.auth.dto.TelegramAuthorizationRequest;
import uz.codebyz.onlinecoursebackend.auth.dto.TelegramAuthorizationResponse;
import uz.codebyz.onlinecoursebackend.auth.service.TelegramAuthorizationService;
import uz.codebyz.onlinecoursebackend.common.ResponseDto;
import uz.codebyz.onlinecoursebackend.user.User;
import uz.codebyz.onlinecoursebackend.user.UserRepository;

@Service
public class TelegramAuthorizationServiceImpl
        implements TelegramAuthorizationService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public TelegramAuthorizationServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public ResponseDto<TelegramAuthorizationResponse> authorize(
            TelegramAuthorizationRequest request) {

        // 1️⃣ USER TOPAMIZ (email yoki username)
        User user = userRepository.findByEmail(request.getLogin())
                .or(() -> userRepository.findByUsername(request.getLogin()))
                .orElse(null);

        if (user == null) {
            return ResponseDto.error("Foydalanuvchi topilmadi");
        }

        // 2️⃣ PAROL TEKSHIRAMIZ
        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            return ResponseDto.error("Login yoki parol noto‘g‘ri");
        }

        // 3️⃣ CHAT ID BOSHQA USERGA TEGISHLI EMASMI
        if (user.getChatId() != null &&
                !user.getChatId().equals(request.getChatId())) {

            return ResponseDto.error(
                    "Bu akkaunt boshqa Telegram bilan bog‘langan"
            );
        }

        // 4️⃣ CHAT ID SAQLAYMIZ
        user.setChatId(request.getChatId());
        userRepository.save(user);

        // 5️⃣ RESPONSE
        TelegramAuthorizationResponse response =
                new TelegramAuthorizationResponse(user.getRole().name());

        return ResponseDto.ok(
                "Telegram akkaunt muvaffaqiyatli bog‘landi",
                response
        );
    }
}
