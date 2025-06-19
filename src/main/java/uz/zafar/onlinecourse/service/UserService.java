package uz.zafar.onlinecourse.service;

//import uz.farobiy.lesson_11_backend.dto.ResponseDto;
//import uz.farobiy.lesson_11_backend.dto.form.LoginForm;

import org.springframework.data.domain.Page;
import uz.zafar.onlinecourse.dto.LoginResponseDto;
import uz.zafar.onlinecourse.dto.ResponseDto;
import uz.zafar.onlinecourse.dto.UserResponseDto;
import uz.zafar.onlinecourse.dto.course_dto.res.CourseDto;
import uz.zafar.onlinecourse.dto.form.ChangePasswordForm;
import uz.zafar.onlinecourse.dto.form.EditProfile;
import uz.zafar.onlinecourse.dto.form.LoginForm;
import uz.zafar.onlinecourse.dto.student_dto.res.StudentDto;
import uz.zafar.onlinecourse.dto.teacher_dto.res.TeacherDto;
import uz.zafar.onlinecourse.dto.user_dto.req.SignUpForm;
import uz.zafar.onlinecourse.dto.user_dto.res.UserDto;

import java.util.List;

public interface UserService {
     ResponseDto<LoginResponseDto> signIn(LoginForm form) throws Exception;
     ResponseDto<UserResponseDto> currentUserInfo() throws Exception;
     ResponseDto<?> findAll(int page,int size) ;
     ResponseDto<Page<TeacherDto>> findAllTeachers(int page,int size) throws Exception;
     ResponseDto<Page<StudentDto>> findAllStudents(int page, int size) throws Exception;
     ResponseDto<UserDto> findUserById(Long id) throws Exception;
     ResponseDto<TeacherDto> findByTeacherId(Long teacherId) throws Exception;
     ResponseDto<StudentDto> findByStudentId(Long studentId) throws Exception;
     ResponseDto<?> signUp(SignUpForm form) ;
     ResponseDto<Void> verifyCode(String code);
     ResponseDto<TeacherDto>addTeacher(SignUpForm form);
     ResponseDto<?>changePassword(ChangePasswordForm form);

     ResponseDto<UserDto> findByUsername(String username);
     ResponseDto<List<UserDto>>searchByUsername(String username);
     ResponseDto<List<UserDto>>searchByUsernameAndFirstNameAndLastnameAndEmail(String query);

     ResponseDto<?> changeUsername(String username);

     ResponseDto<?> changeProfile(EditProfile profile);
}
