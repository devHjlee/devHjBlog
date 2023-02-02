package com.springbootquartz.quartz;

import com.springbootquartz.dto.QuartzBatchLogDto;
import com.springbootquartz.service.QuartzBatchLogService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.quartz.*;
import org.springframework.stereotype.Component;

import java.util.Date;

@Slf4j
@Component
@RequiredArgsConstructor
public class JobsListener implements JobListener {

	private final QuartzBatchLogService quartzBatchLogService;

	@Override
	public String getName() {
		return "globalJob";
	}

	/**
	 * Job 수행 전
	 * @param context
	 */
	@Override
	public void jobToBeExecuted(JobExecutionContext context) {
		JobKey jobKey = context.getJobDetail().getKey();
		log.info("jobToBeExecuted :: jobKey : {}", jobKey);
	}

	/**
	 * Job 중단된 상태
	 * @param context
	 */
	@Override
	public void jobExecutionVetoed(JobExecutionContext context) {
		JobKey jobKey = context.getJobDetail().getKey();
		log.info("jobExecutionVetoed :: jobKey : {}", jobKey);
	}

	/**
	 * Job 수행 완료 후
	 * retry N : Job Exception 발생 시 해당 Trigger,Job Pause
	 * retry Y : Job Exception 발생 시 등록된 기존 Cron Expression 시간에 맞춰 재시도 / 총 3번 실패 시 해당 Trigger 중지
	 * @param context
	 * @param jobException
	 */
	@Override
	public void jobWasExecuted(JobExecutionContext context, JobExecutionException jobException) {

		final int maxCnt = 3;

		int failCnt = context.getTrigger().getJobDataMap().getIntValue("failCnt");
		String stop = (String) context.getTrigger().getJobDataMap().get("stop");
		String retry = (String) context.getTrigger().getJobDataMap().get("retry");
		String schedName = "";
		JobKey jobKey = context.getJobDetail().getKey();
		JobDataMap jobDataMap = context.getTrigger().getJobDataMap();

		log.info("jobWasExecuted :: jobKey : {}", jobKey);
		try {
			schedName = context.getScheduler().getSchedulerName();
			if(jobException != null){
				log.debug("Exception : {}",jobException.getMessage());

					if("N".equals(retry)){
						//context.getScheduler().pauseJob(jobKey);
						jobException.setUnscheduleAllTriggers(true);
					}else {
						++failCnt;

						if (maxCnt == failCnt) {
							jobDataMap.put("stop", "Y");
						} else {
							jobDataMap.put("failCnt", String.valueOf(failCnt));
						}
						if (("N").equals(stop)) {

							CronTrigger cronTrigger = (CronTrigger) context.getTrigger();
							Trigger newTrigger = TriggerBuilder
									.newTrigger()
									.startAt(new Date(System.currentTimeMillis() + 60000)) //reschedule 진행시 즉시 수행되는것을 방지하기 위해
									.withIdentity(context.getTrigger().getKey())
									//.withSchedule(CronScheduleBuilder.cronSchedule("*****"))
									.withSchedule(cronTrigger.getScheduleBuilder())
									.usingJobData(jobDataMap) // 실패 횟수,정지여부를 Trigger JobDataMap에 추가
									.build();

							context.getScheduler().rescheduleJob(context.getTrigger().getKey(), newTrigger);
							//정해진 시간이 아닌 즉시 실행을 위해서는 RefireImmediately 사용 필요
							//jobException.setRefireImmediately(true);

						} else {
							//해당 Trigger 중지
							jobException.setUnscheduleAllTriggers(true);
							//context.getScheduler().pauseJob(jobKey);
						}

					}
					//실패 관련 로직(알림,Email)
					log.info("notified :: context : {}", context);


			}
		} catch (SchedulerException e) {
			e.printStackTrace();
		}
		QuartzBatchLogDto quartzBatchLogDto = QuartzBatchLogDto
				.builder()
				.schedName(schedName)
				.jobName(jobKey.getName())
				.jobGroup(jobKey.getGroup())
				.triggerName(context.getTrigger().getKey().getName())
				.triggerGroup(context.getTrigger().getKey().getGroup())
				.startTime(context.getFireTime())
				.endTime(new Date(context.getFireTime().getTime() + context.getJobRunTime()))
				.result(jobException != null ?"N":"Y")
				.exceptionMessage(jobException != null ? jobException.getMessage():"")
				.build();

		quartzBatchLogService.save(quartzBatchLogDto);
	}
}
