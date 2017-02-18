package com.taskmanager.model;

import javax.persistence.*;
import java.util.Calendar;
import java.util.Date;
import java.util.UUID;

/**
 * Entity for Task
 */
@Entity
@Table(name = "task", schema = "public")
public class Task {

    /**
     * Uuid
     */
    @Id
    @Column(name = "id")
    private String id;

    /**
     * Date
     */
    @Column(name = "created_at")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdAt;

    /**
     * User id
     */
    @Column(name = "user_id")
    private String userId;

    /**
     * Src
     */
    @Column(name = "src")
    private String src;

    /**
     * Algorithm
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "algorithm")
    private HashType algo;

    /**
     * Status
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private TaskStatus status;

    /**
     * Error message
     */
    @Column(name = "error_msg")
    private String errorMsg;

    /**
     * Time execute
     */
    @Column(name = "execute_time")
    private long time;


    /**
     * Result
     */
    @Column(name = "result")
    private String result;

    /**
     * Create Task
     */
    public Task() {
    }

    /**
     * Create Task with id and userId
     * @param id task id
     * @param userId user id
     */
    public Task(String id, String userId) {
        this.id = id;
        this.userId = userId;
    }

    /**
     * Generate uuid.
     */
    @PrePersist
    public void prePersist() {
        if (id == null) {
            id = UUID.randomUUID().toString();
        }
        createdAt = Calendar.getInstance().getTime();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getSrc() {
        return src;
    }

    public void setSrc(String src) {
        this.src = src;
    }

    public HashType getAlgo() {
        return algo;
    }

    public void applyTime(long time) {
        this.time += time;
    }

    public void setAlgo(HashType algo) {
        this.algo = algo;
    }

    public String getErrorMsg() {
        return errorMsg;
    }

    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public TaskStatus getStatus() {
        return status;
    }

    public void setStatus(TaskStatus status) {
        this.status = status;
    }
}
