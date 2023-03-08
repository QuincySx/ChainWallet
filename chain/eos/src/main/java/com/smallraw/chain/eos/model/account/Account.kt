package com.smallraw.chain.eos.model.account

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty
import java.util.*

@JsonIgnoreProperties(ignoreUnknown = true)
class Account {
    @JsonProperty("account_name")
    var accountName: String? = null

    @JsonProperty("privileged")
    var privileged: String? = null

    @JsonProperty("last_code_update")
    var lastCodeUpdate: Date? = null

    @JsonProperty("created")
    var created: Date? = null

    @JsonProperty("ram_quota")
    var ramQuota: Long? = null

    @JsonProperty("net_weight")
    var netWeight: Long? = null

    @JsonProperty("cpu_weight")
    var cpuWeight: Long? = null

    @JsonProperty("net_limit")
    var netLimit: NetLimit? = null

    @JsonProperty("cpu_limit")
    var cpuLimit: CpuLimit? = null

    @JsonProperty("ram_usage")
    var ramUsage: Long? = null

    @JsonProperty("permissions")
    var permissions: List<Permission>? = null
}