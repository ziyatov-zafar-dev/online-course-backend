package uz.codebyz.onlinecoursebackend.admin.category.mapper;

import uz.codebyz.onlinecoursebackend.admin.category.promoCodeDtos.AdminCourseAndPromoCodeResponseDto;
import uz.codebyz.onlinecoursebackend.admin.course.mapper.AdminCourseMapper;
import uz.codebyz.onlinecoursebackend.auth.dto.UserResponse;
import uz.codebyz.onlinecoursebackend.promocode.entity.PromoCode;
import uz.codebyz.onlinecoursebackend.user.User;
import uz.codebyz.onlinecoursebackend.userDevice.service.UserDeviceService;

import java.time.format.DateTimeFormatter;

public class AdminPromoCodeMapper {
    public static AdminCourseAndPromoCodeResponseDto toDto(PromoCode promoCode, UserDeviceService deviceService) {
        AdminCourseAndPromoCodeResponseDto response = new AdminCourseAndPromoCodeResponseDto();
        response.setPromoCodeId(promoCode.getId());


        response.setCode(promoCode.getCode());
        response.setDiscountPercent(promoCode.getDiscountPercent());
        response.setFixedAmount(promoCode.getFixedAmount());
        response.setMaxUsage(promoCode.getMaxUsage());
        response.setUserCount(promoCode.getUserCount());
        response.setValidFrom(promoCode.getValidFrom());
        response.setValidUntil(promoCode.getValidUntil());
        response.setActive(promoCode.getActive());
        response.setCreated(promoCode.getCreated());
        response.setUpdated(promoCode.getUpdated());


        if (promoCode.getUser() != null && promoCode.getCourse() != null) {
            UserResponse res = mapToUser(promoCode.getUser(),deviceService);
            response.setUser(res);
            response.setCourse(AdminCourseMapper.toDto(promoCode.getCourse()));
        }
        return response;
    }

/**/

    public static UserResponse mapToUser(User user, UserDeviceService deviceService) {
        UserResponse res = new UserResponse();
        res.setId(user.getId());
        res.setFirstname(user.getFirstname());
        // 🔥 Online yoki offline aniqlaymiz
        res.setOnline(deviceService.isUserOnline(user.getId()));

        // 🔥 Last seen hisoblaymiz
        res.setLastOnline(deviceService.getLastSeen(user.getId()));
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

}
