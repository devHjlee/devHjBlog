package com.springbootquartz.service;

import com.springbootquartz.dto.JobRequest;
import com.springbootquartz.dto.QuartzHistoryDto;
import com.springbootquartz.repository.QuartzHistoryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service("quartzHistoryService")
@Transactional
@RequiredArgsConstructor
public class QuartzHistoryService {

    private final QuartzHistoryRepository quartzHistoryRepository;

    public void save(JobRequest jobRequest) {
        QuartzHistoryDto quartzHistoryDto = QuartzHistoryDto
                .builder()
                .schedName(jobRequest.getSchedName())
                .jobName(jobRequest.getJobName())
                .jobGroup(jobRequest.getJobGroup())
                .jobClass(jobRequest.getJobClass())
                .triggerName(jobRequest.getJobName().concat("Trigger"))
                .triggerGroup(jobRequest.getJobGroup())
                .scheduleState(jobRequest.getScheduleStat())
                .cronExpression(jobRequest.getCronExpression())
                .reason(jobRequest.getReason())
                .build();

        quartzHistoryRepository.save(quartzHistoryDto.toEntity());
    }

}
