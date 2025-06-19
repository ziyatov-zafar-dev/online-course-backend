package uz.zafar.onlinecourse.service;

import uz.zafar.onlinecourse.dto.ResponseDto;
import uz.zafar.onlinecourse.dto.statistic_dto.res.StatisticDto;

public interface StatisticService {
    public ResponseDto<StatisticDto> getStatistic();
}
