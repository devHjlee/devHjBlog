package com.springbootquartz.quartz;

import com.springbootquartz.dto.JobRequest;
import org.quartz.*;
import org.springframework.context.ApplicationContext;
import org.springframework.scheduling.quartz.CronTriggerFactoryBean;
import org.springframework.scheduling.quartz.JobDetailFactoryBean;
import org.springframework.scheduling.quartz.SimpleTriggerFactoryBean;
import org.springframework.util.StringUtils;

import java.text.ParseException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

import static org.quartz.CronExpression.isValidExpression;

public class QuartzUtils {

    private QuartzUtils() {
    }

    /**
     * Job 생성
     * @param jobRequest - Quartz Job 정보
     * @param jobClass - Job 생성할 Class
     * @param context - ApplicationContext
     * @return JobDetail
     */
    public static JobDetail createJob(JobRequest jobRequest, Class<? extends Job> jobClass, ApplicationContext context) {
        JobDetailFactoryBean factoryBean = new JobDetailFactoryBean();
        factoryBean.setJobClass(jobClass);
        factoryBean.setDurability(false);
        factoryBean.setApplicationContext(context);
        factoryBean.setName(jobRequest.getJobName());
        factoryBean.setGroup(jobRequest.getJobGroup());
        factoryBean.setDescription(jobRequest.getDesc());
        if (jobRequest.getJobDataMap() != null) {
            factoryBean.setJobDataMap(jobRequest.getJobDataMap());
        }

        factoryBean.afterPropertiesSet();
        return factoryBean.getObject();

//        return JobBuilder
//                .newJob(jobClass)
//                .withIdentity(jobRequest.getJobName(),jobRequest.getJobGroup())
//                .storeDurably(false)
//                .withDescription(jobRequest.getDesc())
//                .usingJobData(jobRequest.getJobDataMap())
//                .build();
    }

    /**
     * Trigger 생성(Cron,Simple)
     * @param jobRequest - Quartz Job 정보
     * @return Trigger
     */
    public static Trigger createTrigger(JobRequest jobRequest) {
        String cronExpression = jobRequest.getCronExpression();
        if (!isValidExpression(cronExpression)) {
            throw new IllegalArgumentException("Provided expression " + cronExpression + " is not a valid cron expression");
        } else {
            return createCronTrigger(jobRequest);
        }
    }

    /**
     * CronTrigger 생성
     * @param jobRequest - Quartz Job 정보
     * @return Trigger
     */
    private static Trigger createCronTrigger(JobRequest jobRequest) {
        JobDataMap jobDataMap = new JobDataMap();
        jobDataMap.put("failCnt","0");
        jobDataMap.put("stop","N");
        jobDataMap.put("retry",jobRequest.getRetry());
        CronTriggerFactoryBean factoryBean = new CronTriggerFactoryBean();
        factoryBean.setName(jobRequest.getJobName().concat("Trigger"));
        factoryBean.setGroup(jobRequest.getJobGroup());
        factoryBean.setCronExpression(jobRequest.getCronExpression());
        factoryBean.setMisfireInstruction(SimpleTrigger.MISFIRE_INSTRUCTION_FIRE_NOW);
        factoryBean.setJobDataMap(jobDataMap);
        try {
            factoryBean.afterPropertiesSet();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return factoryBean.getObject();
    }

    /**
     * SimpleTrigger 생성
     * @param jobRequest - Quartz Job 정보
     * @return Trigger
     */
    private static Trigger createSimpleTrigger(JobRequest jobRequest) {
        SimpleTriggerFactoryBean factoryBean = new SimpleTriggerFactoryBean();
        factoryBean.setName(jobRequest.getJobName());
        factoryBean.setGroup(jobRequest.getJobGroup());
        factoryBean.setStartTime(Date.from(jobRequest.getStartDateAt().atZone(ZoneId.systemDefault()).toInstant()));
        factoryBean.setMisfireInstruction(SimpleTrigger.MISFIRE_INSTRUCTION_FIRE_NOW);
        factoryBean.setRepeatInterval(jobRequest.getRepeatIntervalInSeconds() * 1000); //ms 단위임
        factoryBean.setRepeatCount(jobRequest.getRepeatCount());

        factoryBean.afterPropertiesSet();
        return factoryBean.getObject();
    }
}
