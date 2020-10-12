package com.smallraw.crypto.core.stream;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;

/**
 * Thread insecurity
 */
public class ByteWriterStream implements AutoCloseable {
    private byte[] buf;
    private int index;
    private int markIndex;
    private int count;

    /**
     * 字节写入流
     */
    public ByteWriterStream() {
        this(32);
    }

    /**
     * 字节写入流
     *
     * @param capacity 初始化字符数组大小，写入数据太大时数组不够用时，会双倍扩容。
     */
    public ByteWriterStream(int capacity) {
        buf = new byte[capacity];
        index = 0;
        count = buf.length;
    }

    /**
     * 字节写入流
     * # 注意 buf 数组是外部的引用。
     *
     * @param buf 初始化字符数组，写入数据太大时数组不够用时，会双倍扩容。
     */
    public ByteWriterStream(byte[] buf) {
        this.buf = buf;
        index = 0;
        count = buf.length;
    }

    /**
     * 字符数组扩容
     *
     * @param capacity 本次写入需要的字节数
     */
    private void ensureCapacity(int capacity) {
        int size = 0;
        if (buf != null) {
            size = buf.length;
        }
        if (size - index < capacity) {
            byte[] temp = new byte[size * 2 + capacity];
            System.arraycopy(buf, 0, temp, 0, index);
            buf = temp;
            count = temp.length;
        }
    }

    /**
     * 写入一个字节(Byte)
     *
     * @param b 需要写入的字节
     */
    public final ByteWriterStream writeByte(byte b) {
        ensureCapacity(1);
        buf[index++] = b;
        return this;
    }

    /**
     * 写入一个字节(Byte)
     *
     * @param i 需要写入的数字，只会在 int 中读取一个字节写入。
     */
    public final ByteWriterStream writeByte(int i) {
        return writeByte((byte) (i & 0xFF));
    }

    /**
     * 写入多个字节(Byte)
     *
     * @param value 需要写入的字节数字。
     */
    public final ByteWriterStream writeBytes(byte[] value) {
        ensureCapacity(value.length);
        System.arraycopy(value, 0, buf, index, value.length);
        index += value.length;
        return this;
    }

    /**
     * 写入多个字节(Byte)
     *
     * @param value  需要写入的字节数字。
     * @param offset 需要写入的字节数字的起始位置。
     * @param length 需要写入的字节数字的长度。
     */
    public final ByteWriterStream writeBytes(byte[] value, int offset, int length) {
        ensureCapacity(length);
        System.arraycopy(value, offset, buf, index, length);
        index += length;
        return this;
    }

    /**
     * 写入一个布尔
     *
     * @param b 需要写入的数据。
     */
    public final ByteWriterStream writeBoolean(boolean b) {
        return writeByte(b ? (byte) 1 : (byte) 0);
    }

    /**
     * 小端序写入 Int16
     * Little-endian（小端序）
     *
     * @param value 需要写入的数据。
     */
    public final ByteWriterStream writeInt16LE(int value) {
        ensureCapacity(2);
        buf[index++] = (byte) (0xFF & (value));
        buf[index++] = (byte) (0xFF & (value >> 8));
        return this;
    }

    /**
     * 大端序写入 Int16
     * Big-endian（大端序）
     *
     * @param value 需要写入的数据。
     */
    public final ByteWriterStream writeInt16BE(int value) {
        ensureCapacity(2);
        buf[index++] = (byte) (0xFF & (value >> 8));
        buf[index++] = (byte) (0xFF & (value));
        return this;
    }

    /**
     * 小端序写入 Int32
     * Little-endian（小端序）
     *
     * @param value 需要写入的数据。
     */
    public final ByteWriterStream writeInt32LE(int value) {
        ensureCapacity(4);
        buf[index++] = (byte) (0xFF & (value));
        buf[index++] = (byte) (0xFF & (value >> 8));
        buf[index++] = (byte) (0xFF & (value >> 16));
        buf[index++] = (byte) (0xFF & (value >> 24));
        return this;
    }

    /**
     * 大端序写入 Int32
     * Big-endian（大端序）
     *
     * @param value 需要写入的数据。
     */
    public final ByteWriterStream writeInt32BE(int value) {
        ensureCapacity(4);
        buf[index++] = (byte) (0xFF & (value >> 24));
        buf[index++] = (byte) (0xFF & (value >> 16));
        buf[index++] = (byte) (0xFF & (value >> 8));
        buf[index++] = (byte) (0xFF & (value));
        return this;
    }

    /**
     * 小端序写入 Int64
     * Little-endian（小端序）
     *
     * @param value 需要写入的数据。
     */
    public final ByteWriterStream writeInt64LE(long value) {
        ensureCapacity(8);
        buf[index++] = (byte) (0xFFL & (value));
        buf[index++] = (byte) (0xFFL & (value >> 8));
        buf[index++] = (byte) (0xFFL & (value >> 16));
        buf[index++] = (byte) (0xFFL & (value >> 24));
        buf[index++] = (byte) (0xFFL & (value >> 32));
        buf[index++] = (byte) (0xFFL & (value >> 40));
        buf[index++] = (byte) (0xFFL & (value >> 48));
        buf[index++] = (byte) (0xFFL & (value >> 56));
        return this;
    }

    /**
     * 大端序写入 Int64
     * Big-endian（大端序）
     *
     * @param value 需要写入的数据。
     */
    public final ByteWriterStream writeInt64BE(long value) {
        ensureCapacity(8);
        buf[index++] = (byte) (0xFFL & (value >> 56));
        buf[index++] = (byte) (0xFFL & (value >> 48));
        buf[index++] = (byte) (0xFFL & (value >> 40));
        buf[index++] = (byte) (0xFFL & (value >> 32));
        buf[index++] = (byte) (0xFFL & (value >> 24));
        buf[index++] = (byte) (0xFFL & (value >> 16));
        buf[index++] = (byte) (0xFFL & (value >> 8));
        buf[index++] = (byte) (0xFFL & (value));
        return this;
    }

    /**
     * 写入字符串
     *
     * @param s 需要写入的数据。
     */
    public final ByteWriterStream writeString(String s) {
        byte[] bytes = s.getBytes();
        writeInt32LE(bytes.length);
        writeBytes(bytes);
        return this;
    }

    /**
     * 写入 UTF-8 字符串
     *
     * @param s 需要写入的数据。
     */
    public final ByteWriterStream writeRawStringUtf8(String s) {
        byte[] bytes = s.getBytes(StandardCharsets.UTF_8);
        writeInt32LE(bytes.length);
        writeBytes(bytes);
        return this;
    }

    /**
     * 获取写好数据的字符数组
     *
     * @return 字符数组
     */
    public final byte[] toBytes() {
        byte[] bytes = new byte[index];
        System.arraycopy(buf, 0, bytes, 0, index);
        return bytes;
    }

    /**
     * 获取写好数据的字符数组并写入 OutputStream
     *
     * @param out OutputStream
     */
    public final synchronized ByteWriterStream writeTo(OutputStream out) throws IOException {
        out.write(buf, 0, index);
        return this;
    }

    /**
     * 获取写好数据的字符数组并写入 ByteWriterStream
     *
     * @param write ByteWriterStream
     */
    public final synchronized ByteWriterStream writeTo(ByteWriterStream write) throws IOException {
        write.writeBytes(buf, 0, index);
        return this;
    }

    /**
     * 做标记，可以使用 reset() 回到 mark 标记的地方开始写入数据。
     */
    public final void mark() {
        mark(0);
    }

    /**
     * 做标记，可以使用 reset() 回到 mark 标记的地方开始写入数据。
     * # 此方法太过危险，请谨慎使用。
     *
     * @param offset 向后偏移几个字节,做标记。
     */
    @Deprecated
    public final void mark(int offset) {
        markIndex = index + offset;
    }

    /**
     * 如果使用 mark() 做了标记，则恢复到 mark 标记处，
     * 如果没有使用 mark() 标记，则恢复到字节开头处，可以重新写入。
     */
    public final void reset() {
        index = markIndex;
    }

    /**
     * 获取字节数组大小
     *
     * @return 字节数组大小
     */
    public final int size() {
        return count;
    }

    @Override
    public void close() {
    }
}
