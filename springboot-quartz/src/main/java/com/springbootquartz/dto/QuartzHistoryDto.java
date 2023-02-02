package com.springbootquartz.dto;

import com.springbootquartz.domain.QuartzHistory;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class QuartzHistoryDto {

    private String schedName;
    private String triggerName;
    private String triggerGroup;
    private String jobName;
    private String jobGroup;
    private String jobClass;
    private String scheduleState;
    private String cronExpression;
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;
    private String reason;

    @Builder
    public QuartzHistoryDto(String schedName, String triggerName, String triggerGroup, String jobName, String jobGroup, String jobClass, String scheduleState, String cronExpression, LocalDateTime createTime, String reason){
        this.schedName = schedName;
        this.triggerName = triggerName;
        this.triggerGroup = triggerGroup;
        this.jobName = jobName;
        this.jobGroup = jobGroup;
        this.jobClass = jobClass;
        this.scheduleState = scheduleState;
        this.cronExpression = cronExpression;
        this.createTime = createTime;
        this.reason = reason;

    }

    public QuartzHistory toEntity(){
        return QuartzHistory.builder()
                .schedName(this.schedName)
                .triggerName(this.triggerName)
                .triggerGroup(this.triggerGroup)
                .jobName(this.jobName)
                .jobGroup(this.jobGroup)
                .jobClass(this.jobClass)
                .scheduleState(this.scheduleState)
                .cronExpression(this.cronExpression)
                .createTime(this.createTime)
                .reason(this.reason)
                .build();

    }
}
