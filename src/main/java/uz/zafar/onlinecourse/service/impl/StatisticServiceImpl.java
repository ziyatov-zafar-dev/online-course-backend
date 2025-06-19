package uz.zafar.onlinecourse.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import uz.zafar.onlinecourse.db.repository.UserRepository;
import uz.zafar.onlinecourse.dto.ResponseDto;
import uz.zafar.onlinecourse.dto.statistic_dto.res.StatisticDto;
import uz.zafar.onlinecourse.service.StatisticService;

@Service
@Log4j2
@RequiredArgsConstructor
public class StatisticServiceImpl implements StatisticService {
    private final UserRepository userRepository;
    @Override
    public ResponseDto<StatisticDto> getStatistic() {
        try {
            return new ResponseDto<>(true, "Success", userRepository.getStatistics());
        } catch (Exception e) {
            log.error(e);
            return new ResponseDto<>(false, e.getMessage());
        }
    }
}
