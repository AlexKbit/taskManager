package com.taskmanager.core.algorithms.api;

import com.taskmanager.model.HashType;

/**
 * Solver for hash
 */
public interface HashSolver {

    /**
     * Initialize solver
     * @param hashType type of algorithm [SHA-1; SHA-256; MD5]
     * @return
     */
    HashSolver build(HashType hashType);

    HashSolver update(byte[] buffer);

    HashType getType();

    String result2Hex();
}
