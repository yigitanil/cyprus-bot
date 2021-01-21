package com.ygt.cyprusbot.model

import com.binance.api.client.domain.market.CandlestickInterval
import reactor.core.Disposable
import java.time.ZonedDateTime

data class RunningStrategy(
        val symbol:String,
        val interval:CandlestickInterval,
        val disposable: Disposable,
        val createdDate: ZonedDateTime,
)
