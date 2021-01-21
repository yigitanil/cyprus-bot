package com.ygt.cyprusbot.model

import com.binance.api.client.domain.market.CandlestickInterval

class CandleStickExtension{
    companion object{
        fun mapByValue(intervalId: String) :CandlestickInterval{
            return CandlestickInterval.values()
                    .filter { it.intervalId.equals(intervalId) }
                    .first()
        }
    }
}
