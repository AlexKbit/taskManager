package com.taskmanager.core.algorithms;

import com.taskmanager.model.HashType;
import com.taskmanager.core.algorithms.api.HashSolver;
import com.taskmanager.service.exception.ServiceException;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Formatter;

/**
 * Implementation for {@link HashSolver}
 */
public class HashSolverImpl implements HashSolver {

    protected MessageDigest md;

    protected HashType type;

    @Override
    public HashSolverImpl build(HashType hashType) {
        this.type = hashType;
        try {
            this.md = MessageDigest.getInstance(toType(hashType));
        } catch (NoSuchAlgorithmException e) {
           throw new ServiceException("Unknown type for hash", e);
        }
        return this;
    }

    @Override
    public HashSolver update(byte[] buffer) {
        md.update(buffer);
        return this;
    }

    @Override
    public HashType getType() {
        return type;
    }

    @Override
    public String result2Hex() {
        try {
            switch (type) {
                case SHA_1:
                    return sha1(md.digest());
                case SHA_256:
                    return sha256(md.digest());
                case MD5:
                    return md5(md.digest());
                default:
                    throw new ServiceException("Unknown type for hash");
            }
        } catch (NoSuchAlgorithmException e) {
            throw new ServiceException("Unknown type for hash", e);
        }
    }

    private String toType(HashType type) {
        switch (type) {
            case SHA_1: return "SHA-1";
            case SHA_256: return "SHA-256";
            case MD5: return "MD5";
            default: throw new ServiceException("Unknown type for hash");
        }
    }

    private String md5(byte[] data) throws NoSuchAlgorithmException {
            BigInteger number = new BigInteger(1, data);
            String hashtext = number.toString(16);
            // We need to zero pad it if you actually want the full 32 chars.
            while (hashtext.length() < 32) {
                hashtext = "0" + hashtext;
            }
            return hashtext;
    }

    private String sha256(byte[] data) throws NoSuchAlgorithmException{
        return String.format("%064x", new java.math.BigInteger(1, data));
    }

    private String sha1(byte[] data) throws NoSuchAlgorithmException{
        return byteArray2Hex(data);
    }

    private String byteArray2Hex(final byte[] hash) {
        Formatter formatter = new Formatter();
        for (byte b : hash) {
            formatter.format("%02x", b);
        }
        return formatter.toString();
    }
}
