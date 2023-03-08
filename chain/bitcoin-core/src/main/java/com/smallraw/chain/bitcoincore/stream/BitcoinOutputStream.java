package com.smallraw.chain.bitcoincore.stream;

import com.smallraw.crypto.core.stream.ByteWriterStream;

import static com.smallraw.chain.bitcoincore.script.OpCodesKt.OP_PUSHDATA1;
import static com.smallraw.chain.bitcoincore.script.OpCodesKt.OP_PUSHDATA2;
import static com.smallraw.chain.bitcoincore.script.OpCodesKt.OP_PUSHDATA4;

public final class BitcoinOutputStream implements AutoCloseable {
    private final ByteWriterStream writerStream;

    public BitcoinOutputStream() {
        writerStream = new ByteWriterStream();
    }

    public BitcoinOutputStream(int size) {
        writerStream = new ByteWriterStream(size);
    }

    public BitcoinOutputStream writeInt8(int value) {
        writerStream.writeByte(value);
        return this;
    }

    public BitcoinOutputStream writeInt16(int value) {
        writerStream.writeInt16LE(value);
        return this;
    }

    public BitcoinOutputStream writeInt32(int value) {
        writerStream.writeInt32LE(value);
        return this;
    }

    public BitcoinOutputStream writeInt64(long value) {
        writerStream.writeInt64LE(value);
        return this;
    }

    public BitcoinOutputStream writeVarInt(long value) {
        if (value < 0xfd) {
            writeByte((byte) (value & 0xff));
        } else if (value < 0xffff) {
            writeByte((byte) 0xfd);
            writeInt16((int) value);
        } else if (value < 0xffffffffL) {
            writeByte((byte) 0xfe);
            writeInt32((int) value);
        } else {
            writeByte((byte) 0xff);
            writeInt64(value);
        }
        return this;
    }

    /**
     * 直接写入字节
     *
     * @param b 字节
     */
    public BitcoinOutputStream writeByte(byte b) {
        writerStream.writeByte(b);
        return this;
    }

    /**
     * 直接写入字节数组
     *
     * @param bytes 字节数组
     */
    public BitcoinOutputStream writeBytes(byte[] bytes) {
        writerStream.writeBytes(bytes);
        return this;
    }

    /**
     * 写入脚本字节数组，会添加 OP 操作符
     *
     * @param bytes 脚本字节数组
     */
    public BitcoinOutputStream writeScriptBytes(byte[] bytes) {
        if (bytes.length < OP_PUSHDATA1) {
            writeInt8(bytes.length);
        } else if (bytes.length < 0xff) {
            writeByte(OP_PUSHDATA1);
            writeInt8(bytes.length);
        } else if (bytes.length < 0xffff) {
            writeByte(OP_PUSHDATA2);
            writeInt16(bytes.length);
        } else {
            writeByte(OP_PUSHDATA4);
            writeInt32(bytes.length);
        }
        writerStream.writeBytes(bytes);
        return this;
    }

    public byte[] toByteArray() {
        return writerStream.toBytes();
    }

    @Override
    public void close() {
        writerStream.close();
    }
}
