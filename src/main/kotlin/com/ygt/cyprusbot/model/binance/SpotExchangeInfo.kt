package com.ygt.cyprusbot.model.binance.spot

import com.fasterxml.jackson.annotation.JsonProperty

data class SpotExchangeInfo(

        @field:JsonProperty("rateLimits")
        val rateLimits: List<RateLimitsItem?>? = null,

        @field:JsonProperty("exchangeFilters")
        val exchangeFilters: List<Any?>? = null,

        @field:JsonProperty("timezone")
        val timezone: String? = null,

        @field:JsonProperty("serverTime")
        val serverTime: Long? = null,

        @field:JsonProperty("symbols")
        val symbols: List<SymbolsItem>
)

data class SymbolsItem(

        @field:JsonProperty("symbol")
        val symbol: String,

        @field:JsonProperty("quoteOrderQtyMarketAllowed")
        val quoteOrderQtyMarketAllowed: Boolean? = null,

        @field:JsonProperty("baseAsset")
        val baseAsset: String? = null,

        @field:JsonProperty("filters")
        val filters: List<FiltersItem?>? = null,

        @field:JsonProperty("baseAssetPrecision")
        val baseAssetPrecision: Int? = null,

        @field:JsonProperty("isSpotTradingAllowed")
        val isSpotTradingAllowed: Boolean? = null,

        @field:JsonProperty("quoteAssetPrecision")
        val quoteAssetPrecision: Int? = null,

        @field:JsonProperty("quoteCommissionPrecision")
        val quoteCommissionPrecision: Int? = null,

        @field:JsonProperty("ocoAllowed")
        val ocoAllowed: Boolean? = null,

        @field:JsonProperty("quotePrecision")
        val quotePrecision: Int? = null,

        @field:JsonProperty("orderTypes")
        val orderTypes: List<String?>? = null,

        @field:JsonProperty("permissions")
        val permissions: List<String?>? = null,

        @field:JsonProperty("icebergAllowed")
        val icebergAllowed: Boolean? = null,

        @field:JsonProperty("isMarginTradingAllowed")
        val isMarginTradingAllowed: Boolean,

        @field:JsonProperty("quoteAsset")
        val quoteAsset: String? = null,

        @field:JsonProperty("baseCommissionPrecision")
        val baseCommissionPrecision: Int? = null,

        @field:JsonProperty("status")
        val status: String? = null
)

data class RateLimitsItem(

        @field:JsonProperty("intervalNum")
        val intervalNum: Int? = null,

        @field:JsonProperty("limit")
        val limit: Int? = null,

        @field:JsonProperty("interval")
        val interval: String? = null,

        @field:JsonProperty("rateLimitType")
        val rateLimitType: String? = null
)

data class FiltersItem(

        @field:JsonProperty("filterType")
        val filterType: String? = null,

        @field:JsonProperty("maxNumAlgoOrders")
        val maxNumAlgoOrders: Int? = null,

        @field:JsonProperty("maxNumOrders")
        val maxNumOrders: Int? = null,

        @field:JsonProperty("stepSize")
        val stepSize: String? = null,

        @field:JsonProperty("maxQty")
        val maxQty: String? = null,

        @field:JsonProperty("minQty")
        val minQty: String? = null,

        @field:JsonProperty("limit")
        val limit: Int? = null,

        @field:JsonProperty("minNotional")
        val minNotional: String? = null,

        @field:JsonProperty("avgPriceMins")
        val avgPriceMins: Int? = null,

        @field:JsonProperty("applyToMarket")
        val applyToMarket: Boolean? = null,

        @field:JsonProperty("multiplierDown")
        val multiplierDown: String? = null,

        @field:JsonProperty("multiplierUp")
        val multiplierUp: String? = null,

        @field:JsonProperty("minPrice")
        val minPrice: String? = null,

        @field:JsonProperty("maxPrice")
        val maxPrice: String? = null,

        @field:JsonProperty("tickSize")
        val tickSize: String? = null
)
