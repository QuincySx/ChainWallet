package com.smallraw.chain.eos.model.serializer

import com.smallraw.chain.eos.model.BaseVo
import com.smallraw.chain.eos.utils.ByteBuffer
import com.smallraw.chain.eos.utils.ByteUtils
import com.smallraw.crypto.core.extensions.hexToByteArray
import java.util.*

object ObjectUtils {
    private fun getFieldValueByName(fieldName: String, o: Any): Any? {
        return try {
            val firstLetter = fieldName.substring(0, 1).toUpperCase()
            val getter = "get" + firstLetter + fieldName.substring(1)
            val method = o.javaClass.getMethod(getter, *arrayOf())
            method.invoke(o)
        } catch (e: Exception) {
            null
        }
    }

    /**
     * Bean2Map
     *
     * @param obj
     * @return
     */
    fun Bean2Map(obj: Any?): Map<String, Any?>? {
        if (obj == null) {
            return null
        }
        val map: MutableMap<String, Any?> = LinkedHashMap()
        val fields = obj.javaClass.declaredFields
        for (i in fields.indices) {
            map[fields[i].name] = getFieldValueByName(fields[i].name, obj)
        }
        return map
    }

    fun writeBytes(vo: Any?, bf: ByteBuffer) {
        var params: Map<String, Any?>?
        if (vo is Map<*, *>) {
            params = vo as Map<String, Any?>?
        } else {
            params = Bean2Map(vo)
        }
        val objMap: MutableMap<String, Any> = LinkedHashMap()
        for (key in params!!.keys) {
            val obj = params[key]
            if (obj is BaseVo || obj is List<*> || obj is Map<*, *>) {
                if ("authorization" == key) {
                    bf.concat(ByteUtils.writerVarint32((obj as List<*>).size.toString()))
                    for (ob in obj) {
                        writeBytes(ob, bf)
                    }
                } else if ("data" == key) {
                    val databf = ByteBuffer()
                    writeBytes(obj, databf)
                    bf.concat(ByteUtils.writerVarint32(databf.buffer.size.toString()))
                    bf.concat(databf.buffer)
                } else if ("transaction_extensions" == key) {
                } else {
                    objMap[key] = obj
                }
            } else {
                if ("chain_id" == key) {
                    bf.concat(obj.toString().hexToByteArray())
                } else if ("expiration" == key) {
                    bf.concat(ByteUtils.writerUnit32(obj.toString()))
                } else if ("ref_block_num" == key) {
                    bf.concat(ByteUtils.writerUnit16(obj.toString()))
                } else if ("ref_block_prefix" == key) {
                    bf.concat(ByteUtils.writerUnit32(obj.toString()))
                } else if ("net_usage_words" == key) {
                    bf.concat(ByteUtils.writerVarint32(obj.toString()))
                } else if ("max_cpu_usage_ms" == key) {
                    bf.concat(ByteUtils.writerUnit8(obj.toString()))
                } else if ("delay_sec" == key) {
                    bf.concat(ByteUtils.writerVarint32(obj.toString()))
                } else if ("account" == key) {
                    bf.concat(ByteUtils.writeName(obj.toString()))
                } else if ("name" == key) {
                    bf.concat(ByteUtils.writeName(obj.toString()))
                } else if ("actor" == key) {
                    bf.concat(ByteUtils.writeName(obj.toString()))
                } else if ("permission" == key) {
                    bf.concat(ByteUtils.writeName(obj.toString()))
                } else if ("from" == key) {
                    bf.concat(ByteUtils.writeName(obj.toString()))
                } else if ("to" == key) {
                    bf.concat(ByteUtils.writeName(obj.toString()))
                } else if ("quantity" == key) {
                    bf.concat(ByteUtils.writerAsset(obj.toString()))
                } else if ("memo" == key) {
                    bf.concat(ByteUtils.writerString(obj.toString()))
                } else if ("creator" == key) {
                    bf.concat(ByteUtils.writeName(obj.toString()))
                } else if ("owner" == key) {
                    bf.concat(ByteUtils.writerKey(obj.toString()))
                } else if ("active" == key) {
                    bf.concat(ByteUtils.writerKey(obj.toString()))
                } else if ("payer" == key) {
                    bf.concat(ByteUtils.writeName(obj.toString()))
                } else if ("receiver" == key) {
                    bf.concat(ByteUtils.writeName(obj.toString()))
                } else if ("bytes" == key) {
                    bf.concat(ByteUtils.writerUnit32(obj.toString()))
                } else if ("stake_net_quantity" == key) {
                    bf.concat(ByteUtils.writerAsset(obj.toString()))
                } else if ("stake_cpu_quantity" == key) {
                    bf.concat(ByteUtils.writerAsset(obj.toString()))
                } else if ("transfer" == key) {
                    bf.concat(ByteUtils.writerUnit8(obj.toString()))
                } else if ("voter" == key) {
                    bf.concat(ByteUtils.writeName(obj.toString()))
                } else if ("proxy" == key) {
                    bf.concat(ByteUtils.writeName(obj.toString()))
                } else if ("producer" == key) {
                    bf.concat(ByteUtils.writeName(obj.toString()))
                } else if ("close-owner" == key) {
                    bf.concat(ByteUtils.writeName(obj.toString()))
                } else if ("close-symbol" == key) {
                    bf.concat(ByteUtils.writerSymbol(obj.toString()))
                }
            }
        }
        for (key in objMap.keys) {
            val obj = params[key]
            if ("context_free_actions" == key) {
                bf.concat(ByteUtils.writerVarint32((obj as List<*>?)!!.size.toString()))
                for (ob in obj!!) {
                    writeBytes(ob, bf)
                }
            } else if ("actions" == key) {
                bf.concat(ByteUtils.writerVarint32((obj as List<*>?)!!.size.toString()))
                for (ob in obj!!) {
                    writeBytes(ob, bf)
                }
            } else if ("producers" == key) {
                bf.concat(ByteUtils.writerVarint32((obj as List<*>?)!!.size.toString()))
                for (ob in obj!!) {
                    val mp: MutableMap<String, Any> = HashMap()
                    mp["producer"] = ob as Any
                    writeBytes(mp, bf)
                }
            } else {
                writeBytes(obj, bf)
            }
        }
    }
}