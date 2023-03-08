package com.smallraw.chain.eos.model

import com.fasterxml.jackson.annotation.JsonIgnoreProperties

@JsonIgnoreProperties(ignoreUnknown = true)
class UpTransfer(var upfrom: String, var upto: String, var value: String)