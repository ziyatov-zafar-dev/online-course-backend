package uz.zafar.onlinecourse.rest.admin;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import uz.zafar.onlinecourse.dto.ResponseDto;
import uz.zafar.onlinecourse.dto.statistic_dto.res.StatisticDto;
import uz.zafar.onlinecourse.service.StatisticService;

@RequestMapping("/api/teacher/statistic")
@RequiredArgsConstructor
@RestController
@Tag(name = "Admin Statistic Controller", description = "Admin paneli uchun statistika bilan ishlash API'lari1")
public class AdminStatisticsRestController {
    private final StatisticService statisticService;

    @GetMapping("statistics")
    public ResponseDto<StatisticDto> getStatistics() {
        return statisticService.getStatistic();
    }
}
