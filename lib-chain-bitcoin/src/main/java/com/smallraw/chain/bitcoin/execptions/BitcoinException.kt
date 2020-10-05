package com.smallraw.chain.bitcoin.execptions

class BitcoinException @JvmOverloads constructor(
    val errorCode: Int,
    detailMessage: String?,
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
        const val ERR_FEE_IS_TOO_BIG = 7
        const val ERR_FEE_IS_LESS_THEN_ZERO = 8
        const val ERR_CHANGE_IS_LESS_THEN_ZERO = 9
        const val ERR_AMOUNT_TO_SEND_IS_LESS_THEN_ZERO = 10
        const val ERR_UNSUPPORTED = 11
    }
}