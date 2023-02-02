package com.springbootquartz.dto;

import lombok.Getter;
import lombok.Setter;
import org.quartz.JobDataMap;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;

@Getter
@Setter
public class JobRequest {

    private String schedName;
    private String jobGroup = "DEV";

    private String retry = "N";

    @NotBlank(message = "JobName is required.")
    private String jobName;

    @NotBlank(message = "JobClass is required.")
    private String jobClass;

    //@NotBlank(message = "cronExpression is required.")
    private String cronExpression;

    private String scheduleStat;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime startDateAt;

    private long repeatIntervalInSeconds;

    private int repeatCount;

    private JobDataMap jobDataMap;

    private String desc;

    private String reason;

}
