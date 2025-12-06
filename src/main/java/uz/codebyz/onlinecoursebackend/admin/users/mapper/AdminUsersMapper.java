package uz.codebyz.onlinecoursebackend.admin.users.mapper;

import org.springframework.data.domain.Page;
import uz.codebyz.onlinecoursebackend.admin.users.userDto.AdminUserResponseDto;
import uz.codebyz.onlinecoursebackend.user.User;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

public class AdminUsersMapper {
    public static AdminUserResponseDto toDto(User user) {
        AdminUserResponseDto res = new AdminUserResponseDto();
        res.setId(user.getId());
        res.setFirstname(user.getFirstname());
        res.setLastname(user.getLastname());
        res.setUsername(user.getUsername());
        res.setEmail(user.getEmail());
        res.setBirthDate(user.getBirthDate() == null ? "Mavjud emas" :
                user.getBirthDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
        if (user.getProfile() != null) {
            res.setBio(user.getProfile().getBio());
            res.setWebsite(user.getProfile().getWebsite());
            res.setTelegram(user.getProfile().getTelegram());
            res.setGithub(user.getProfile().getGithub());
            res.setLinkedin(user.getProfile().getLinkedin());
            res.setTwitter(user.getProfile().getTwitter());
            res.setFacebook(user.getProfile().getFacebook());
            res.setInstagram(user.getProfile().getInstagram());
        }
        res.setRole(user.getRole());
        res.setStatus(user.getStatus());
        res.setEnabled(user.isEnabled());
        res.setTeacherId(user.getTeacher() != null ? user.getTeacher().getId() : null);
        res.setStudentId(user.getStudent() != null ? user.getStudent().getId() : null);
        return res;
    }

    public static List<AdminUserResponseDto> toDto(List<User> users) {
        return users.stream().map(
                AdminUsersMapper::toDto
        ).collect(Collectors.toList());
    }

    public static Page<AdminUserResponseDto> toDto(Page<User> users) {
        return users.map(AdminUsersMapper::toDto);
    }
}
