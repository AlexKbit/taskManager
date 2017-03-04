package com.taskmanager.rest;

import com.taskmanager.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

/**
 * REST controller for users
 */
@RestController
@RequestMapping("user")
public class UserRestController {

    /**
     * Logger
     */
    private static final Logger log = LoggerFactory.getLogger(UserRestController.class);

    /**
     * Get uuid for new user
     * @return {@link User}
     */
    @RequestMapping(value = "/new", method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public User get() {
        String uuid = UUID.randomUUID().toString();
        log.info("Get new uuid for user: {}", uuid);
        return new User(uuid);
    }
}
