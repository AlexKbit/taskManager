package com.taskmanager.app.util;

import java.util.UUID;

/**
 * Utility for UUID
 */
public class UUIDUtility {

    /**
     * Create new UUID
     * @return UUID
     */
    public static String newUuid() {
        return UUID.randomUUID().toString().replace("-", "");
    }
}
