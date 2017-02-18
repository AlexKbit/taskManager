package com.taskmanager.app.core.service.load.impl;

import com.taskmanager.app.core.service.load.api.DataLoader;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.URL;
import java.nio.ByteBuffer;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.nio.channels.WritableByteChannel;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

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
     * Read bytes from file
     * @param path path to file(url or path)
     * @return bytes
     * @throws IOException
     */
    public byte[] read(String path) throws IOException {
        if (path.startsWith("file://")) {
            return readFromFile(path.substring(7));
        }
        return readFromURL(path);
    }

    private static byte[] readFromURL(String url) throws IOException {
        URL website = new URL(url);
        ReadableByteChannel inChannel = Channels.newChannel(website.openStream());
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        WritableByteChannel outChannel = Channels.newChannel(baos);

        ByteBuffer buffer = ByteBuffer.allocate(BUFFER_SIZE);
        int read;
        while ((read = inChannel.read(buffer)) > 0) {
            buffer.rewind();
            buffer.limit(read);
            while (read > 0) {
                read -= outChannel.write(buffer);
            }
            buffer.clear();
        }
        return baos.toByteArray();
    }

    private static byte[] readFromFile(String filename) throws IOException {
        Path path = Paths.get(filename);
        if (!path.toFile().exists()) {
            throw new IOException("File not found");
        }
        byte[] data = Files.readAllBytes(path);
        return data;
    }
}
