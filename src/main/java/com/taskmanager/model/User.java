package com.taskmanager.model;

/**
 * User
 */
public class User {

    /**
     * User id(UUID)
     */
    private String id;

    /**
     * Create user
     * @param id
     */
    public User(String id) {
        this.id = id;
    }

    /**
     * Gets id
     * @return UUID
     */
    public String getId() {
        return id;
    }

    /**
     * Sets id
     * @param id UUID
     */
    public void setId(String id) {
        this.id = id;
    }
}
