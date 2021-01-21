package com.ygt.cyprusbot.model

import com.binance.api.client.domain.market.CandlestickInterval
import reactor.core.Disposable

data class RunningStrategyPostDto(
        val symbol:String,
        val interval:String
)
