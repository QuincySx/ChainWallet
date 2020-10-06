package com.smallraw.chain.bitcoin.transaction.script

open class ScriptInput : Script {
    companion object {
        @JvmStatic
        val EMPTY = ScriptInput(byteArrayOf())

        @Throws(ScriptParsingException::class)
        fun fromScriptBytes(scriptBytes: ByteArray): ScriptInput {

            if (isWitnessProgram(depush(scriptBytes))) {
                val witnessProgram: ByteArray =
                    getWitnessProgram(depush(scriptBytes))
                if (witnessProgram.size == 20) {
                    return ScriptInputP2WPKH(scriptBytes)
                }
                return if (witnessProgram.size == 32) {
                    ScriptInputP2WSH(scriptBytes)
                } else ScriptInput(scriptBytes)
            }
            val chunks: List<Chunk> = parseChunks(scriptBytes)
            if (ScriptInputP2PKH.isScriptInputStandard(chunks)) {
                return ScriptInputP2PKH(chunks, scriptBytes)
            }
            if (ScriptInputP2PK.isScriptInputP2PK(chunks)) {
                return ScriptInputP2PK(chunks, scriptBytes)
            }
            return if (ScriptInputP2SHMultisig.isScriptInputP2SHMultisig(chunks)) {
                ScriptInputP2SHMultisig(chunks, scriptBytes)
            } else ScriptInput(scriptBytes)
        }

        /**
         * Check if supplied bytes are witness program
         */
        private fun isWitnessProgram(scriptBytes: ByteArray): Boolean {
            if (scriptBytes.size < 4 || scriptBytes.size > 42) {
                return false
            }
            if (scriptBytes[0] != OP_0.toByte() && (scriptBytes[0] < OP_1.toByte() || scriptBytes[0] > OP_16.toByte())) {
                return false
            }
            return if (scriptBytes[1] < 0x02 || scriptBytes[1] > 0x28) {
                false
            } else true
        }

        fun getWitnessProgram(scriptBytes: ByteArray): ByteArray {
            require(isWitnessProgram(scriptBytes)) { "Script is not a witness programm" }
            return scriptBytes.copyOfRange(2, scriptBytes.size)
        }

        /**
         * Tries to remove push code from script.
         * @return script without first byte if it's push, else empty script.
         */
        fun depush(scriptBytes: ByteArray): ByteArray {
            var script = scriptBytes
            if (script.isEmpty()) {
                return byteArrayOf()
            }
            val pushByte = script[0]
            if (pushByte < 1 || pushByte > 76) {
                return byteArrayOf()
            }
            script = script.copyOfRange(1, script.size)
            return if (script.size != pushByte.toInt()) {
                byteArrayOf()
            } else script
        }
    }

    constructor(scriptBytes: ByteArray) : super(scriptBytes)
    constructor(chunks: List<Chunk>) : super(chunks)

    open fun getUnmalleableBytes(): ByteArray? {
        // We cannot do this for scripts we do not understand
        return null
    }
}