package uz.codebyz.onlinecoursebackend.security.oauth2;

import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.OAuth2Error;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import uz.codebyz.onlinecoursebackend.user.User;
import uz.codebyz.onlinecoursebackend.user.UserRepository;

import java.util.Map;
import java.util.Set;

@Service
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    private final UserRepository userRepository;

    public CustomOAuth2UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = super.loadUser(userRequest);
        String email = (String) oAuth2User.getAttributes().get("email");
        if (email == null) {
            throw new OAuth2AuthenticationException(new OAuth2Error("email_not_found"), "Email topilmadi. Avval ro‘yxatdan o‘ting.");
        }
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new OAuth2AuthenticationException(new OAuth2Error("email_not_found"), "Email topilmadi. Avval ro‘yxatdan o‘ting."));
        Map<String, Object> attributes = oAuth2User.getAttributes();
        return new DefaultOAuth2User(
                Set.of(() -> "ROLE_" + user.getRole().name()),
                attributes,
                "email");
    }
}
