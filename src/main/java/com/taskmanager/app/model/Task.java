package com.taskmanager.app.model;

import com.taskmanager.app.util.UUIDUtility;
import com.taskmanager.app.util.WorkerNumberGenerator;

import javax.persistence.*;
import java.sql.Blob;
import java.util.Date;

/**
 * Entity for Task
 */
@Entity
public class Task {

    /**
     * Uuid
     */
    @Id
    private String id;

    /**
     * Date
     */
    private Date createdAt;

    /**
     * User id
     */
    private String userId;

    /**
     * Src
     */
    private String src;

    /**
     * Algoritm
     */
    private String algo;

    /**
     * Data
     */
    @Lob
    @Column(length=100000)
    private byte[] data;

    /**
     * Status
     */
    private String status;

    /**
     * Error message
     */
    private String errorMsg;

    /**
     * Time execute
     */
    private long time;


    /**
     * Result
     */
    private String result;

    /**
     * Worker number
     */
    private int workerNumber;

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
            id = UUIDUtility.newUuid();
        }
        createdAt = new Date();
        workerNumber = WorkerNumberGenerator.generate();
    }

    /**
     * Gets Uuid
     *
     * @return Value of Uuid.
     */
    public String getId() {
        return id;
    }

    /**
     * Gets CreateAt
     * @return Value of CreateAt.
     */
    public Date getCreatedAt() {
        return createdAt;
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

    public String getAlgo() {
        return algo;
    }

    public HashType getHashType() {
        return HashType.valueOf(algo);
    }

    public void setAlgo(String algo) {
        this.algo = algo;
    }

    public byte[] getData() {
        return data;
    }

    public void setData(byte[] data) {
        this.data = data;
    }

    public String getStatus() {
        return status;
    }

    public TaskStatus getTaskStatus() {
        return TaskStatus.valueOf(status);
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setTaskStatus(TaskStatus status) {
        this.status = status.name();
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

    public void applyTime(long time) {
        this.time += time;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public int getWorkerNumber() {
        return workerNumber;
    }
}
