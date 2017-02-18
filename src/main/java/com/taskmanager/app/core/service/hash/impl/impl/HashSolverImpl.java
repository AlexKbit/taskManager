package com.taskmanager.app.core.service.hash.impl.impl;

import com.taskmanager.app.core.service.hash.impl.api.HashSolver;
import com.taskmanager.app.model.HashType;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Formatter;

/**
 * Implementation for {@link HashSolver}
 */
@Service
public class HashSolverImpl implements HashSolver {

    /**
     * Sole hash for data
     * @param type type of algorithm [SHA-1; SHA-256; MD5]
     * @param data bytes
     * @return hash
     * @throws NoSuchAlgorithmException
     */
    public String hash(HashType type, byte[] data) throws NoSuchAlgorithmException {
        if (type == null) {
            return null;
        }
        switch (type) {
            case SHA_1: return sha1(data);
            case SHA_256: return sha256(data);
            case MD5: return md5(data);
        }
        return null;
    }

    private static String md5(byte[] data) throws NoSuchAlgorithmException {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] messageDigest = md.digest(data);
            BigInteger number = new BigInteger(1, messageDigest);
            String hashtext = number.toString(16);
            // We need to zero pad it if you actually want the full 32 chars.
            while (hashtext.length() < 32) {
                hashtext = "0" + hashtext;
            }
            return hashtext;
    }

    private static String sha256(byte[] data) throws NoSuchAlgorithmException{
        MessageDigest md = MessageDigest.getInstance("SHA-256");
        md.update(data);
        byte[] digest = md.digest();
        return String.format("%064x", new java.math.BigInteger(1, digest));
    }

    private static String sha1(byte[] data) throws NoSuchAlgorithmException{
        MessageDigest md = MessageDigest.getInstance("SHA-1");
        return byteArray2Hex(md.digest(data));
    }

    private static String byteArray2Hex(final byte[] hash) {
        Formatter formatter = new Formatter();
        for (byte b : hash) {
            formatter.format("%02x", b);
        }
        return formatter.toString();
    }
}
