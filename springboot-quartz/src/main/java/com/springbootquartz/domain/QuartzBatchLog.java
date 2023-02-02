package com.springbootquartz.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

import javax.persistence.*;
import java.util.Date;

@Getter
@Entity
@Table(name = "QUARTZ_BATCH_LOG")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class QuartzBatchLog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

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
    public QuartzBatchLog(String schedName,String triggerName,String triggerGroup,String jobName,String jobGroup,String result,String exceptionMessage,Date startTime,Date endTime){
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

}
