package com.smallraw.kmm.crypto

import com.appmattus.crypto.Algorithm

enum class HashType {
    MD5,
    SM3,

    SHA224,
    SHA256,
    SHA512,

    SHA3_256,
    SHA3_512,

    Keccak256,
    Keccak512,

    CRC32,

    BLAKE256,
    BLAKE512,

    BLAKE2B_160,
    BLAKE2B_256,
    BLAKE2B_512,
    BLAKE2S_160,
    BLAKE2S_256,

    XXHash32,
    XXHash64,
    XXH3_64,
    XXH3_128,

    RipeMD128,
    RipeMD160,
    RipeMD256,
}

class Hash {
    private val hash: Algorithm
    private var data: ByteArray = ByteArray(0)

    constructor(type: HashType) {
        when (type) {
            HashType.MD5 -> {
                hash = Algorithm.MD5
            }

            HashType.SM3 -> {
                hash = Algorithm.SM3
            }

            HashType.SHA224 -> {
                hash = Algorithm.SHA_224
            }

            HashType.SHA256 -> {
                hash = Algorithm.SHA_256
            }

            HashType.SHA512 -> {
                hash = Algorithm.SHA_512
            }

            HashType.SHA3_256 -> {
                hash = Algorithm.SHA3_256
            }

            HashType.SHA3_512 -> {
                hash = Algorithm.SHA3_512
            }

            HashType.Keccak256 -> {
                hash = Algorithm.Keccak256
            }

            HashType.Keccak512 -> {
                hash = Algorithm.Keccak512
            }

            HashType.CRC32 -> {
                hash = Algorithm.CRC32
            }

            HashType.BLAKE256 -> {
                hash = Algorithm.BLAKE256
            }

            HashType.BLAKE512 -> {
                hash = Algorithm.BLAKE512
            }

            HashType.BLAKE2B_160 -> {
                hash = Algorithm.Blake2b_160
            }

            HashType.BLAKE2B_256 -> {
                hash = Algorithm.Blake2b_256
            }

            HashType.BLAKE2B_512 -> {
                hash = Algorithm.Blake2b_512
            }

            HashType.BLAKE2S_160 -> {
                hash = Algorithm.Blake2s_160
            }

            HashType.BLAKE2S_256 -> {
                hash = Algorithm.Blake2s_256
            }

            HashType.XXHash32 -> {
                hash = Algorithm.XXHash32()
            }

            HashType.XXHash64 -> {
                hash = Algorithm.XXHash64()
            }

            HashType.XXH3_64 -> {
                hash = Algorithm.XXH3_64()
            }

            HashType.XXH3_128 -> {
                hash = Algorithm.XXH3_128()
            }

            HashType.RipeMD128 -> {
                hash = Algorithm.RipeMD128
            }

            HashType.RipeMD160 -> {
                hash = Algorithm.RipeMD160
            }

            HashType.RipeMD256 -> {
                hash = Algorithm.RipeMD256
            }
        }
    }

    fun update(data: ByteArray): Hash {
        this.data += data
        return this
    }

    fun digest(): ByteArray {
        return hash.hash(data)
    }
}