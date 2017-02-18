package com.taskmanager.app.util;

import java.util.Random;

/**
 * Generator for workerNumbers
 */
public class WorkerNumberGenerator {

    private static final int MAX_WORKERS = 1;

    private static Random random = new Random();

    public static int generate() {
        //return random.nextInt(MAX_WORKERS);
        return 1;
    }
}
