package com.ygt.cyprusbot

import com.binance.api.client.BinanceApiClientFactory
import com.binance.api.client.domain.market.CandlestickInterval
import com.ygt.cyprusbot.indicator.*
import com.ygt.cyprusbot.model.PriceNumber
import com.ygt.cyprusbot.service.TelegramClientService
import org.springframework.boot.CommandLineRunner
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.stereotype.Component
import org.ta4j.core.*
import org.ta4j.core.indicators.helpers.ClosePriceIndicator
import java.time.Duration
import java.time.Instant
import java.time.ZoneId
import java.time.ZonedDateTime


@SpringBootApplication
class CyprusBotApplication

fun main(args: Array<String>) {
    runApplication<CyprusBotApplication>(*args)
}

@Component
class Command (val telegramClientService: TelegramClientService) : CommandLineRunner {
    override fun run(vararg args: String?) {
        val factory = BinanceApiClientFactory.newInstance();
        val client = factory.newWebSocketClient()
        val restClient = factory.newRestClient()

        val candlestickBars = restClient.getCandlestickBars("LTCUSDT", CandlestickInterval.FIFTEEN_MINUTES)
        val bars = candlestickBars.map {
            BaseBar.builder()
                    .trades(it.numberOfTrades.toInt())
                    .closePrice(PriceNumber.valueOf(it.close))
                    .openPrice(PriceNumber.valueOf(it.open))
                    .highPrice(PriceNumber.valueOf(it.high))
                    .lowPrice(PriceNumber.valueOf(it.low))
                    .volume(PriceNumber.valueOf(it.volume))
                    .timePeriod(Duration.ofMinutes(15))
                    .endTime(ZonedDateTime.ofInstant(Instant.ofEpochMilli(it.closeTime), ZoneId.of("UTC")))
                    .lowPrice(PriceNumber.valueOf(it.low))
                    .build()
        }

        val barSeries = BaseBarSeries(bars)
        println(barSeries.getBar(barSeries.barCount - 1))
        val close = ClosePriceIndicator(barSeries)

        val change = ChangeIndicator(close)
        val max = MaxIndicator(change, 0)
        val up=RmaIndicator(max, 14)
        val min = MinIndicator(change, 0)
        val negativeIndicator = NegativeIndicator(min)
        val down=RmaIndicator(negativeIndicator, 14)
        val rsi=RsiIndicator(barSeries,14)
        rsi.getValue(492)
        println(rsi)
    }
}