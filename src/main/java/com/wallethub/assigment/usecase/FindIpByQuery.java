package com.wallethub.assigment.usecase;


import com.wallethub.assigment.domain.Duration;
import com.wallethub.assigment.gateway.repository.AccessLogRepository;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Component
public class FindIpByQuery {

    private final AccessLogRepository accessLogRepository;

    public FindIpByQuery(AccessLogRepository accessLogRepository) {
        this.accessLogRepository = accessLogRepository;
    }

    public List<String> execute(LocalDateTime dateTime, Duration duration, Long threshold){

        LocalDateTime lastDate;

        if(duration.equals(Duration.DAILY)){
            lastDate = dateTime.plusDays(1);
        } else {
           lastDate = dateTime.plusHours(1);
        }

        return accessLogRepository.findIpByPeriodAndThresholdGEThan(dateTime, lastDate, threshold);
    }
}
