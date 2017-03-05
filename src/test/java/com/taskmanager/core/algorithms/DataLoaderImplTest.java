package com.taskmanager.core.algorithms;

import com.taskmanager.core.algorithms.api.DataLoader;
import com.taskmanager.model.HashType;
import com.taskmanager.service.exception.ServiceException;
import org.junit.Test;

import static org.junit.Assert.assertNotNull;


/**
 * Test for {@link DataLoaderImpl}
 */
public class DataLoaderImplTest {

    private DataLoader dataLoader = new DataLoaderImpl();

    @Test
    public void testCorrectLoad() {
        String hash = dataLoader.solveHash("https://www.google.ru", HashType.MD5);
        assertNotNull(hash);
    }

    @Test(expected = ServiceException.class)
    public void testUnCorrectLoad() {
        dataLoader.solveHash("fail.url", HashType.MD5);
    }

}