package com.smallraw.chain.lib.core.stream;

import java.nio.charset.StandardCharsets;

final public class ByteReader {
    public static class InsufficientBytesException extends Exception {
        private static final long serialVersionUID = 1L;
    }

    private final byte[] buf;
    private int index;
    private int markIndex;

    public ByteReader(byte[] buf) {
        this.buf = buf;
        index = 0;
    }

    public ByteReader(byte[] buf, int index) {
        this.buf = buf;
        this.index = index;
    }

    public byte getByte() throws InsufficientBytesException {
        checkAvailable(1);
        return buf[index++];
    }

    public boolean getBoolean() throws InsufficientBytesException {
        return getByte() != 0;
    }

    public int getInt16LE() throws InsufficientBytesException {
        checkAvailable(2);
        return (((buf[index++] & 0xFF)) |
                ((buf[index++] & 0xFF) << 8)) & 0xFFFF;
    }

    public int getInt16BE() throws InsufficientBytesException {
        checkAvailable(2);
        return (((buf[index++] & 0xFF) << 8) |
                ((buf[index++] & 0xFF))) & 0xFFFF;
    }

    public int getInt32LE() throws InsufficientBytesException {
        checkAvailable(4);
        return ((buf[index++] & 0xFF)) |
                ((buf[index++] & 0xFF) << 8) |
                ((buf[index++] & 0xFF) << 16)
                | ((buf[index++] & 0xFF) << 24);
    }

    public int getInt32BE() throws InsufficientBytesException {
        checkAvailable(4);
        return ((buf[index++] & 0xFF) << 24) |
                ((buf[index++] & 0xFF) << 16) |
                ((buf[index++] & 0xFF) << 8) |
                ((buf[index++] & 0xFF));
    }

    public long getInt64LE() throws InsufficientBytesException {
        checkAvailable(8);
        return ((buf[index++] & 0xFFL)) |
                ((buf[index++] & 0xFFL) << 8) |
                ((buf[index++] & 0xFFL) << 16) |
                ((buf[index++] & 0xFFL) << 24) |
                ((buf[index++] & 0xFFL) << 32) |
                ((buf[index++] & 0xFFL) << 40) |
                ((buf[index++] & 0xFFL) << 48) |
                ((buf[index++] & 0xFFL) << 56);
    }

    public long getInt64BE() throws InsufficientBytesException {
        checkAvailable(4);
        return ((buf[index++] & 0xFFL) << 56) |
                ((buf[index++] & 0xFFL) << 48) |
                ((buf[index++] & 0xFFL) << 40) |
                ((buf[index++] & 0xFFL) << 32) |
                ((buf[index++] & 0xFFL) << 24) |
                ((buf[index++] & 0xFFL) << 16) |
                ((buf[index++] & 0xFFL) << 8) |
                ((buf[index++] & 0xFFL));
    }

    public byte[] getBytes(int size) throws InsufficientBytesException {
        checkAvailable(size);
        byte[] bytes = new byte[size];
        System.arraycopy(buf, index, bytes, 0, size);
        index += size;
        return bytes;
    }

    public String getString() throws InsufficientBytesException {
        int length = getInt32LE();
        byte[] bytes = getBytes(length);
        return new String(bytes);
    }

    public String getRawStringUtf8() throws InsufficientBytesException {
        int length = getInt32LE();
        byte[] bytes = getBytes(length);
        return new String(bytes, StandardCharsets.UTF_8);
    }

    public void skip(int num) throws InsufficientBytesException {
        checkAvailable(num);
        index += num;
    }

    public void mark() {
        mark(0);
    }

    /**
     * 此方法太过危险，请谨慎使用。
     */
    @Deprecated
    public void mark(int offset) {
        markIndex = index + offset;
    }

    public void reset() {
        index = markIndex;
    }

//   public long getCompactInt() throws InsufficientBytesException {
//      return CompactInt.fromByteReader(this);
//   }
//
//   public Sha256Hash getSha256Hash() throws InsufficientBytesException {
//      checkAvailable(Sha256Hash.HASH_LENGTH);
//      return Sha256Hash.of(getBytes(Sha256Hash.HASH_LENGTH));
//   }

    @Deprecated
    public int getPosition() {
        return index;
    }

    /**
     * 此方法太过危险，请谨慎使用。
     */
    @Deprecated
    public void setPosition(int index) {
        this.index = index;
    }

    public final int available() {
        return buf.length - index;
    }

    private void checkAvailable(int num) throws InsufficientBytesException {
        if (buf.length - index < num) {
            throw new InsufficientBytesException();
        }
    }
}
