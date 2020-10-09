package com.smallraw.chain.bitcoincore.script

// push value
const val OP_0 = 0x00.toByte() // push empty vector
const val OP_FALSE = OP_0
const val OP_PUSHDATA1 = 0x4c.toByte()
const val OP_PUSHDATA2 = 0x4d.toByte()
const val OP_PUSHDATA4 = 0x4e.toByte()
const val OP_1NEGATE = 0x4f.toByte()
const val OP_RESERVED = 0x50.toByte()
const val OP_1 = 0x51.toByte()
const val OP_TRUE = OP_1
const val OP_2 = 0x52.toByte()
const val OP_3 = 0x53.toByte()
const val OP_4 = 0x54.toByte()
const val OP_5 = 0x55.toByte()
const val OP_6 = 0x56.toByte()
const val OP_7 = 0x57.toByte()
const val OP_8 = 0x58.toByte()
const val OP_9 = 0x59.toByte()
const val OP_10 = 0x5a.toByte()
const val OP_11 = 0x5b.toByte()
const val OP_12 = 0x5c.toByte()
const val OP_13 = 0x5d.toByte()
const val OP_14 = 0x5e.toByte()
const val OP_15 = 0x5f.toByte()
const val OP_16 = 0x60.toByte()

// control
const val OP_NOP = 0x61.toByte()
const val OP_VER = 0x62.toByte()
const val OP_IF = 0x63.toByte()
const val OP_NOTIF = 0x64.toByte()
const val OP_VERIF = 0x65.toByte()
const val OP_VERNOTIF = 0x66.toByte()
const val OP_ELSE = 0x67.toByte()
const val OP_ENDIF = 0x68.toByte()
const val OP_VERIFY = 0x69.toByte()
const val OP_RETURN = 0x6a.toByte()

// stack ops
const val OP_TOALTSTACK = 0x6b.toByte()
const val OP_FROMALTSTACK = 0x6c.toByte()
const val OP_2DROP = 0x6d.toByte()
const val OP_2DUP = 0x6e.toByte()
const val OP_3DUP = 0x6f.toByte()
const val OP_2OVER = 0x70.toByte()
const val OP_2ROT = 0x71.toByte()
const val OP_2SWAP = 0x72.toByte()
const val OP_IFDUP = 0x73.toByte()
const val OP_DEPTH = 0x74.toByte()
const val OP_DROP = 0x75.toByte()
const val OP_DUP = 0x76.toByte()
const val OP_NIP = 0x77.toByte()
const val OP_OVER = 0x78.toByte()
const val OP_PICK = 0x79.toByte()
const val OP_ROLL = 0x7a.toByte()
const val OP_ROT = 0x7b.toByte()
const val OP_SWAP = 0x7c.toByte()
const val OP_TUCK = 0x7d.toByte()

// splice ops
const val OP_CAT = 0x7e.toByte()
const val OP_SUBSTR = 0x7f.toByte()
const val OP_LEFT = 0x80.toByte()
const val OP_RIGHT = 0x81.toByte()
const val OP_SIZE = 0x82.toByte()

// bit logic
const val OP_INVERT = 0x83.toByte()
const val OP_AND = 0x84.toByte()
const val OP_OR = 0x85.toByte()
const val OP_XOR = 0x86.toByte()
const val OP_EQUAL = 0x87.toByte()
const val OP_EQUALVERIFY = 0x88.toByte()
const val OP_RESERVED1 = 0x89.toByte()
const val OP_RESERVED2 = 0x8a.toByte()

// numeric
const val OP_1ADD = 0x8b.toByte()
const val OP_1SUB = 0x8c.toByte()
const val OP_2MUL = 0x8d.toByte()
const val OP_2DIV = 0x8e.toByte()
const val OP_NEGATE = 0x8f.toByte()
const val OP_ABS = 0x90.toByte()
const val OP_NOT = 0x91.toByte()
const val OP_0NOTEQUAL = 0x92.toByte()
const val OP_ADD = 0x93.toByte()
const val OP_SUB = 0x94.toByte()
const val OP_MUL = 0x95.toByte()
const val OP_DIV = 0x96.toByte()
const val OP_MOD = 0x97.toByte()
const val OP_LSHIFT = 0x98.toByte()
const val OP_RSHIFT = 0x99.toByte()
const val OP_BOOLAND = 0x9a.toByte()
const val OP_BOOLOR = 0x9b.toByte()
const val OP_NUMEQUAL = 0x9c.toByte()
const val OP_NUMEQUALVERIFY = 0x9d.toByte()
const val OP_NUMNOTEQUAL = 0x9e.toByte()
const val OP_LESSTHAN = 0x9f.toByte()
const val OP_GREATERTHAN = 0xa0.toByte()
const val OP_LESSTHANOREQUAL = 0xa1.toByte()
const val OP_GREATERTHANOREQUAL = 0xa2.toByte()
const val OP_MIN = 0xa3.toByte()
const val OP_MAX = 0xa4.toByte()
const val OP_WITHIN = 0xa5.toByte()

// crypto
const val OP_RIPEMD160 = 0xa6.toByte()
const val OP_SHA1 = 0xa7.toByte()
const val OP_SHA256 = 0xa8.toByte()
const val OP_HASH160 = 0xa9.toByte()
const val OP_HASH256 = 0xaa.toByte()
const val OP_CODESEPARATOR = 0xab.toByte()
const val OP_CHECKSIG = 0xac.toByte()
const val OP_CHECKSIGVERIFY = 0xad.toByte()
const val OP_CHECKMULTISIG = 0xae.toByte()
const val OP_CHECKMULTISIGVERIFY = 0xaf.toByte()

// block state
/** Check lock time of the block. Introduced in BIP 65, replacing OP_NOP2  */
const val OP_CHECKLOCKTIMEVERIFY = 0xb1.toByte()
const val OP_CHECKSEQUENCEVERIFY = 0xb2.toByte()

// expansion
const val OP_NOP1 = 0xb0.toByte()

/** Deprecated by BIP 65  */
@Deprecated("")
const val OP_NOP2 = OP_CHECKLOCKTIMEVERIFY

/** Deprecated by BIP 112  */
@Deprecated("")
const val OP_NOP3 = OP_CHECKSEQUENCEVERIFY
const val OP_NOP4 = 0xb3.toByte()
const val OP_NOP5 = 0xb4.toByte()
const val OP_NOP6 = 0xb5.toByte()
const val OP_NOP7 = 0xb6.toByte()
const val OP_NOP8 = 0xb7.toByte()
const val OP_NOP9 = 0xb8.toByte()
const val OP_NOP10 = 0xb9.toByte()
const val OP_INVALIDOPCODE = 0xff.toByte()

/** Sighash Types */
object SigHash {
    const val ALL: Byte = 0x01              // Sign all outputs
    const val NONE: Byte = 0x02             // Do not sign outputs (zero sequences)
    const val SINGLE: Byte = 0x03           // Sign output at the same index (zero sequences)
    const val FORKID: Byte = 0x40        // Bitcoin Cash SIGHASH_FORKID
    const val ANYONECANPAY = 0x80.toByte()  // Sign only the current input (mask)
}

object OpCodes {
    private val opCodeMap = hashMapOf(
        OP_0 to "0",
        OP_PUSHDATA1 to "PUSHDATA1",
        OP_PUSHDATA2 to "PUSHDATA2",
        OP_PUSHDATA4 to "PUSHDATA4",
        OP_1NEGATE to "1NEGATE",
        OP_RESERVED to "RESERVED",
        OP_1 to "1",
        OP_2 to "2",
        OP_3 to "3",
        OP_4 to "4",
        OP_5 to "5",
        OP_6 to "6",
        OP_7 to "7",
        OP_8 to "8",
        OP_9 to "9",
        OP_10 to "10",
        OP_11 to "11",
        OP_12 to "12",
        OP_13 to "13",
        OP_14 to "14",
        OP_15 to "15",
        OP_16 to "16",
        OP_NOP to "NOP",
        OP_VER to "VER",
        OP_IF to "IF",
        OP_NOTIF to "NOTIF",
        OP_VERIF to "VERIF",
        OP_VERNOTIF to "VERNOTIF",
        OP_ELSE to "ELSE",
        OP_ENDIF to "ENDIF",
        OP_VERIFY to "VERIFY",
        OP_RETURN to "RETURN",
        OP_TOALTSTACK to "TOALTSTACK",
        OP_FROMALTSTACK to "FROMALTSTACK",
        OP_2DROP to "2DROP",
        OP_2DUP to "2DUP",
        OP_3DUP to "3DUP",
        OP_2OVER to "2OVER",
        OP_2ROT to "2ROT",
        OP_2SWAP to "2SWAP",
        OP_IFDUP to "IFDUP",
        OP_DEPTH to "DEPTH",
        OP_DROP to "DROP",
        OP_DUP to "DUP",
        OP_NIP to "NIP",
        OP_OVER to "OVER",
        OP_PICK to "PICK",
        OP_ROLL to "ROLL",
        OP_ROT to "ROT",
        OP_SWAP to "SWAP",
        OP_TUCK to "TUCK",
        OP_CAT to "CAT",
        OP_SUBSTR to "SUBSTR",
        OP_LEFT to "LEFT",
        OP_RIGHT to "RIGHT",
        OP_SIZE to "SIZE",
        OP_INVERT to "INVERT",
        OP_AND to "AND",
        OP_OR to "OR",
        OP_XOR to "XOR",
        OP_EQUAL to "EQUAL",
        OP_EQUALVERIFY to "EQUALVERIFY",
        OP_RESERVED1 to "RESERVED1",
        OP_RESERVED2 to "RESERVED2",
        OP_1ADD to "1ADD",
        OP_1SUB to "1SUB",
        OP_2MUL to "2MUL",
        OP_2DIV to "2DIV",
        OP_NEGATE to "NEGATE",
        OP_ABS to "ABS",
        OP_NOT to "NOT",
        OP_0NOTEQUAL to "0NOTEQUAL",
        OP_ADD to "ADD",
        OP_SUB to "SUB",
        OP_MUL to "MUL",
        OP_DIV to "DIV",
        OP_MOD to "MOD",
        OP_LSHIFT to "LSHIFT",
        OP_RSHIFT to "RSHIFT",
        OP_BOOLAND to "BOOLAND",
        OP_BOOLOR to "BOOLOR",
        OP_NUMEQUAL to "NUMEQUAL",
        OP_NUMEQUALVERIFY to "NUMEQUALVERIFY",
        OP_NUMNOTEQUAL to "NUMNOTEQUAL",
        OP_LESSTHAN to "LESSTHAN",
        OP_GREATERTHAN to "GREATERTHAN",
        OP_LESSTHANOREQUAL to "LESSTHANOREQUAL",
        OP_GREATERTHANOREQUAL to "GREATERTHANOREQUAL",
        OP_MIN to "MIN",
        OP_MAX to "MAX",
        OP_WITHIN to "WITHIN",
        OP_RIPEMD160 to "RIPEMD160",
        OP_SHA1 to "SHA1",
        OP_SHA256 to "SHA256",
        OP_HASH160 to "HASH160",
        OP_HASH256 to "HASH256",
        OP_CODESEPARATOR to "CODESEPARATOR",
        OP_CHECKSIG to "CHECKSIG",
        OP_CHECKSIGVERIFY to "CHECKSIGVERIFY",
        OP_CHECKMULTISIG to "CHECKMULTISIG",
        OP_CHECKMULTISIGVERIFY to "CHECKMULTISIGVERIFY",
        OP_NOP1 to "NOP1",
        OP_CHECKLOCKTIMEVERIFY to "CHECKLOCKTIMEVERIFY",
        OP_CHECKSEQUENCEVERIFY to "CHECKSEQUENCEVERIFY",
        OP_NOP4 to "NOP4",
        OP_NOP5 to "NOP5",
        OP_NOP6 to "NOP6",
        OP_NOP7 to "NOP7",
        OP_NOP8 to "NOP8",
        OP_NOP9 to "NOP9",
        OP_NOP10 to "NOP10"
    )

    //  Converts the given OpCode into a string (eg "0", "PUSHDATA", or "NON_OP(10)")
    fun getOpCodeName(opcode: Byte): String {
        return opCodeMap[opcode] ?: "NON_OP($opcode)"
    }

    //  Converts the given pushdata OpCode into a string (eg "PUSHDATA2", or "PUSHDATA(23)")
    fun getPushDataName(opcode: Byte): String {
        val opcodeInt = opcode.toInt() and 0xFF
        return if (opcodeInt in OP_PUSHDATA1..OP_PUSHDATA4) {
            return opCodeMap[opcode] ?: "PUSHDATA($opcode)"
        } else ""
    }

    fun opToIntValue(chunk: ScriptChunk): Int {
        val opCode = chunk.opcode
        return opToIntValue(opCode)
        //not within range
    }

    fun opToIntValue(opCode: Byte): Int {
        val opCodeInt = opCode.toInt() and 0xFF
        return if (opCodeInt in 81..96) {
            opCodeInt - 80
        } else -1
        //not within range
    }

    fun intToOpCode(value: Int): Byte {
        return if (value in 1..16) {
            (value + 80).toByte()
        } else OP_0
    }
}