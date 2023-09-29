package com.sgurin.inetback;

import com.sgurin.inetback.schedule.tasks.GarbageCollectorTask;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.Assert.assertTrue;

@SpringBootTest(classes = {
        InetBackApplication.class})
@RunWith(SpringRunner.class)
@Transactional
public class ScheduleTaskTest {
    @Autowired
    private GarbageCollectorTask garbageCollectorTask;

    @Test
    @Rollback(value = false)
    public void name() {
        boolean run = garbageCollectorTask.run("14");

        assertTrue(run);
    }
}
