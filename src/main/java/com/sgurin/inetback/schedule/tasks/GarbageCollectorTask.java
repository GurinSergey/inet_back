package com.sgurin.inetback.schedule.tasks;

import com.sgurin.inetback.annotation.scheduler.SchedulerTask;
import com.sgurin.inetback.service.scheduler.SchedulerTaskProcess;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Date;

@SchedulerTask(name = "GarbageCollectorTask")
@Service
@Slf4j
public class GarbageCollectorTask implements SchedulerTaskProcess {
    @Override
    public boolean run(String parameters) {
        return clearProtocolOldData();
    }

    private boolean clearProtocolOldData() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        log.info("clearOldData : {}", dateFormat.format(new Date()));

        return true;
    }
}