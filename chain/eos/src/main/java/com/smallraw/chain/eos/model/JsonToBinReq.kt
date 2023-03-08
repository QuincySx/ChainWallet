package com.smallraw.chain.eos.model

import com.fasterxml.jackson.annotation.JsonIgnoreProperties

@JsonIgnoreProperties(ignoreUnknown = true)
class JsonToBinReq(var code: String, var action: String, var args: UpTransfer)