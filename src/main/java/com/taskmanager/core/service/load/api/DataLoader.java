package com.taskmanager.core.service.load.api;

import java.io.IOException;

/**
 * Loader for local and remote files
 */
public interface DataLoader {

    /**
     * Read bytes from file
     * @param path path to file(url or path)
     * @return bytes
     * @throws IOException
     */
    byte[] read(String path) throws IOException;
}
