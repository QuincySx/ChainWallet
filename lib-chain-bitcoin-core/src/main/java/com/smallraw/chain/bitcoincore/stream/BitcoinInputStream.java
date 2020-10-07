package com.smallraw.chain.bitcoincore.stream;

import java.io.ByteArrayInputStream;
import java.io.EOFException;

public final class BitcoinInputStream extends ByteArrayInputStream {
    public BitcoinInputStream(byte[] buf) {
        super(buf);
    }

    @SuppressWarnings("unused")
    public BitcoinInputStream(byte[] buf, int offset, int length) {
        super(buf, offset, length);
    }

    /**
     * Little-endian（小端序）
     *
     * @return
     * @throws EOFException
     */
    public int readInt8() throws EOFException {
        return (readByte() & 0xff);
    }

    /**
     * Little-endian（小端序）
     *
     * @return
     * @throws EOFException
     */
    public int readInt16() throws EOFException {
        return (readByte() & 0xff) | ((readByte() & 0xff) << 8);
    }

    /**
     * Big-endian（大端序）
     *
     * @return
     * @throws EOFException
     */
    public int readIntBE16() throws EOFException {
        return (readByte() & 0xff) << 8 | ((readByte() & 0xff));
    }

    /**
     * Little-endian（小端序）
     *
     * @return
     * @throws EOFException
     */
    public int readInt32() throws EOFException {
        return (readByte() & 0xff) | ((readByte() & 0xff) << 8) | ((readByte() & 0xff) << 16) | ((readByte() & 0xff) << 24);
    }

    /**
     * Big-endian（大端序）
     *
     * @return
     * @throws EOFException
     */
    public int readIntBE32() throws EOFException {
        return ((readByte() & 0xff) << 24 | ((readByte() & 0xff) << 16) | ((readByte() & 0xff) << 8) | (readByte() & 0xff));
    }

    /**
     * Little-endian（小端序）
     *
     * @return
     * @throws EOFException
     */
    public long readInt64() throws EOFException {
        return (readInt32() & 0xFFFFFFFFL) | ((readInt32() & 0xFFFFFFFFL) << 32);
    }

    /**
     * Big-endian（大端序）
     *
     * @return
     * @throws EOFException
     */
    public long readIntBE64() throws EOFException {
        return (readIntBE32() & 0xFFFFFFFFL) << 32 | ((readIntBE32() & 0xFFFFFFFFL));
    }

    public int readByte() throws EOFException {
        int readedByte = super.read();
        if (readedByte == -1) {
            throw new EOFException();
        }
        return readedByte;
    }

    public Boolean readBoolean() throws EOFException {
        int readedByte = super.read();
        if (readedByte == -1) {
            throw new EOFException();
        }
        return readedByte != 0;
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

    public byte[] readBytes(final int count) throws EOFException {
        byte[] buf = new byte[count];
        int off = 0;
        while (off != count) {
            int bytesReadCurr = read(buf, off, count - off);
            if (bytesReadCurr == -1) {
                throw new EOFException();
            } else {
                off += bytesReadCurr;
            }
        }
        return buf;
    }
}
