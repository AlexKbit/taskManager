package com.taskmanager.core.service.hash.impl;

import com.taskmanager.core.algorithms.HashSolverImpl;
import com.taskmanager.core.algorithms.api.HashSolver;
import com.taskmanager.model.HashType;
import org.junit.Test;

import java.security.NoSuchAlgorithmException;
import java.util.Random;

import static org.junit.Assert.assertEquals;

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
    private HashSolver hashSolver;

    @Test
    public void testSha1() throws NoSuchAlgorithmException {
        byte[] data = generateData();
        hashSolver = new HashSolverImpl();
        String hash1 = hashSolver.build(HashType.SHA_1).update(data).result2Hex();
        hashSolver = new HashSolverImpl();
        String hash2 = hashSolver.build(HashType.SHA_1).update(data).result2Hex();
        assertEquals(hash1, hash2);
    }

    @Test
    public void testSha256() throws NoSuchAlgorithmException {
        byte[] data = generateData();
        hashSolver = new HashSolverImpl();
        String hash1 = hashSolver.build(HashType.SHA_256).update(data).result2Hex();
        hashSolver = new HashSolverImpl();
        String hash2 = hashSolver.build(HashType.SHA_256).update(data).result2Hex();
        assertEquals(hash1, hash2);
    }

    @Test
    public void testMd5() throws NoSuchAlgorithmException {
        byte[] data = generateData();
        hashSolver = new HashSolverImpl();
        String hash1 = hashSolver.build(HashType.MD5).update(data).result2Hex();
        hashSolver = new HashSolverImpl();
        String hash2 = hashSolver.build(HashType.MD5).update(data).result2Hex();
        assertEquals(hash1, hash2);
    }

    private byte[] generateData() {
        byte[] data = new byte[BYTE_SIZE];
        new Random().nextBytes(data);
        return data;
    }
}