package com.smallraw.chain.lib.core.stream

import org.junit.Assert
import org.junit.Test

class ByteReaderStreamAndWriterUnitTest {
    @Test
    fun test_reader() {
        val byteWriter = ByteWriterStream()
        byteWriter.writeByte(0)
        byteWriter.writeBoolean(true)
        byteWriter.writeInt16LE(16)
        byteWriter.writeInt16BE(61)
        byteWriter.writeInt32LE(32)
        byteWriter.writeInt32BE(23)
        byteWriter.writeInt64LE(64)
        byteWriter.writeInt64BE(46)
        byteWriter.writeBytes(byteArrayOf(0, 1, 2))
        byteWriter.writeString("123")
        byteWriter.writeRawStringUtf8("123")
        val toBytes = byteWriter.toBytes()

        val byteReader = ByteReaderStream(toBytes)
        Assert.assertEquals(byteReader.readByte(), 0.toByte())
        Assert.assertEquals(byteReader.readBoolean(), true)
        Assert.assertEquals(byteReader.readInt16LE(), 16)
        Assert.assertEquals(byteReader.readInt16BE(), 61)
        Assert.assertEquals(byteReader.readInt32LE(), 32)
        Assert.assertEquals(byteReader.readInt32BE(), 23)
        Assert.assertEquals(byteReader.readInt64LE(), 64)
        Assert.assertEquals(byteReader.readInt64BE(), 46)
        Assert.assertArrayEquals(byteReader.readBytes(3), byteArrayOf(0, 1, 2))
        Assert.assertEquals(byteReader.readString(), "123")
        Assert.assertEquals(byteReader.readRawStringUtf8(), "123")
    }

    @Test
    fun test_reader_reset() {
        val byteWriter = ByteWriterStream()
        byteWriter.writeBoolean(true)
        val toBytes = byteWriter.toBytes()

        val byteReader = ByteReaderStream(toBytes)
        Assert.assertEquals(byteReader.readBoolean(), true)

        byteReader.reset()
        Assert.assertEquals(byteReader.readBoolean(), true)
    }

    @Test
    fun test_reader_mark() {
        val byteWriter = ByteWriterStream()
        byteWriter.writeBoolean(true)
        byteWriter.writeInt32LE(32)
        val toBytes = byteWriter.toBytes()

        val byteReader = ByteReaderStream(toBytes)
        Assert.assertEquals(byteReader.readBoolean(), true)

        byteReader.mark()
        Assert.assertEquals(byteReader.readInt32LE(), 32)

        byteReader.reset()
        Assert.assertEquals(byteReader.readInt32LE(), 32)
    }

    @Test
    fun test_writer_mark() {
        val byteWriter = ByteWriterStream()
        byteWriter.writeByte(0)
        byteWriter.writeBoolean(true)

        byteWriter.mark()
        byteWriter.writeInt16LE(17)

        byteWriter.reset()
        byteWriter.writeInt16LE(16)

        val toBytes = byteWriter.toBytes()

        val byteReader = ByteReaderStream(toBytes)
        Assert.assertEquals(byteReader.readByte(), 0.toByte())
        Assert.assertEquals(byteReader.readBoolean(), true)
        Assert.assertEquals(byteReader.readInt16LE(), 16)
    }

    @Test
    fun test_writer_reset() {
        val byteWriter = ByteWriterStream()
        byteWriter.writeByte(0)
        byteWriter.writeBoolean(true)

        byteWriter.reset()
        byteWriter.writeInt16LE(16)

        val toBytes = byteWriter.toBytes()

        val byteReader = ByteReaderStream(toBytes)
        Assert.assertEquals(byteReader.readInt16LE(), 16)

        Assert.assertEquals(byteWriter.size(), 32)
    }
}