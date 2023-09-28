package com.sgurin.inetback.service.scheduler;

import com.sgurin.inetback.domain.SchedulerTask;
import com.sgurin.inetback.repository.SchedulerRepo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
@Slf4j
public class SchedulerService {
    private final SchedulerRepo schedulerRepo;

    @Autowired
    public SchedulerService(SchedulerRepo schedulerRepo) {
        this.schedulerRepo = schedulerRepo;
    }

    public List<SchedulerTask> getAll() {
        return schedulerRepo.findAll();
    }

    public SchedulerTask update(SchedulerTask task) {
        return schedulerRepo.saveAndFlush(task);
    }

    public List<SchedulerTask> findAllCurrentTask(Date date) {
        return schedulerRepo.findAllCurrentTask(date);
    }

    public SchedulerTask getById(long id) {
        return schedulerRepo.findById(id).orElse(null);
    }

    public List<SchedulerTask> findAllRepeatableTasks() {
        return schedulerRepo.findAllRepeatableTasks();
    }
}