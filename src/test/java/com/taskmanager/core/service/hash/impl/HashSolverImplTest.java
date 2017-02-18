package com.taskmanager.core.service.hash.impl;

import com.taskmanager.core.service.hash.impl.api.HashSolver;
import com.taskmanager.core.service.hash.impl.impl.HashSolverImpl;
import com.taskmanager.model.HashType;
import org.junit.Test;

import java.security.NoSuchAlgorithmException;
import java.util.Random;

import static org.junit.Assert.*;

/**
 * Test for {@link HashSolverImplTest}
 */
public class HashSolverImplTest {

    /**
     * Size of byte array for hash
     */
    private static final int BYTE_SIZE = 1000;

    /**
     * Hash solver
     */
    private HashSolver hashSolver = new HashSolverImpl();

    @Test
    public void testSha1() throws NoSuchAlgorithmException {
        byte[] data = generateData();
        String hash1 = hashSolver.hash(HashType.SHA_1, data);
        String hash2 = hashSolver.hash(HashType.SHA_1, data);
        assertEquals(hash1, hash2);
    }

    @Test
    public void testSha256() throws NoSuchAlgorithmException {
        byte[] data = generateData();
        String hash1 = hashSolver.hash(HashType.SHA_256, data);
        String hash2 = hashSolver.hash(HashType.SHA_256, data);
        assertEquals(hash1, hash2);
    }

    @Test
    public void testMd5() throws NoSuchAlgorithmException {
        byte[] data = generateData();
        String hash1 = hashSolver.hash(HashType.MD5, data);
        String hash2 = hashSolver.hash(HashType.MD5, data);
        assertEquals(hash1, hash2);
    }

    @Test
    public void testNull() throws NoSuchAlgorithmException {
        byte[] data = generateData();
        String hash = hashSolver.hash(null, data);
        assertNull(hash);
    }


    private byte[] generateData() {
        byte[] data = new byte[BYTE_SIZE];
        new Random().nextBytes(data);
        return data;
    }
}