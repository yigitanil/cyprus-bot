package com.ygt.cyprusbot.model

import com.binance.api.client.domain.market.CandlestickInterval
import reactor.core.Disposable
import java.time.ZonedDateTime

data class RunningStrategyDto(
        val id:String,
        val symbol:String,
        val interval:CandlestickInterval,
        val createdDate: ZonedDateTime,
)
