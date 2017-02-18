package com.taskmanager.app.model;

/**
 * Created by Aleksandr on 09.07.2016.
 */
public enum TaskStatus {
    /**
     * Task wait a manager for work
     */
    WAIT,
    /**
     * Task in progress
     */
    IN_PROGRESS,
    /**
     * Task is done with result
     */
    SUCCESS,
    /**
     * Task failed and should exist error message
     */
    FAIL,
    /**
     * Task was stopped
     */
    STOP
}

