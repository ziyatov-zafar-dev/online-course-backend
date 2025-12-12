package uz.codebyz.onlinecoursebackend.admin.device.service.impl;

import org.springframework.stereotype.Service;
import uz.codebyz.onlinecoursebackend.admin.device.service.DeviceService;
import uz.codebyz.onlinecoursebackend.common.ResponseDto;
import uz.codebyz.onlinecoursebackend.userDevice.entity.MaxDevice;
import uz.codebyz.onlinecoursebackend.userDevice.repository.MaxDeviceRepository;

@Service
public class DeviceServiceImpl implements DeviceService {
    private final MaxDeviceRepository maxDeviceRepository;

    public DeviceServiceImpl(MaxDeviceRepository maxDeviceRepository) {
        this.maxDeviceRepository = maxDeviceRepository;
    }

    @Override
    public ResponseDto<?> changeDeviceLimit(Integer limit) {
        MaxDevice maxDevice = maxDeviceRepository.getMaxDeviceCount();
        maxDevice.setDeviceCount(limit);
        maxDeviceRepository.save(maxDevice);
        return new ResponseDto<>(true, "Success", "Changed");
    }

    @Override
    public ResponseDto<Integer> getDeviceLimit() {
        ///  ///////////////////////////
        return new ResponseDto<>(true , "Success" , maxDeviceRepository.getMaxDeviceCount().getDeviceCount());
    }
}
