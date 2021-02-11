package com.ygt.cyprusbot.model

import com.binance.api.client.domain.market.CandlestickInterval
import java.io.Closeable
import java.time.ZonedDateTime

data class RunningStrategy(
        val symbol: String,
        val interval: CandlestickInterval,
        var closeable: Closeable?,
        val createdDate: ZonedDateTime,
)
