package com.smallraw.chain.lib.core.stream

import org.junit.Assert
import org.junit.Test

class ByteReaderAndWriterUnitTest {
    @Test
    fun test_reader() {
        val byteWriter = ByteWriter()
        byteWriter.putByte(0)
        byteWriter.putBoolean(true)
        byteWriter.putInt16LE(16)
        byteWriter.putInt16BE(61)
        byteWriter.putInt32LE(32)
        byteWriter.putInt32BE(23)
        byteWriter.putInt64LE(64)
        byteWriter.putInt64BE(46)
        byteWriter.putBytes(byteArrayOf(0, 1, 2))
        byteWriter.putString("123")
        byteWriter.putRawStringUtf8("123")
        val toBytes = byteWriter.toBytes()

        val byteReader = ByteReader(toBytes)
        Assert.assertEquals(byteReader.byte, 0.toByte())
        Assert.assertEquals(byteReader.boolean, true)
        Assert.assertEquals(byteReader.int16LE, 16)
        Assert.assertEquals(byteReader.int16BE, 61)
        Assert.assertEquals(byteReader.int32LE, 32)
        Assert.assertEquals(byteReader.int32BE, 23)
        Assert.assertEquals(byteReader.int64LE, 64)
        Assert.assertEquals(byteReader.int64BE, 46)
        Assert.assertArrayEquals(byteReader.getBytes(3), byteArrayOf(0, 1, 2))
        Assert.assertEquals(byteReader.string, "123")
        Assert.assertEquals(byteReader.rawStringUtf8, "123")
    }

    @Test
    fun test_reader_reset() {
        val byteWriter = ByteWriter()
        byteWriter.putBoolean(true)
        val toBytes = byteWriter.toBytes()

        val byteReader = ByteReader(toBytes)
        Assert.assertEquals(byteReader.boolean, true)

        byteReader.reset()
        Assert.assertEquals(byteReader.boolean, true)
    }

    @Test
    fun test_reader_mark() {
        val byteWriter = ByteWriter()
        byteWriter.putBoolean(true)
        byteWriter.putInt32LE(32)
        val toBytes = byteWriter.toBytes()

        val byteReader = ByteReader(toBytes)
        Assert.assertEquals(byteReader.boolean, true)

        byteReader.mark()
        Assert.assertEquals(byteReader.int32LE, 32)

        byteReader.reset()
        Assert.assertEquals(byteReader.int32LE, 32)
    }
}