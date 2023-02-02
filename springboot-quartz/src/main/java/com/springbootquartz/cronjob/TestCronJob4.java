package com.springbootquartz.cronjob;

import lombok.extern.slf4j.Slf4j;
import org.quartz.DisallowConcurrentExecution;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.scheduling.quartz.QuartzJobBean;

/**
 * 트리거 중지 테스트
 */
@Slf4j
@DisallowConcurrentExecution
public class TestCronJob4 extends QuartzJobBean {

    @Override
    protected void executeInternal(JobExecutionContext context) throws JobExecutionException {
        log.info("START TEST4==================================================");
        log.info("TestCronJob4");
        log.info("TestCronJob4");
        log.info("END TEST4================================================================");
        throw new JobExecutionException("exception");
    }
}
