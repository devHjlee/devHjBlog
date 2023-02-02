package com.springbootquartz.service.impl;


import com.springbootquartz.dto.JobRequest;
import com.springbootquartz.exception.ApiException;
import com.springbootquartz.exception.ErrorCode;
import com.springbootquartz.quartz.QuartzUtils;
import com.springbootquartz.service.QuartzHistoryService;
import com.springbootquartz.service.QuartzService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.quartz.*;
import org.springframework.context.ApplicationContext;

import org.springframework.scheduling.quartz.SchedulerFactoryBean;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;

import static com.springbootquartz.exception.ErrorCode.INVALID_PARAMETER;

@Slf4j
@Component
@RequiredArgsConstructor
public class QuartzServiceImpl implements QuartzService {

    private final SchedulerFactoryBean schedulerFactoryBean;

    private final QuartzHistoryService quartzHistoryService;

    private final ApplicationContext context;

    @Override
    public void addScheduleJob(JobRequest jobRequest) {
        JobDetail jobDetail;
        Trigger trigger;
        Class<Job> jobClass = null;

        try {
            jobClass = (Class<Job>) Class.forName("com.springbootquartz.cronjob." +jobRequest.getJobClass());
            jobDetail = QuartzUtils.createJob(jobRequest,jobClass,context);
            trigger = QuartzUtils.createTrigger(jobRequest);

            schedulerFactoryBean.getScheduler().scheduleJob(jobDetail,trigger);

            jobRequest.setSchedName(schedulerFactoryBean.getScheduler().getSchedulerName());
            quartzHistoryService.save(jobRequest);
        } catch (SchedulerException e) {
            throw new ApiException(ErrorCode.INTERNAL_SERVER_ERROR);
        } catch (ClassNotFoundException e){
            throw new ApiException(ErrorCode.INVALID_CLASS);
        } catch (IllegalArgumentException e){
            throw new IllegalArgumentException(e.getMessage());
        }
    }

    @Override
    public boolean updateScheduleJob(JobRequest jobRequest) {
        JobKey jobKey = null;
        Trigger newTrigger;

        try {
            newTrigger = QuartzUtils.createTrigger(jobRequest);
            jobKey = JobKey.jobKey(jobRequest.getJobName(), jobRequest.getJobGroup());

            Date dt = schedulerFactoryBean.getScheduler().rescheduleJob(TriggerKey.triggerKey(jobRequest.getJobName().concat("Trigger"),jobRequest.getJobGroup()), newTrigger);
            log.debug("Job with jobKey : {} rescheduled successfully at date : {}", jobKey, dt);
            return true;
        } catch (SchedulerException e) {
            log.error("[schedulerdebug] error occurred while checking job with jobKey : {}", jobRequest.getJobName(), e);
        } catch (IllegalArgumentException e){
            log.error("[schedulerdebug] error occurred while checking job with jobKey : {}", jobRequest.getJobName(), e);
            throw new IllegalArgumentException(e);
        }
        return false;
    }

    @Override
    public boolean deleteScheduleJob(JobRequest jobRequest) {
        JobKey jobKey = JobKey.jobKey(jobRequest.getJobName(), jobRequest.getJobGroup());
        try {
            schedulerFactoryBean.getScheduler().deleteJob(jobKey);
        } catch (SchedulerException e) {
            throw new ApiException(ErrorCode.INTERNAL_SERVER_ERROR);
        }
        return false;
    }

    @Override
    public boolean pauseScheduleJob(JobRequest jobRequest) {
        JobKey jobKey = JobKey.jobKey(jobRequest.getJobName(), jobRequest.getJobGroup());
        try {
            schedulerFactoryBean.getScheduler().pauseJob(jobKey);
        } catch (SchedulerException e) {
            throw new ApiException(ErrorCode.INTERNAL_SERVER_ERROR);
        }
        return false;
    }

    @Override
    public boolean resumeScheduleJob(JobRequest jobRequest) {
        JobKey jobKey = JobKey.jobKey(jobRequest.getJobName(), jobRequest.getJobGroup());
        try {
            schedulerFactoryBean.getScheduler().resumeJob(jobKey);
        } catch (SchedulerException e) {
            throw new ApiException(ErrorCode.INTERNAL_SERVER_ERROR);
        }
        return false;
    }

    @Override
    public boolean immediatelyJob(JobRequest jobRequest){
        JobKey jobKey = JobKey.jobKey(jobRequest.getJobName(), jobRequest.getJobGroup());
        JobDataMap jobDataMap = new JobDataMap(jobRequest.getJobDataMap());

        try {
            schedulerFactoryBean.getScheduler().triggerJob(jobKey,jobDataMap);
        } catch (SchedulerException e) {
            throw new ApiException(ErrorCode.INTERNAL_SERVER_ERROR);
        }
        return false;
    }

    @Override
    public boolean isJobRunning(JobRequest jobRequest) {
        try {
            List<JobExecutionContext> currentJobs = schedulerFactoryBean.getScheduler().getCurrentlyExecutingJobs();
            if (currentJobs != null) {
                for (JobExecutionContext jobCtx : currentJobs) {
                    if (jobRequest.getJobName().equals(jobCtx.getJobDetail().getKey().getName())) {
                        return true;
                    }
                }
            }
        } catch (SchedulerException e) {
            throw new ApiException(ErrorCode.INTERNAL_SERVER_ERROR);
        }
        return false;
    }

    @Override
    public boolean isJobExists(JobRequest jobRequest) {
        JobKey jobKey = jobKey = new JobKey(jobRequest.getJobName(),jobRequest.getJobGroup());
        try {
            Scheduler scheduler = schedulerFactoryBean.getScheduler();
            if (scheduler.checkExists(jobKey)) {
                return true;
            }

        } catch (SchedulerException e) {
            throw new ApiException(ErrorCode.INTERNAL_SERVER_ERROR);
        }
        return false;
    }

    @Override
    public String getScheduleState(JobRequest jobRequest) {
        JobKey jobKey = JobKey.jobKey(jobRequest.getJobName(), jobRequest.getJobGroup());
        try {
            Scheduler scheduler = schedulerFactoryBean.getScheduler();
            JobDetail jobDetail = scheduler.getJobDetail(jobKey);

            List<? extends Trigger> triggers = scheduler.getTriggersOfJob(jobDetail.getKey());

            if (triggers != null && triggers.size() > 0) {
                for (Trigger trigger : triggers) {
                    Trigger.TriggerState triggerState = scheduler.getTriggerState(trigger.getKey());
                    if (Trigger.TriggerState.NORMAL.equals(triggerState)) {
                        return "SCHEDULED";
                    }
                    return triggerState.name().toUpperCase();
                }
            }
        } catch (SchedulerException e) {
            throw new ApiException(ErrorCode.INTERNAL_SERVER_ERROR);
        }
        return null;
    }
}
