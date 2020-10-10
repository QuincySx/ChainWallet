package com.smallraw.chain.bitcoincore.stream;

import com.smallraw.chain.lib.core.stream.ByteReaderStream;

import java.io.EOFException;

public final class BitcoinInputStream implements AutoCloseable {
    private final ByteReaderStream readerStream;

    public BitcoinInputStream(byte[] buf) {
        readerStream = new ByteReaderStream(buf);
    }

    public int readByte() throws EOFException {
        return readerStream.readByte() & 0xFF;
    }

    public int readBytes(byte[] buf, int off, int len) throws EOFException {
        return readerStream.readBytes(buf, off, len - off);
    }

    public byte[] readBytes(final int count) throws EOFException {
        byte[] buf = new byte[count];
        int off = 0;
        while (off != count) {
            int bytesReadCurr = readerStream.readBytes(buf, off, count - off);
            if (bytesReadCurr == -1) {
                throw new EOFException();
            } else {
                off += bytesReadCurr;
            }
        }
        return buf;
    }

    /**
     * Little-endian（小端序）
     *
     * @return
     * @throws EOFException
     */
    public int readInt8() throws EOFException {
        return readerStream.readByte() & 0xff;
    }

    /**
     * Little-endian（小端序）
     *
     * @return
     * @throws EOFException
     */
    public int readInt16() throws EOFException {
        return readerStream.readInt16LE();
    }

    /**
     * Little-endian（小端序）
     *
     * @return
     * @throws EOFException
     */
    public int readInt32() throws EOFException {
        return readerStream.readInt32LE();
    }

    /**
     * Little-endian（小端序）
     *
     * @return
     * @throws EOFException
     */
    public long readInt64() throws EOFException {
        return readerStream.readInt64LE();
    }

    public long readVarInt() throws EOFException {
        int readedByte = readByte();
        if (readedByte < 0xfd) {
            return readedByte;
        } else if (readedByte == 0xfd) {
            return readInt16();
        } else if (readedByte == 0xfe) {
            return readInt32();
        } else {
            return readInt64();
        }
    }

    public final int available() {
        return readerStream.available();
    }

    public final void mark() {
        readerStream.mark();
    }

    public final void reset() {
        readerStream.reset();
    }

    @Override
    public void close() throws Exception {
        readerStream.close();
    }
}
