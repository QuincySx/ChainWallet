package com.smallraw.core.chain.database.util

import androidx.room.TypeConverter
import kotlinx.datetime.Instant
import java.math.BigInteger

class InstantConverter {
    @TypeConverter
    fun longToInstant(value: Long?): Instant? =
        value?.let(Instant::fromEpochMilliseconds)

    @TypeConverter
    fun instantToLong(instant: Instant?): Long? =
        instant?.toEpochMilliseconds()
}

class ByteArrayConverter {
    @TypeConverter
    fun byteArrayToString(value: ByteArray?): String? =
        value?.let { String(it) }

    @TypeConverter
    fun stringToByteArray(value: String?): ByteArray? =
        value?.let { it.toByteArray() }
}

class BigIntegerConverter {
    @TypeConverter
    fun bigIntegerToString(value: BigInteger?): String? =
        value?.let { it.toString() }

    @TypeConverter
    fun stringToBigInteger(value: String?): java.math.BigInteger? =
        value?.let { java.math.BigInteger(it) }
}