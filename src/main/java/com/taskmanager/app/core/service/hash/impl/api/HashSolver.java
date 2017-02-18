package com.taskmanager.app.core.service.hash.impl.api;

import com.taskmanager.app.model.HashType;

import java.security.NoSuchAlgorithmException;

/**
 * Solver for hash
 */
public interface HashSolver {

    /**
     * Sole hash for data
     * @param type type of algorithm [SHA-1; SHA-256; MD5]
     * @param data bytes
     * @return hash
     * @throws NoSuchAlgorithmException
     */
    String hash(HashType type, byte[] data) throws NoSuchAlgorithmException;
}
