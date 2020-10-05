package com.smallraw.chain.bitcoin.utils;

import java.io.IOException;
import java.io.InputStream;

public final class IntUtil {
    /**
     * Bit masks (Low-order bit is bit 0 and high-order bit is bit 7)
     */
    private static final int bitMask[] = {0x01, 0x02, 0x04, 0x08, 0x10, 0x20, 0x40, 0x80};

    /**
     * Checks if the specified bit is set
     *
     * @param data  Byte array to check
     * @param index Bit position
     * @return TRUE if the bit is set
     */
    public static boolean checkBitLE(byte[] data, int index) {
        return (data[index >>> 3] & bitMask[7 & index]) != 0;
    }

    /**
     * Sets the specified bit
     *
     * @param data  Byte array
     * @param index Bit position
     */
    public static void setBitLE(byte[] data, int index) {
        data[index >>> 3] |= bitMask[7 & index];
    }


    /**
     * Form a long value from a 4-byte array in big-endian format
     *
     * @param bytes  The byte array
     * @param offset Starting offset within the array
     * @return The long value
     */
    public static long readUint32BE(byte[] bytes, int offset) {
        return (((long) bytes[offset++] & 0x00FFL) << 24) |
                (((long) bytes[offset++] & 0x00FFL) << 16) |
                (((long) bytes[offset++] & 0x00FFL) << 8) |
                ((long) bytes[offset] & 0x00FFL);
    }

    /**
     * Parse 2 bytes from the stream as unsigned 16-bit integer in little endian format.
     */
    public static int readUint16FromStream(InputStream is) {
        try {
            return (is.read() & 0xff) |
                    ((is.read() & 0xff) << 8);
        } catch (IOException x) {
            throw new RuntimeException(x);
        }
    }

    /**
     * Parse 4 bytes from the stream as unsigned 32-bit integer in little endian format.
     */
    public static long readUint32FromStream(InputStream is) {
        try {
            return (is.read() & 0xffl) |
                    ((is.read() & 0xffl) << 8) |
                    ((is.read() & 0xffl) << 16) |
                    ((is.read() & 0xffl) << 24);
        } catch (IOException x) {
            throw new RuntimeException(x);
        }
    }

    public static byte[] intToByteArray(int value) {
        return new byte[]{(byte) (value >> 24), (byte) (value >> 16), (byte) (value >> 8), (byte) value};
    }

    public static int byteArrayToUInt16LE(byte[] data) {
        return (data[0] & 0xff) | ((data[1] & 0xff) << 8);
    }

    public static int intFromBytes(byte b1, byte b2, byte b3, byte b4) {
        return b1 << 24 | (b2 & 255) << 16 | (b3 & 255) << 8 | b4 & 255;
    }
}
