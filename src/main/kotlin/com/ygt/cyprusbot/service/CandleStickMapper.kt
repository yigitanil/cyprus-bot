package com.ygt.cyprusbot.service

import com.binance.api.client.domain.event.CandlestickEvent
import com.binance.api.client.domain.market.Candlestick
import org.ta4j.core.BaseBar
import org.ta4j.core.num.PrecisionNum
import java.time.Duration
import java.time.Instant
import java.time.ZoneId
import java.time.ZonedDateTime

class CandleStickMapper {
    companion object{
         fun candleStickBarToBar(it: Candlestick, intervalId: String) = BaseBar.builder()
                 .trades(it.numberOfTrades.toInt())
                 .closePrice(PrecisionNum.valueOf(it.close))
                 .openPrice(PrecisionNum.valueOf(it.open))
                 .highPrice(PrecisionNum.valueOf(it.high))
                 .lowPrice(PrecisionNum.valueOf(it.low))
                 .volume(PrecisionNum.valueOf(it.volume))
                 .timePeriod(intervalToDuration(intervalId))
                 .endTime(ZonedDateTime.ofInstant(Instant.ofEpochMilli(it.closeTime), ZoneId.of("UTC")))
                 .lowPrice(PrecisionNum.valueOf(it.low))
                .build()

         fun candleStickEventToBar(it: CandlestickEvent) = BaseBar.builder()
                 .amount(PrecisionNum.valueOf(it.numberOfTrades))
                 .closePrice(PrecisionNum.valueOf(it.close))
                 .openPrice(PrecisionNum.valueOf(it.open))
                 .highPrice(PrecisionNum.valueOf(it.high))
                 .lowPrice(PrecisionNum.valueOf(it.low))
                 .volume(PrecisionNum.valueOf(it.volume))
                 .timePeriod(intervalToDuration(it.intervalId))
                 .endTime(ZonedDateTime.ofInstant(Instant.ofEpochMilli(it.closeTime), ZoneId.of("UTC")))
                 .lowPrice(PrecisionNum.valueOf(it.low))
                .build()

       private fun intervalToDuration(intervalId: String): Duration {
            if (intervalId.equals("15m")){
                return Duration.ofMinutes(15);
            }
            if (intervalId.equals("1h")){
                return Duration.ofHours(1);
            }
            if (intervalId.equals("4h")){
                return Duration.ofHours(4);
            }
            if (intervalId.equals("2h")){
                return Duration.ofHours(2);
            }
            if (intervalId.equals("5m")){
                return Duration.ofMinutes(5);
            }
            return Duration.ZERO
        }
    }

}