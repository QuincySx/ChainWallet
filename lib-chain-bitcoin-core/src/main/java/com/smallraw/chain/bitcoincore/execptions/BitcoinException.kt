package com.smallraw.chain.bitcoincore.execptions

open class BitcoinException @JvmOverloads constructor(
    val errorCode: Int,
    detailMessage: String? = null,
    val extraInformation: Any? = null
) : Exception(detailMessage) {

    companion object {
        const val ERR_NO_SPENDABLE_OUTPUTS_FOR_THE_ADDRESS = 0
        const val ERR_INSUFFICIENT_FUNDS = 1
        const val ERR_WRONG_TYPE = 2
        const val ERR_BAD_FORMAT = 3
        const val ERR_INCORRECT_PASSWORD = 4
        const val ERR_MEANINGLESS_OPERATION = 5
        const val ERR_NO_INPUT = 6
        const val ERR_INPUT_SIGN = 7
        const val ERR_FEE_IS_TOO_BIG = 8
        const val ERR_FEE_IS_LESS_THEN_ZERO = 9
        const val ERR_CHANGE_IS_LESS_THEN_ZERO = 10
        const val ERR_AMOUNT_TO_SEND_IS_LESS_THEN_ZERO = 11
        const val ERR_UNSUPPORTED = 12
        const val ERR_ADDRESS_BAD_FORMAT = 13
        const val ERR_KEY_WRONG_LENGTH = 14
        const val ERR_CALCULATE_SIGNATURE = 15
        const val ERR_CALCULATE_PUBLIC_KEY = 16
        const val ERR_GENERATE_PRIVATE_KEY = 17
    }

    class AddressFormatException(msg: String = "err address bad format") :
        BitcoinException(ERR_ADDRESS_BAD_FORMAT, msg)

    class KeyWrongLengthException(msg: String = "err key wrong length") :
        BitcoinException(ERR_KEY_WRONG_LENGTH, msg)

    class CalculateSignatureException(msg: String = "Calculate Signature error") :
        BitcoinException(ERR_CALCULATE_SIGNATURE, msg)

    class CalculatePublicKeyException(msg: String = "Calculate PublicKey error") :
        BitcoinException(ERR_CALCULATE_PUBLIC_KEY, msg)

    class GeneratePrivateKeyException(msg: String = "Generate PrivateKey error") :
        BitcoinException(ERR_GENERATE_PRIVATE_KEY, msg)
}