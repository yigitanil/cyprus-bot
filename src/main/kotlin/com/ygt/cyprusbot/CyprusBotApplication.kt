package com.ygt.cyprusbot

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication


@SpringBootApplication
class CyprusBotApplication

fun main(args: Array<String>) {
    runApplication<CyprusBotApplication>(*args)
}


//@Component
//class Command : CommandLineRunner {
//    override fun run(vararg args: String?) {
//        val factory = BinanceApiClientFactory.newInstance();
//        val client = factory.newWebSocketClient()
//        val restClient = factory.newRestClient()
//
//        val candlestickBars = restClient.getCandlestickBars("BTCUSDT", CandlestickInterval.FIFTEEN_MINUTES)
//        val bars = candlestickBars.map {
//            BaseBar.builder()
//                    .trades(it.numberOfTrades.toInt())
//                    .closePrice(PrecisionNum.valueOf(it.close))
//                    .openPrice(PrecisionNum.valueOf(it.open))
//                    .highPrice(PrecisionNum.valueOf(it.high))
//                    .lowPrice(PrecisionNum.valueOf(it.low))
//                    .volume(PrecisionNum.valueOf(it.volume))
//                    .timePeriod(Duration.ofMinutes(15))
//                    .endTime(ZonedDateTime.ofInstant(Instant.ofEpochMilli(it.closeTime), ZoneId.of("UTC")))
//                    .lowPrice(PrecisionNum.valueOf(it.low))
//                    .build()
//        }
//
//        val barSeries = BaseBarSeries(bars)
//        println(barSeries.getBar(barSeries.barCount - 1))
//        val default = TilsonT3ShortTermStrategy(barSeries)
//        println(default)
//        default.evaluate(478)
//        default.evaluate(479)
//        default.evaluate(480)
//    }
//}