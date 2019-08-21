package com.slabs.exchange.common.config;


import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Data
@Component
public class ScheduleProperties {
    @Value("${schedule.num}")
    private String num;
}
