package com.ygt.cyprusbot.model

import com.fasterxml.jackson.annotation.JsonProperty

data class NftMetaData(
        val id: Int,
        val description: String,
        @JsonProperty("external_url") val externalUrl: String,
        val image: String,
        val name: String,

        )

