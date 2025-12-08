package uz.codebyz.onlinecoursebackend.admin.device.service;

import uz.codebyz.onlinecoursebackend.common.ResponseDto;

public interface DeviceService {
    ResponseDto<?>changeDeviceLimit(Integer limit);
}
