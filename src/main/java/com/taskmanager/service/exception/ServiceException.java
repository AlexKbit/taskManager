package com.taskmanager.service.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Exception for service layer
 */
@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
public class ServiceException extends RuntimeException {

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
