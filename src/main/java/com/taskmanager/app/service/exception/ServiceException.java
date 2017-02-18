package com.taskmanager.app.service.exception;

/**
 * Exception for service layer
 */
public class ServiceException extends Exception {

    /**
     * Create ServiceException
     * @param msg message
     */
    public ServiceException(String msg) {
        super(msg);
    }

    /**
     * Create ServiceException
     * @param msg message
     * @param t {@link Throwable}
     */
    public ServiceException(String msg, Throwable t) {
        super(msg, t);
    }
}
