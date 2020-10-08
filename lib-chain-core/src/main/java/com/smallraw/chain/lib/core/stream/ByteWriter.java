package com.smallraw.chain.lib.core.stream;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;

final public class ByteWriter {

    private byte[] buf;
    private int index;

    public ByteWriter() {
        this(32);
    }

    public ByteWriter(int capacity) {
        buf = new byte[capacity];
        index = 0;
    }

    public ByteWriter(byte[] buf) {
        this.buf = buf;
        index = buf.length;
    }

    private void ensureCapacity(int capacity) {
        int size = 0;
        if (buf != null) {
            size = buf.length;
        }
        if (size - index < capacity) {
            byte[] temp = new byte[size * 2 + capacity];
            System.arraycopy(buf, 0, temp, 0, index);
            buf = temp;
        }
    }

    public void putByte(byte b) {
        ensureCapacity(1);
        buf[index++] = b;
    }

    public void putBoolean(boolean b) {
        putByte(b ? (byte) 1 : (byte) 0);
    }

    public void putInt16LE(short value) {
        ensureCapacity(2);
        buf[index++] = (byte) (0xFF & (value));
        buf[index++] = (byte) (0xFF & (value >> 8));
    }

    public void putInt16BE(short value) {
        ensureCapacity(2);
        buf[index++] = (byte) (0xFF & (value >> 8));
        buf[index++] = (byte) (0xFF & (value));
    }

    public void putInt32LE(int value) {
        ensureCapacity(4);
        buf[index++] = (byte) (0xFF & (value));
        buf[index++] = (byte) (0xFF & (value >> 8));
        buf[index++] = (byte) (0xFF & (value >> 16));
        buf[index++] = (byte) (0xFF & (value >> 24));
    }

    public void putInt32BE(int value) {
        ensureCapacity(4);
        buf[index++] = (byte) (0xFF & (value >> 24));
        buf[index++] = (byte) (0xFF & (value >> 16));
        buf[index++] = (byte) (0xFF & (value >> 8));
        buf[index++] = (byte) (0xFF & (value));
    }

    public void putInt64LE(long value) {
        ensureCapacity(8);
        buf[index++] = (byte) (0xFFL & (value));
        buf[index++] = (byte) (0xFFL & (value >> 8));
        buf[index++] = (byte) (0xFFL & (value >> 16));
        buf[index++] = (byte) (0xFFL & (value >> 24));
        buf[index++] = (byte) (0xFFL & (value >> 32));
        buf[index++] = (byte) (0xFFL & (value >> 40));
        buf[index++] = (byte) (0xFFL & (value >> 48));
        buf[index++] = (byte) (0xFFL & (value >> 56));
    }

    public void putInt64BE(long value) {
        ensureCapacity(8);
        buf[index++] = (byte) (0xFFL & (value >> 56));
        buf[index++] = (byte) (0xFFL & (value >> 48));
        buf[index++] = (byte) (0xFFL & (value >> 40));
        buf[index++] = (byte) (0xFFL & (value >> 32));
        buf[index++] = (byte) (0xFFL & (value >> 24));
        buf[index++] = (byte) (0xFFL & (value >> 16));
        buf[index++] = (byte) (0xFFL & (value >> 8));
        buf[index++] = (byte) (0xFFL & (value));
    }

    public void putBytes(byte[] value) {
        ensureCapacity(value.length);
        System.arraycopy(value, 0, buf, index, value.length);
        index += value.length;
    }

    public void putBytes(byte[] value, int offset, int length) {
        ensureCapacity(length);
        System.arraycopy(value, offset, buf, index, length);
        index += length;
    }

//   public void putCompactInt(long value) {
//      putBytes(CompactInt.toBytes(value));
//   }
//
//   public void putSha256Hash(Sha256Hash hash) {
//      putBytes(hash.getBytes());
//   }
//
//   public void putSha256Hash(Sha256Hash hash, boolean reverse) {
//      if (reverse) {
//         putBytes(BitUtils.reverseBytes(hash.getBytes()));
//      } else {
//         putBytes(hash.getBytes());
//      }
//   }

    public void putString(String s) {
        byte[] bytes = s.getBytes();
        putInt32LE(bytes.length);
        putBytes(bytes);
    }

    public void putRawStringUtf8(String s) {
        byte[] bytes = s.getBytes(StandardCharsets.UTF_8);
        putInt32LE(bytes.length);
        putBytes(bytes);
    }

    public byte[] toBytes() {
        byte[] bytes = new byte[index];
        System.arraycopy(buf, 0, bytes, 0, index);
        return bytes;
    }

    public void close() {
        Arrays.fill(buf, (byte) 0);
        index = 0;
        buf = null;
    }

    public int length() {
        return index;
    }
}
