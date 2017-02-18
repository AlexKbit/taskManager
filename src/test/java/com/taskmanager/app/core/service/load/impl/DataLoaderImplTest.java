package com.taskmanager.app.core.service.load.impl;

import com.taskmanager.app.core.service.load.api.DataLoader;
import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.*;

/**
 * Test for {@link DataLoaderImpl}
 */
public class DataLoaderImplTest {

    /**
     * Download google :)
     */
    private static final String IMAGE = "https://www.google.com/";

    /**
     * Data loader
     */
    private DataLoader dataLoader = new DataLoaderImpl();

    @Test
    public void testLoadByUrl() throws IOException {
        byte[] data = dataLoader.read(IMAGE);
        assertNotNull(data);
        assertTrue(data.length > 0);
    }

}