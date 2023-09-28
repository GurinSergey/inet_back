package com.sgurin.inetback.schedule.threads;

import com.sgurin.inetback.component.scheduler.TaskContainer;
import com.sgurin.inetback.domain.SchedulerTask;
import com.sgurin.inetback.enums.SchedulerPeriodType;
import com.sgurin.inetback.enums.SchedulerServiceKind;
import com.sgurin.inetback.enums.SchedulerServiceStatus;
import com.sgurin.inetback.service.scheduler.SchedulerService;
import com.sgurin.inetback.service.scheduler.SchedulerTaskProcess;
import lombok.extern.slf4j.Slf4j;

import java.util.Calendar;
import java.util.Date;
import java.util.Objects;

@Slf4j
public class ThreadServiceProcess implements Runnable{
    final SchedulerService schedulerService;
    final TaskContainer taskContainer;
    final long schedulerId;
    final String objectName;
    final String params;

    public ThreadServiceProcess(SchedulerService schedulerService,
                                TaskContainer taskContainer,
                                long schedulerId,
                                String objectName,
                                String params) {
        this.schedulerService = schedulerService;
        this.taskContainer = taskContainer;
        this.schedulerId = schedulerId;
        this.objectName = objectName;
        this.params = params;
    }

    @Override
    public void run() {
        SchedulerTask scheduler = schedulerService.getById(this.schedulerId);

        Date nextDate = scheduler.getNextStamp();
        scheduler.setNextStamp(null);
        scheduler.setWorkStartTime(new Date());
        if (scheduler.getServiceKind() == SchedulerServiceKind.SINGLE) {
            scheduler.setSchedulerType(SchedulerServiceStatus.DISABLE);
        }
        schedulerService.update(scheduler);

        try {
            log.info("ThreadServiceProcess {}",this.objectName);
            log.info("thread id {}", this.schedulerId);

            SchedulerTaskProcess taskProcess = taskContainer.getTaskList().get(this.objectName);

            if (Objects.nonNull(taskProcess)) {
                //spring behavior
                taskProcess.run(params);
            } else {
                log.info("Task not found {}", objectName);
            }

            scheduler.setWorkEndTime(new Date());

            Calendar cal = Calendar.getInstance();
            if (scheduler.getTypePeriod() == SchedulerPeriodType.DATE.getValue()) {
                cal.setTime(nextDate);
            } else {
                cal.setTime(new Date());
            }
            cal.add(scheduler.getTypePeriod(), scheduler.getPeriod());
            scheduler.setNextStamp(cal.getTime());

            schedulerService.update(scheduler);

            if (scheduler.getNextTask() != null) {
                SchedulerTask nextTask = schedulerService.getById(scheduler.getNextTask());
                nextTask.setSchedulerType(SchedulerServiceStatus.ACTIVE);
                nextTask.setNextStamp(nextDate);
                schedulerService.update(nextTask);
            }

            log.info("thread end {}", objectName);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}