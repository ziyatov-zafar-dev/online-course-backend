package uz.codebyz.onlinecoursebackend.auth.dto;

import uz.codebyz.onlinecoursebackend.userDevice.dt.UserDeviceResponse;
import uz.codebyz.onlinecoursebackend.userDevice.entity.UserDevice;

import java.util.List;

public class AuthTokensResponse {
    private String accessToken;
    private String refreshToken;
    private UserResponse user;
    private List<UserDeviceResponse> devices;
    public AuthTokensResponse() {
    }

    public List<UserDeviceResponse> getDevices() {
        return devices;
    }

    public void setDevices(List<UserDeviceResponse> devices) {
        this.devices = devices;
    }

    public AuthTokensResponse(String accessToken, String refreshToken, UserResponse user) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
        this.user = user;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    public UserResponse getUser() {
        return user;
    }

    public void setUser(UserResponse user) {
        this.user = user;
    }
}
