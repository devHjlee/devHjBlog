package com.springbootquartz.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.springbootquartz.domain.QuartzBatchLog;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Date;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class QuartzBatchLogDto {
    private String schedName;
    private String triggerName;
    private String triggerGroup;
    private String jobName;
    private String jobGroup;
    private String result;
    private String exceptionMessage;
    @JsonFormat(shape= JsonFormat.Shape.STRING, pattern="yyyy-MM-dd HH:mm:ss", timezone="Asia/Seoul")
    private Date startTime;
    @JsonFormat(shape= JsonFormat.Shape.STRING, pattern="yyyy-MM-dd HH:mm:ss", timezone="Asia/Seoul")
    private Date endTime;

    @Builder
    public QuartzBatchLogDto(String schedName,String triggerName,String triggerGroup,String jobName,String jobGroup,String result,String exceptionMessage,Date startTime,Date endTime){
        this.schedName = schedName;
        this.triggerName = triggerName;
        this.triggerGroup = triggerGroup;
        this.jobName = jobName;
        this.jobGroup = jobGroup;
        this.result = result;
        this.exceptionMessage = exceptionMessage;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    public QuartzBatchLog toEntity(){
        return QuartzBatchLog.builder()
                .schedName(this.schedName)
                .triggerName(this.triggerName)
                .triggerGroup(this.triggerGroup)
                .jobName(this.jobName)
                .jobGroup(this.jobGroup)
                .result(this.result)
                .exceptionMessage(this.exceptionMessage)
                .startTime(this.startTime)
                .endTime(this.endTime)
                .build();
    }
}
