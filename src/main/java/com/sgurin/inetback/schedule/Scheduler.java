package com.sgurin.inetback.schedule;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import com.sgurin.inetback.component.scheduler.TaskContainer;
import com.sgurin.inetback.domain.SchedulerTask;
import com.sgurin.inetback.schedule.threads.ThreadServiceProcess;
import com.sgurin.inetback.service.scheduler.SchedulerService;
import io.micrometer.core.annotation.Timed;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;

@Component
@Slf4j
public class Scheduler {
    public static final long CHECK_EVERY_10_minutes = 10 * 60 * 1000L;

    @Value("${scheduler.enable}")
    boolean isSchedulerEnabled;

    private final SchedulerService schedulerService;
    private final TaskContainer taskContainer;

    @Autowired
    public Scheduler(SchedulerService schedulerService,
                     TaskContainer taskContainer) {
        this.schedulerService = schedulerService;
        this.taskContainer = taskContainer;
    }

    @Timed("runTestTask")
    @Scheduled(fixedDelay = 60000)
    public void runTestTask() {
        if (!isSchedulerEnabled) return;

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        log.info("now : {}", dateFormat.format(new Date()));
    }

    @PostConstruct
    public void initializeNextStamp() {
        if (!isSchedulerEnabled) return;

        List<SchedulerTask> tasks = schedulerService.getAll();

        for (SchedulerTask task : tasks) {
            if (Objects.isNull(task.getNextStamp())) {
                Calendar cal = Calendar.getInstance();
                if (task.getWorkEndTime() == null) {
                    task.setWorkEndTime(new Date());
                }
                cal.setTime(task.getWorkEndTime());
                cal.add(task.getTypePeriod(), task.getPeriod());
                task.setNextStamp(cal.getTime());
                schedulerService.update(task);
            }
        }
    }

    @Scheduled(fixedDelay = 30000)
    public void runTaskByScheduler() {
        if (!isSchedulerEnabled) return;

        DateFormat dateFormat = new SimpleDateFormat("MM-dd-hh:mm:ss");
        Date currentDate = new Date();
        String executorIdentifier = dateFormat.format(currentDate);
        List<SchedulerTask> tasks = schedulerService.findAllCurrentTask(currentDate);

        final ThreadFactory threadFactory = new ThreadFactoryBuilder()
                .setNameFormat(executorIdentifier + "_pet-%d")
                .build();
        log.info("executor {} start", executorIdentifier);
        log.info("executor {} count tasks: {}", executorIdentifier, tasks.size());
        final ExecutorService executor = Executors.newFixedThreadPool(8, threadFactory);
        for (SchedulerTask scheduler : tasks) {

            executor.submit(new ThreadServiceProcess(
                    schedulerService,
                    taskContainer,
                    scheduler.getSchedulerId(),
                    scheduler.getObject(),
                    scheduler.getParam()));
        }

        executor.shutdown();
        log.info("executor {} end", executorIdentifier);
    }

    @Scheduled(fixedDelay = CHECK_EVERY_10_minutes)
    public void checkRepeatableTasks() {
        if (!isSchedulerEnabled) return;

        Calendar cal = Calendar.getInstance();
        Date currentDate = cal.getTime();
        List<SchedulerTask> tasks = schedulerService.findAllRepeatableTasks();

        for (SchedulerTask schedulerTask : tasks) {
            if (Objects.isNull(schedulerTask.getNextStamp())) {
                if (currentDate.getTime() - schedulerTask.getWorkStartTime().getTime() >= schedulerTask.getRepeatAfterMinutes() * 60 * 1000L) {
                    log.info("task {} will be restarted", schedulerTask.getSchedulerName());

                    Calendar calendar = Calendar.getInstance();
                    calendar.setTime(schedulerTask.getWorkEndTime());
                    calendar.add(Calendar.MINUTE, schedulerTask.getRepeatAfterMinutes());
                    schedulerTask.setNextStamp(calendar.getTime());

                    schedulerService.update(schedulerTask);
                }
            }
        }
    }
}
