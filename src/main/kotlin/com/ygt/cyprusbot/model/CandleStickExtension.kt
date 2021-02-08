package com.ygt.cyprusbot.model

import com.binance.api.client.domain.market.CandlestickInterval


fun CandlestickInterval.mapByValue(intervalId: String): CandlestickInterval {
    return CandlestickInterval.values()
            .filter { it.intervalId.equals(intervalId) }
            .first()
}