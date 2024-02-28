package com.sgurin.inetback.domain;


import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.nio.charset.StandardCharsets;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;

@Entity
@Table(schema = "inet_back", name = "protocol")
public class Protocol {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @Column(name = "text", columnDefinition = "bytea")
    private byte[] logMessage;

    @Column(name = "err_count")
    private int errCount = 0;

    @Transient
    private StringBuilder tempMessage;

    @Transient
    private SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS Z");

    @Column(columnDefinition = "TIMESTAMP WITH TIME ZONE", name = "created_at", insertable = false, updatable = false)
    private Timestamp createdAt;

    public Protocol() {
    }

    public Protocol(String name) {
        this.name = name;
        this.tempMessage = new StringBuilder("").append("\n");
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public StringBuilder getTempMessage() {
        return tempMessage;
    }

    public void setTempMessage(StringBuilder tempMessage) {
        this.tempMessage = tempMessage;
    }

    public SimpleDateFormat getFormat() {
        return format;
    }

    public void setFormat(SimpleDateFormat format) {
        this.format = format;
    }

    public String getLogMessage() {
        return new String(this.logMessage, StandardCharsets.UTF_8);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @JsonIgnore
    public byte[] getLogMessageByte() {
        return logMessage;
    }

    public void setLogMessage(byte[] logMessage) {
        this.logMessage = logMessage;
    }

    public Protocol addLineProtocol(String line) {
        this.tempMessage.append(format.format(new Date()));
        this.tempMessage.append("  :").append(line).append('\n');
        return this;
    }

    public int getErrCount() {
        return errCount;
    }

    public void setErrCount(int errCount) {
        this.errCount += errCount;
    }

    public void addErrCount() {
        this.errCount += 1;
    }

    public void confirm() {
        this.logMessage = this.tempMessage.toString().getBytes(StandardCharsets.UTF_8);
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }
}