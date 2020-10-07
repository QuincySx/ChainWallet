/*
 The MIT License (MIT)

 Copyright (c) 2013 Valentin Konovalov

 Permission is hereby granted, free of charge, to any person obtaining a copy
 of this software and associated documentation files (the "Software"), to deal
 in the Software without restriction, including without limitation the rights
 to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 copies of the Software, and to permit persons to whom the Software is
 furnished to do so, subject to the following conditions:

 The above copyright notice and this permission notice shall be included in
 all copies or substantial portions of the Software.

 THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 THE SOFTWARE.*/

package com.smallraw.chain.bitcoincore.stream;

import java.io.ByteArrayOutputStream;
import java.io.EOFException;
import java.io.IOException;

import static com.smallraw.chain.bitcoincore.script.OpCodesKt.OP_PUSHDATA1;
import static com.smallraw.chain.bitcoincore.script.OpCodesKt.OP_PUSHDATA2;
import static com.smallraw.chain.bitcoincore.script.OpCodesKt.OP_PUSHDATA4;


public final class BitcoinOutputStream extends ByteArrayOutputStream {

    public BitcoinOutputStream() {
        super();
    }

    public BitcoinOutputStream(int size) {
        super(size);
    }

    public BitcoinOutputStream writeInt8(int value) {
        write(value & 0xff);
        return this;
    }

    public BitcoinOutputStream writeInt16(int value) {
        write(value & 0xff);
        write((value >> 8) & 0xff);
        return this;
    }

    /**
     * Big-endian（大端序）
     *
     * @return
     * @throws EOFException
     */
    public BitcoinOutputStream writeIntBE16(int value) {
        write((value >> 8) & 0xff);
        write(value & 0xff);
        return this;
    }

    public BitcoinOutputStream writeInt32(int value) {
        write(value & 0xff);
        write((value >> 8) & 0xff);
        write((value >> 16) & 0xff);
        write((value >>> 24) & 0xff);
        return this;
    }

    /**
     * Big-endian（大端序）
     *
     * @param value
     */
    public BitcoinOutputStream writeIntBE32(int value) {
        write((value >>> 24) & 0xff);
        write((value >> 16) & 0xff);
        write((value >> 8) & 0xff);
        write(value & 0xff);
        return this;
    }

    public BitcoinOutputStream writeInt64(long value) {
        writeInt32((int) (value & 0xFFFFFFFFL));
        writeInt32((int) ((value >>> 32) & 0xFFFFFFFFL));
        return this;
    }

    /**
     * Big-endian（大端序）
     *
     * @param value
     */
    public BitcoinOutputStream writeIntBE64(long value) {
        writeInt32((int) ((value >>> 32) & 0xFFFFFFFFL));
        writeInt32((int) (value & 0xFFFFFFFFL));
        return this;
    }

    public BitcoinOutputStream writeVarInt(long value) {
        if (value < 0xfd) {
            write((int) (value & 0xff));
        } else if (value < 0xffff) {
            write(0xfd);
            writeInt16((int) value);
        } else if (value < 0xffffffffL) {
            write(0xfe);
            writeInt32((int) value);
        } else {
            write(0xff);
            writeInt64(value);
        }
        return this;
    }

    public BitcoinOutputStream writeByte(byte b) throws IOException {
        write(b);
        return this;
    }

    public BitcoinOutputStream writeBytes(byte[] bytes) throws IOException {
        if (bytes.length < OP_PUSHDATA1) {
            writeInt8(bytes.length);
        } else if (bytes.length < 0xff) {
            write(OP_PUSHDATA1);
            writeInt8(bytes.length);
        } else if (bytes.length < 0xffff) {
            write(OP_PUSHDATA2);
            writeInt16(bytes.length);
        } else {
            write(OP_PUSHDATA4);
            writeInt32(bytes.length);
        }
        write(bytes);
        return this;
    }

    public BitcoinOutputStream writeBoolean(boolean bool) throws IOException {
        if (bool) {
            writeBytes(new byte[]{1});
        } else {
            writeBytes(new byte[]{0});
        }
        return this;
    }
}
