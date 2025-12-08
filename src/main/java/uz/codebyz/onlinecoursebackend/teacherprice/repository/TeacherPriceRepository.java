package uz.codebyz.onlinecoursebackend.teacherprice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import uz.codebyz.onlinecoursebackend.teacherprice.entity.TeacherPrice;

public interface TeacherPriceRepository extends JpaRepository<TeacherPrice, Short> {

}
