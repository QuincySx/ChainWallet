package com.smallraw.chain.lib.core.stream;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;

/**
 * Thread insecurity
 */
public class ByteReaderStream implements AutoCloseable {
    public static class InsufficientBytesException extends Exception {
        private static final long serialVersionUID = 1L;
    }

    private final byte[] buf;
    private int index;
    private int markIndex;

    /**
     * 字节读取流
     *
     * @param buf 需要读取的字节数组
     */
    public ByteReaderStream(byte[] buf) {
        this.buf = buf;
        index = 0;
        this.markIndex = 0;
    }

    /**
     * 字节读取流
     *
     * @param buf   需要读取的字节数组
     * @param index 从第 index 字节开始读取
     */
    public ByteReaderStream(byte[] buf, int index) {
        this.buf = buf;
        this.index = index;
        this.markIndex = index;
    }

    /**
     * 读取一个字节(Byte)
     *
     * @return Bytes
     * @throws InsufficientBytesException 资源不足，无法读取
     */
    public final byte readByte() throws InsufficientBytesException {
        checkAvailable(1);
        return buf[index++];
    }

    /**
     * 读取多个字节(Bytes)
     *
     * @return Bytes
     * @throws InsufficientBytesException 资源不足，无法读取
     */
    public final byte[] readBytes(int size) throws InsufficientBytesException {
        checkAvailable(size);
        byte[] bytes = new byte[size];
        System.arraycopy(buf, index, bytes, 0, size);
        index += size;
        return bytes;
    }

    /**
     * 读取 boolean
     *
     * @return boolean
     * @throws InsufficientBytesException 资源不足，无法读取
     */
    public final boolean readBoolean() throws InsufficientBytesException {
        return readByte() != 0;
    }

    /**
     * 小端序读取 Int16
     * Little-endian（小端序）
     *
     * @return int16
     * @throws InsufficientBytesException 资源不足，无法读取
     */
    public final int readInt16LE() throws InsufficientBytesException {
        checkAvailable(2);
        return (((buf[index++] & 0xFF)) |
                ((buf[index++] & 0xFF) << 8)) & 0xFFFF;
    }

    /**
     * 大端序读取 Int16
     * Big-endian（大端序）
     *
     * @return int16
     * @throws InsufficientBytesException 资源不足，无法读取
     */
    public final int readInt16BE() throws InsufficientBytesException {
        checkAvailable(2);
        return (((buf[index++] & 0xFF) << 8) |
                ((buf[index++] & 0xFF))) & 0xFFFF;
    }

    /**
     * 小端序读取 Int32
     * Little-endian（小端序）
     *
     * @return int32
     * @throws InsufficientBytesException 资源不足，无法读取
     */
    public final int readInt32LE() throws InsufficientBytesException {
        checkAvailable(4);
        return ((buf[index++] & 0xFF)) |
                ((buf[index++] & 0xFF) << 8) |
                ((buf[index++] & 0xFF) << 16)
                | ((buf[index++] & 0xFF) << 24);
    }

    /**
     * 大端序读取 Int32
     * Big-endian（大端序）
     *
     * @return int32
     * @throws InsufficientBytesException 资源不足，无法读取
     */
    public final int readInt32BE() throws InsufficientBytesException {
        checkAvailable(4);
        return ((buf[index++] & 0xFF) << 24) |
                ((buf[index++] & 0xFF) << 16) |
                ((buf[index++] & 0xFF) << 8) |
                ((buf[index++] & 0xFF));
    }

    /**
     * 小端序读取 Int64
     * Little-endian（小端序）
     *
     * @return int64
     * @throws InsufficientBytesException 资源不足，无法读取
     */
    public final long readInt64LE() throws InsufficientBytesException {
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

    /**
     * 大端序读取 Int64
     * Big-endian（大端序）
     *
     * @return int64
     * @throws InsufficientBytesException 资源不足，无法读取
     */
    public final long readInt64BE() throws InsufficientBytesException {
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

    /**
     * 读取字符串
     *
     * @return 读取字符串
     * @throws InsufficientBytesException 资源不足，无法读取
     */
    public final String readString() throws InsufficientBytesException {
        int length = readInt32LE();
        byte[] bytes = readBytes(length);
        return new String(bytes);
    }

    /**
     * 读取 UTF-8 格式的字符串
     *
     * @return 读取字符串
     * @throws InsufficientBytesException 资源不足，无法读取
     */
    public final String readRawStringUtf8() throws InsufficientBytesException {
        int length = readInt32LE();
        byte[] bytes = readBytes(length);
        return new String(bytes, StandardCharsets.UTF_8);
    }

    /**
     * 此方法太过危险，请谨慎使用。
     */
    @Deprecated
    public final void skip(int num) throws InsufficientBytesException {
        checkAvailable(num);
        index += num;
    }

    /**
     * 做标记，可以使用 reset() 回到 mark 标记的地方开始读取数据。
     */
    public final void mark() {
        mark(0);
    }

    /**
     * 做标记，可以使用 reset() 回到 mark 标记的地方开始读取数据。
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
     * 如果没有使用 mark() 标记，则恢复到字节开头处，可以重新读取。
     */
    public final void reset() {
        index = markIndex;
    }

    /**
     * 强行制定字节流读取的位置
     * # 此方法太过危险，请谨慎使用。
     */
    public final int getPosition() {
        return index;
    }

    /**
     * 强行制定字节流读取的位置
     * # 此方法太过危险，请谨慎使用。
     */
    @Deprecated
    public final void setPosition(int index) {
        this.index = index;
    }

    /**
     * 获得剩余可读字节数
     */
    public final int available() {
        return buf.length - index;
    }

    /**
     * 检查读取 num 个，剩余可读字节数是否足够
     *
     * @param num 字节数量
     */
    protected final void checkAvailable(int num) throws InsufficientBytesException {
        if (buf.length - index < num) {
            throw new InsufficientBytesException();
        }
    }

    @Override
    public void close() {
        Arrays.fill(buf, (byte) 0);
        index = 0;
    }
}
