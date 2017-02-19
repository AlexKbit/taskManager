package com.taskmanager.core.algorithms.api;

import com.taskmanager.model.HashType;

import java.io.IOException;

/**
 * Loader for remote files and solve hash
 */
public interface DataLoader {

    /**
     * Read bytes from file and solve hash
     * @param path url or path
     * @return hash
     * @throws IOException
     */
    String solveHash(String path, HashType type);
}
