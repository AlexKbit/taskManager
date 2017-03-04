package com.taskmanager.core.algorithms;

import com.taskmanager.model.HashType;
import com.taskmanager.core.algorithms.api.DataLoader;
import com.taskmanager.core.algorithms.api.HashSolver;
import com.taskmanager.service.exception.ServiceException;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URL;
import java.nio.ByteBuffer;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;

/**
 * Implementation of {@link DataLoader}
 */
@Service
public class DataLoaderImpl implements DataLoader {

    /**
     * Buffer size
     */
    public static int BUFFER_SIZE = 8192;

    /**
     * Hash solver
     */
    private HashSolver hashSolver;

    /**
     * {@inheritDoc}
     */
    public String solveHash(String path, HashType type) {
        hashSolver = new HashSolverImpl().build(type);
        try {
            return loadAndSolveHash(path);
        } catch (IOException e) {
            throw new ServiceException("Error in process of load file by url", e);
        }
    }

    private String loadAndSolveHash(String url) throws IOException {
        URL website = new URL(url);
        ReadableByteChannel inChannel = Channels.newChannel(website.openStream());
        ByteBuffer buffer = ByteBuffer.allocate(BUFFER_SIZE);
        int read;
        while ((read = inChannel.read(buffer)) > 0) {
            buffer.rewind();
            buffer.limit(read);
            if (read > 0) {
                hashSolver.update(buffer.array());
            }
            buffer.clear();
        }
        return hashSolver.result2Hex();
    }
}
