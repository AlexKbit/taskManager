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

    /**
     * Update hash
     * @param buffer new bytes
     * @return
     */
    HashSolver update(byte[] buffer);

    /**
     * Gets type of solver
     * @return
     */
    HashType getType();

    /**
     * Gets result in Hex
     * @return
     */
    String result2Hex();
}
