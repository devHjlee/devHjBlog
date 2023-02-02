package com.springbootquartz.cronjob;

import lombok.extern.slf4j.Slf4j;
import org.quartz.*;
import org.springframework.scheduling.quartz.QuartzJobBean;

import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;

/**
 * 인터럽트 테스트용
 * TODO 스레드,인터럽트 이해필요
 */
@Slf4j
public class TestCronJob3 extends QuartzJobBean implements InterruptableJob {

    private volatile boolean isJobInterrupted = false;
    private final int MAX_SLEEP_IN_SECONDS = 20;

    private volatile Thread currThread;

    @Override
    protected void executeInternal(JobExecutionContext context) throws JobExecutionException {
        JobDataMap jobDataMap = context.getJobDetail().getJobDataMap();
        currThread = Thread.currentThread();
        if (jobDataMap.size() > 0 && !isJobInterrupted) {
            String jobId = (String) jobDataMap.get("jobS");
            JobKey jobKey = context.getJobDetail().getKey();
            log.info("START TEST3==================================================");
            log.info("TestCronJob3");
            try {
                Thread.sleep(15000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            log.info("TestCronJob3");
            log.info("END TEST2================================================================");
        }
    }

    @Override
    public void interrupt() {
        isJobInterrupted = true;
        if (currThread != null) {
            log.info("interrupting - {}", currThread.getName());
            currThread.interrupt();
        }
    }
}
