package com.ygt.cyprusbot.model.binance.future

import com.fasterxml.jackson.annotation.JsonProperty

data class FutureExchangeInfo(

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

        @field:JsonProperty("quantityPrecision")
        val quantityPrecision: Int? = null,

        @field:JsonProperty("symbol")
        val symbol: String,

        @field:JsonProperty("pricePrecision")
        val pricePrecision: Int? = null,

        @field:JsonProperty("contractStatus")
        val contractStatus: String? = null,

        @field:JsonProperty("requiredMarginPercent")
        val requiredMarginPercent: String? = null,

        @field:JsonProperty("contractType")
        val contractType: String? = null,

        @field:JsonProperty("onboardDate")
        val onboardDate: Long? = null,

        @field:JsonProperty("baseAsset")
        val baseAsset: String? = null,

        @field:JsonProperty("equalQtyPrecision")
        val equalQtyPrecision: Int? = null,

        @field:JsonProperty("filters")
        val filters: List<FiltersItem?>? = null,

        @field:JsonProperty("baseAssetPrecision")
        val baseAssetPrecision: Int? = null,

        @field:JsonProperty("pair")
        val pair: String? = null,

        @field:JsonProperty("triggerProtect")
        val triggerProtect: String? = null,

        @field:JsonProperty("marginAsset")
        val marginAsset: String? = null,

        @field:JsonProperty("quotePrecision")
        val quotePrecision: Int? = null,

        @field:JsonProperty("underlyingType")
        val underlyingType: String? = null,

        @field:JsonProperty("orderTypes")
        val orderTypes: List<String?>? = null,

        @field:JsonProperty("maintMarginPercent")
        val maintMarginPercent: String? = null,

        @field:JsonProperty("contractSize")
        val contractSize: Int? = null,

        @field:JsonProperty("deliveryDate")
        val deliveryDate: Long? = null,

        @field:JsonProperty("timeInForce")
        val timeInForce: List<String?>? = null,

        @field:JsonProperty("quoteAsset")
        val quoteAsset: String? = null,

        @field:JsonProperty("underlyingSubType")
        val underlyingSubType: List<Any?>? = null
)

data class FiltersItem(

        @field:JsonProperty("multiplierDown")
        val multiplierDown: String? = null,

        @field:JsonProperty("multiplierUp")
        val multiplierUp: String? = null,

        @field:JsonProperty("multiplierDecimal")
        val multiplierDecimal: String? = null,

        @field:JsonProperty("filterType")
        val filterType: String? = null,

        @field:JsonProperty("limit")
        val limit: Int? = null,

        @field:JsonProperty("stepSize")
        val stepSize: String? = null,

        @field:JsonProperty("maxQty")
        val maxQty: String? = null,

        @field:JsonProperty("minQty")
        val minQty: String? = null,

        @field:JsonProperty("minPrice")
        val minPrice: String? = null,

        @field:JsonProperty("maxPrice")
        val maxPrice: String? = null,

        @field:JsonProperty("tickSize")
        val tickSize: String? = null
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
