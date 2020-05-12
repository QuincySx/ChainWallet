package com.smallraw.chain.lib.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public final class SHA256 {
    public static byte[] sha256(byte[] bytes) {
        return sha256(bytes, 0, bytes.length);
    }

    public static byte[] sha256(byte[] bytes, int offset, int size) {
        try {
            MessageDigest sha256Digest = MessageDigest.getInstance("SHA-256");
            sha256Digest.update(bytes, offset, size);
            byte[] sha256 = new byte[32];
            sha256Digest.digest(sha256);
            return sha256;
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static byte[] doubleSha256(byte[] bytes) {
        return doubleSha256(bytes, 0, bytes.length);
    }

    public static byte[] doubleSha256(byte[] bytes, int offset, int size) {
        try {
            MessageDigest sha256 = MessageDigest.getInstance("SHA-256");
            sha256.update(bytes, offset, size);
            return sha256.digest(sha256.digest());
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return null;
    }
}
