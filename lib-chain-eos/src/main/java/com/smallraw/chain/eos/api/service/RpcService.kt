package com.smallraw.chain.eos.api.service

import com.smallraw.chain.eos.model.Block
import com.smallraw.chain.eos.model.ChainInfo
import com.smallraw.chain.eos.model.JsonToBin
import com.smallraw.chain.eos.model.JsonToBinReq
import com.smallraw.chain.eos.model.TableRows
import com.smallraw.chain.eos.model.TableRowsReq
import com.smallraw.chain.eos.model.account.Account
import com.smallraw.chain.eos.model.transaction.Transaction
import com.smallraw.chain.eos.model.transaction.push.TxRequest
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface RpcService {
    @get:GET("/v1/chain/get_info")
    val chainInfo: Call<ChainInfo?>?

    @POST("/v1/chain/get_block")
    fun getBlock(@Body requestFields: Map<String?, String?>?): Call<Block?>?

    @POST("/v1/chain/get_account")
    fun getAccount(@Body requestFields: Map<String?, String?>?): Call<Account?>?

    @POST("/v1/chain/push_transaction")
    fun pushTransaction(@Body request: TxRequest?): Call<Transaction?>?

    @POST("/v1/chain/get_table_rows")
    fun getTableRows(@Body request: TableRowsReq?): Call<TableRows?>?

    @POST("/v1/chain/abi_json_to_bin")
    fun abiJsonToBin(@Body request: JsonToBinReq?): Call<JsonToBin?>?
}