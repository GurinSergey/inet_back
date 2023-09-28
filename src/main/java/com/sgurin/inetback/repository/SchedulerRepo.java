package com.sgurin.inetback.repository;

import com.sgurin.inetback.domain.SchedulerTask;
import io.micrometer.core.annotation.Timed;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Date;
import java.util.List;

public interface SchedulerRepo extends JpaRepository<SchedulerTask, Long> {
    @Timed("findAllCurrentTask")
    @Query("Select sch from SchedulerTask sch where sch.nextStamp<= ?1 and sch.schedulerType <> 0 ORDER BY sch.nextStamp")
    List<SchedulerTask> findAllCurrentTask(Date date);

    @Query("Select sch from SchedulerTask sch where sch.schedulerType <> 0 and sch.repeatable = true")
    List<SchedulerTask> findAllRepeatableTasks();
}
