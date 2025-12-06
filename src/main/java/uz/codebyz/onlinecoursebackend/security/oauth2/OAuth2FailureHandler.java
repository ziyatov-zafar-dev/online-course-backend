package uz.codebyz.onlinecoursebackend.security.oauth2;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;
import uz.codebyz.onlinecoursebackend.common.ApiResponse;

import java.io.IOException;

@Component
public class OAuth2FailureHandler implements AuthenticationFailureHandler {

    private final String frontendBaseUrl;
    private final ObjectMapper objectMapper = new ObjectMapper();

    public OAuth2FailureHandler(@Value("${frontend.base-url:http://localhost:5173}") String frontendBaseUrl) {
        this.frontendBaseUrl = frontendBaseUrl;
    }

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
        if (exception instanceof OAuth2AuthenticationException oAuth2Ex
                && "email_not_found".equalsIgnoreCase(oAuth2Ex.getError().getErrorCode())) {
            // Redirect frontendga
            response.setStatus(HttpServletResponse.SC_FOUND);
            response.setHeader("Location", frontendBaseUrl + "/google-email-not-found");
            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
            response.getWriter().write(objectMapper.writeValueAsString(ApiResponse.error("Email topilmadi. Avval ro‘yxatdan o‘ting.", "EMAIL_NOT_FOUND")));
            return;
        }
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.getWriter().write(objectMapper.writeValueAsString(ApiResponse.error(exception.getMessage(), "OAUTH2_AUTH_ERROR")));
    }
}
