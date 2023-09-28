package com.sgurin.inetback.component.scheduler;

import com.sgurin.inetback.annotation.scheduler.SchedulerTaskContainer;
import com.sgurin.inetback.service.scheduler.SchedulerTaskProcess;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class TaskContainer {
    @SchedulerTaskContainer
    private Map<String, SchedulerTaskProcess> taskList = new HashMap<>();

    public Map<String, SchedulerTaskProcess> getTaskList() {
        return taskList;
    }
}
