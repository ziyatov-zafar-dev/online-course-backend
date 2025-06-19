package uz.zafar.onlinecourse.helper;

import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.context.SecurityContextHolder;
import uz.zafar.onlinecourse.db.domain.User;
//import uz.farobiy.lesson_11_backend.db.domain.User;
@Log4j2
public class SecurityHelper {
    public static User getCurrentUser(){
        try {
            return (User) SecurityContextHolder
                    .getContext()
                    .getAuthentication()
                    .getPrincipal();
        }catch (Exception e){
            log.error(e);
            return null;
        }
    }
}
