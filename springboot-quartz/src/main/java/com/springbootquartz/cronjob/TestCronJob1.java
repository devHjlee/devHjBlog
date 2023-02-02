package com.springbootquartz.cronjob;

import lombok.extern.slf4j.Slf4j;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.scheduling.quartz.QuartzJobBean;

/**
 * 단순 스케줄러 테스트
 */
@Slf4j
public class TestCronJob1 extends QuartzJobBean {

    @Override
    protected void executeInternal(JobExecutionContext context) {
        log.info("============================================================================");
        log.info("TestCronJob1");
        log.info("TestCronJob1");
        log.info("============================================================================");
    }
}
