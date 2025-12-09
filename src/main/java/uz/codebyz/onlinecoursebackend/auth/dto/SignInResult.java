package uz.codebyz.onlinecoursebackend.auth.dto;

import org.springframework.http.HttpStatus;
import uz.codebyz.onlinecoursebackend.common.ApiResponse;

public class SignInResult {
    private ApiResponse<?> response;
    private HttpStatus status;

    public SignInResult(ApiResponse<?> response, HttpStatus status) {
        this.response = response;
        this.status = status;
    }

    public ApiResponse<?> getResponse() {
        return response;
    }

    public HttpStatus getStatus() {
        return status;
    }
}
