package uz.codebyz.onlinecoursebackend.admin.rest;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uz.codebyz.onlinecoursebackend.admin.device.service.DeviceService;
import uz.codebyz.onlinecoursebackend.common.ResponseDto;

@RestController
@RequestMapping("/api/admin/device")
public class AdminDeviceLimitRestController {
    private final DeviceService deviceService;

    public AdminDeviceLimitRestController(DeviceService deviceService) {
        this.deviceService = deviceService;
    }

    @PutMapping("change-device-limit/{limit}")
    public ResponseEntity<ResponseDto<?>> changeDeviceLimit(@PathVariable("limit") Integer limit) {
        return new ResponseEntity<>(deviceService.changeDeviceLimit(limit), HttpStatus.OK);
    }


    @GetMapping("get-device-limit")
    public ResponseEntity<ResponseDto<Integer>> getDeviceLimit() {
        return new ResponseEntity<>(deviceService.getDeviceLimit(), HttpStatus.OK);
    }

}
