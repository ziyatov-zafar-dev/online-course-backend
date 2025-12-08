package uz.codebyz.onlinecoursebackend.userDevice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import uz.codebyz.onlinecoursebackend.userDevice.entity.MaxDevice;

public interface MaxDeviceRepository extends JpaRepository<MaxDevice,Long> {
    @Query("select m from MaxDevice m")
    public MaxDevice getMaxDeviceCount();
}
