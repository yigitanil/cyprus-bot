package com.ygt.cyprusbot.service

import com.binance.api.client.domain.event.CandlestickEvent
import com.binance.api.client.domain.market.Candlestick
import com.ygt.cyprusbot.model.PriceNumber
import org.ta4j.core.BaseBar
import java.time.Duration
import java.time.Instant
import java.time.ZoneId
import java.time.ZonedDateTime

class CandleStickMapper {
    companion object{
         fun candleStickBarToBar(it: Candlestick, intervalId: String) = BaseBar.builder()
                .trades(it.numberOfTrades.toInt())
                .closePrice(PriceNumber.valueOf(it.close))
                .openPrice(PriceNumber.valueOf(it.open))
                .highPrice(PriceNumber.valueOf(it.high))
                .lowPrice(PriceNumber.valueOf(it.low))
                .volume(PriceNumber.valueOf(it.volume))
                .timePeriod(intervalToDuration(intervalId))
                .endTime(ZonedDateTime.ofInstant(Instant.ofEpochMilli(it.closeTime), ZoneId.of("UTC")))
                .lowPrice(PriceNumber.valueOf(it.low))
                .build()

         fun candleStickEventToBar(it: CandlestickEvent) = BaseBar.builder()
                .amount(PriceNumber.valueOf(it.numberOfTrades))
                .closePrice(PriceNumber.valueOf(it.close))
                .openPrice(PriceNumber.valueOf(it.open))
                .highPrice(PriceNumber.valueOf(it.high))
                .lowPrice(PriceNumber.valueOf(it.low))
                .volume(PriceNumber.valueOf(it.volume))
                .timePeriod(intervalToDuration(it.intervalId))
                .endTime(ZonedDateTime.ofInstant(Instant.ofEpochMilli(it.closeTime), ZoneId.of("UTC")))
                .lowPrice(PriceNumber.valueOf(it.low))
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