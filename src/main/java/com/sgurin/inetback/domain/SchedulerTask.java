package com.sgurin.inetback.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.sgurin.inetback.enums.SchedulerPeriodType;
import com.sgurin.inetback.enums.SchedulerServiceKind;
import com.sgurin.inetback.enums.SchedulerServiceStatus;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(schema = "inet_back", name = "scheduler")
public class SchedulerTask {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "scheduler_id")
    private long schedulerId;

    @Column(name = "scheduler_name")
    private String schedulerName;

    private String description;

    private String object;

    @Enumerated
    @Column(name = "scheduler_type")
    private SchedulerServiceStatus schedulerType;

    @Column(name = "period")
    private int period;

    @Column(name = "type_period")
    private SchedulerPeriodType typePeriod;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern = "dd.MM.yyyy hh:mm:ss")
    @Column(name = "work_start_time")
    @Temporal(TemporalType.TIMESTAMP)
    private Date workStartTime;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern = "dd.MM.yyyy hh:mm:ss")
    @Column(name = "work_end_time")
    @Temporal(TemporalType.TIMESTAMP)
    private Date workEndTime;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern = "dd.MM.yyyy hh:mm:ss")
    @Column(name = "start_time")
    @Temporal(TemporalType.TIMESTAMP)
    private Date startTime;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern = "dd.MM.yyyy hh:mm:ss")
    @Column(name = "next_stamp")
    @Temporal(TemporalType.TIMESTAMP)
    private Date nextStamp;

    @Enumerated
    @Column(name = "service_kind")
    private SchedulerServiceKind serviceKind = SchedulerServiceKind.PERIODIC;

    @Column(name = "next_task_id")
    private Long nextTask;

    @Column(name = "param")
    private String param;

    @Column(name = "repeatable")
    private Boolean repeatable = false;

    @Column(name = "repeat_after_minutes")
    private Integer repeatAfterMinutes = 0;

    public SchedulerTask(String schedulerName, String description, String object, SchedulerServiceStatus schedulerType, SchedulerServiceKind serviceKind) {
        this.schedulerName = schedulerName;
        this.description = description;
        this.object = object;
        this.schedulerType = schedulerType;
        this.serviceKind = serviceKind;
    }

    public SchedulerTask() {
    }

    public long getSchedulerId() {
        return schedulerId;
    }

    public void setSchedulerId(long schedulerId) {
        this.schedulerId = schedulerId;
    }

    public String getSchedulerName() {
        return schedulerName;
    }

    public void setSchedulerName(String schedulerName) {
        this.schedulerName = schedulerName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getObject() {
        return object;
    }

    public void setObject(String object) {
        this.object = object;
    }

    public SchedulerServiceStatus getSchedulerType() {
        return schedulerType;
    }

    public void setSchedulerType(SchedulerServiceStatus schedulerType) {
        this.schedulerType = schedulerType;
    }

    public Date getWorkStartTime() {
        return workStartTime;
    }

    public void setWorkStartTime(Date workStartTime) {
        this.workStartTime = workStartTime;
    }

    public Date getWorkEndTime() {
        return workEndTime;
    }

    public void setWorkEndTime(Date workEndTime) {
        this.workEndTime = workEndTime;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getNextStamp() {
        return nextStamp;
    }

    public void setNextStamp(Date nextStamp) {
        this.nextStamp = nextStamp;
    }

    public SchedulerServiceKind getServiceKind() {
        return serviceKind;
    }

    public void setServiceKind(SchedulerServiceKind serviceKind) {
        this.serviceKind = serviceKind;
    }

    public int getPeriod() {
        return period;
    }

    public void setPeriod(int period) {
        this.period = period;
    }

    public int getTypePeriod() {
        return typePeriod.getValue();
    }

    public void setTypePeriod(SchedulerPeriodType typePeriod) {
        this.typePeriod = typePeriod;
    }

    public String getParam() {
        return param;
    }

    public void setParam(String param) {
        this.param = param;
    }

    public String[] getParamsArrayString(){
        if (this.param == null) {
            return null;
        }
        return  this.param.split(",");
    }

    public Long getNextTask() {
        return nextTask;
    }

    public void setNextTask(Long nextTask) {
        this.nextTask = nextTask;
    }

    public Boolean getRepeatable() {
        return repeatable;
    }

    public void setRepeatable(Boolean repeatable) {
        this.repeatable = repeatable;
    }

    public Integer getRepeatAfterMinutes() {
        return repeatAfterMinutes;
    }

    public void setRepeatAfterMinutes(Integer repeatAfterMinutes) {
        this.repeatAfterMinutes = repeatAfterMinutes;
    }
}