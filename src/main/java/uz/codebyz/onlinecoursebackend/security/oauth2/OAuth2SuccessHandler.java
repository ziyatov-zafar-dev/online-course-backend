package uz.codebyz.onlinecoursebackend.security.oauth2;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.OAuth2Error;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import uz.codebyz.onlinecoursebackend.auth.dto.AuthTokensResponse;
import uz.codebyz.onlinecoursebackend.auth.dto.UserResponse;
import uz.codebyz.onlinecoursebackend.common.ApiResponse;
import uz.codebyz.onlinecoursebackend.security.jwt.JwtService;
import uz.codebyz.onlinecoursebackend.user.User;
import uz.codebyz.onlinecoursebackend.user.UserRepository;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Optional;

@Component
public class OAuth2SuccessHandler implements AuthenticationSuccessHandler {

    private final JwtService jwtService;
    private final UserRepository userRepository;
    private final String frontendBaseUrl;
    private final ObjectMapper objectMapper = new ObjectMapper();

    public OAuth2SuccessHandler(JwtService jwtService,
                                UserRepository userRepository,
                                @Value("${frontend.base-url:http://localhost:5173}") String frontendBaseUrl) {
        this.jwtService = jwtService;
        this.userRepository = userRepository;
        this.frontendBaseUrl = frontendBaseUrl;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        try {
            OAuth2User principal = (OAuth2User) authentication.getPrincipal();
            String email = principal != null ? (String) principal.getAttributes().get("email") : null;
            if (email == null) {
                throw new OAuth2AuthenticationException(new OAuth2Error("email_not_found"), "Email topilmadi. Avval ro‘yxatdan o‘ting.");
            }
            Optional<User> optionalUser = userRepository.findByEmail(email);
            if (optionalUser.isEmpty()) {
                throw new OAuth2AuthenticationException(new OAuth2Error("email_not_found"), "Email topilmadi. Avval ro‘yxatdan o‘ting.");
            }
            User user = optionalUser.get();

            String access = jwtService.generateAccessToken(user);
            String refresh = jwtService.generateRefreshToken(user);
            UserResponse userResponse = mapUser(user);
            AuthTokensResponse tokensResponse = new AuthTokensResponse(access, refresh, userResponse);

            String redirectUrl = buildSuccessRedirect(access, refresh, userResponse);
            response.setStatus(HttpServletResponse.SC_FOUND);
            response.setHeader("Location", redirectUrl);
            response.getWriter().write(objectMapper.writeValueAsString(ApiResponse.ok("Muvaffaqiyatli tizimga kirdingiz.", tokensResponse)));
        } catch (OAuth2AuthenticationException ex) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write(objectMapper.writeValueAsString(ApiResponse.error("Email topilmadi. Avval ro‘yxatdan o‘ting.", "EMAIL_NOT_FOUND")));
        } catch (Exception ex) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write(objectMapper.writeValueAsString(ApiResponse.error("OAuth2 login xatosi: " + ex.getMessage(), "OAUTH2_AUTH_ERROR")));
        }
    }

    private UserResponse mapUser(User user) {
        UserResponse res = new UserResponse();
        res.setId(user.getId());
        res.setFirstname(user.getFirstname());
        res.setLastname(user.getLastname());
        res.setUsername(user.getUsername());
        res.setEmail(user.getEmail());
        res.setBirthDate(user.getBirthDate() != null ? user.getBirthDate().toString() : null);
        if (user.getProfile() != null) {
            res.setBio(user.getProfile().getBio());
            res.setWebsite(user.getProfile().getWebsite());
            res.setTelegram(user.getProfile().getTelegram());
            res.setGithub(user.getProfile().getGithub());
            res.setLinkedin(user.getProfile().getLinkedin());
            res.setTwitter(user.getProfile().getTwitter());
            res.setFacebook(user.getProfile().getFacebook());
            res.setInstagram(user.getProfile().getInstagram());
            if (user.getProfile().getImages() != null && !user.getProfile().getImages().isEmpty()) {
                java.util.List<uz.codebyz.onlinecoursebackend.auth.dto.ProfileImageResponse> imgDtos = new java.util.ArrayList<>();
                for (uz.codebyz.onlinecoursebackend.user.UserProfileImage img : user.getProfile().getImages()) {
                    uz.codebyz.onlinecoursebackend.auth.dto.ProfileImageResponse dto = new uz.codebyz.onlinecoursebackend.auth.dto.ProfileImageResponse();
                    dto.setId(img.getId());
                    dto.setUrl(img.getUrl());
                    dto.setOriginalName(img.getOriginalName());
                    dto.setContentType(img.getContentType());
                    dto.setSize(img.getSize());
                    imgDtos.add(dto);
                }
                res.setImages(imgDtos);
            }
        }
        res.setRole(user.getRole());
        res.setStatus(user.getStatus());
        res.setEnabled(user.isEnabled());
        return res;
    }

    private String buildSuccessRedirect(String access, String refresh, UserResponse userResponse) {
        String encodedAccess = URLEncoder.encode(access, StandardCharsets.UTF_8);
        String encodedRefresh = URLEncoder.encode(refresh, StandardCharsets.UTF_8);
        String encodedEmail = userResponse.getEmail() != null ? URLEncoder.encode(userResponse.getEmail(), StandardCharsets.UTF_8) : "";
        return frontendBaseUrl + "/google-auth-success"
                + "?accessToken=" + encodedAccess
                + "&refreshToken=" + encodedRefresh
                + "&email=" + encodedEmail;
    }
}
