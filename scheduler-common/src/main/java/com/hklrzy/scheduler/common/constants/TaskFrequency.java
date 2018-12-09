package com.hklrzy.scheduler.common.constants;

import lombok.Getter;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Created 2018/12/6.
 *
 * @author ke.hao
 */
@Getter
public enum TaskFrequency {

    EVERY_TEN_MINUTES(1),

    HOURLY(2),

    DAILY(3),

    WEEKLY(4),

    MONTHLY(5);

    Integer code;

    private static final Map<Integer, TaskFrequency> FREQUENCY_CODE_MAP = Arrays.stream(values())
            .collect(Collectors.toMap(TaskFrequency::getCode, Function.identity()));


    TaskFrequency(Integer code) {
        this.code = code;
    }

    public static TaskFrequency ofCode(Integer code) {
        return FREQUENCY_CODE_MAP.getOrDefault(code, DAILY);
    }

    public LocalDateTime formatTaskTime(LocalDateTime taskTime) {
        return taskTime;
    }

}
