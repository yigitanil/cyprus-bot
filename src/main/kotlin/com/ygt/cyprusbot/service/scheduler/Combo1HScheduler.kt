package com.ygt.cyprusbot.service.scheduler

import com.binance.api.client.BinanceApiRestClient
import com.binance.api.client.domain.market.CandlestickInterval
import com.ygt.cyprusbot.model.Strategies
import com.ygt.cyprusbot.service.CandleStickMapper
import com.ygt.cyprusbot.service.TelegramClientService
import com.ygt.cyprusbot.strategy.Combo1HStrategy
import mu.KotlinLogging
import org.ta4j.core.BaseBarSeries
import reactor.core.publisher.Flux


//@Service
class Combo1HScheduler(private val restClient: BinanceApiRestClient, private val telegramClientService: TelegramClientService) {
    private val log = KotlinLogging.logger {}

    private val pairs = listOf("AAVEUSDT", "XEMUSDT", "ADAUSDT", "EOSUSDT", "XRPUSDT", "BNBUSDT", "XLMUSDT", "UNIUSDT", "XMRUSDT", "THETAUSDT", "AVAXUSDT")

    //    @Scheduled(cron = "20 0 */1 * * *")
    fun run() {
        log.info { "Combo1HScheduler is started" }
        val strategyType = Strategies.COMBO_1H
        Flux.fromIterable(pairs)
                .parallel()
                .map {
                    val bars = restClient.getCandlestickBars(it, CandlestickInterval.HOURLY)
                            .map { CandleStickMapper.candleStickBarToBar(it, CandlestickInterval.HOURLY.intervalId) }
                    val barSeries = BaseBarSeries(bars)
                    PairBar(it, barSeries)
                }.doOnComplete { log.info { "Combo1HScheduler is finished" } }
                .subscribe {
                    val ndx = it.barSeries.getBarCount() - 1
                    val strategy = Combo1HStrategy(it.barSeries)
                    val evaluate = strategy.evaluate(ndx)
                    val prefix = "${it.pair.toUpperCase()}, ${it.barSeries.lastBar.timePeriod}"
                    if (evaluate == 1) {
                        log.info { "$prefix , $strategyType possible enter point ${it.barSeries.lastBar}" }
                        telegramClientService.sendMessageAsync("$prefix, ${strategyType.enterMessage}, Last price: ${it.barSeries.lastBar.closePrice}")
                    }
                    if (evaluate == -1) {
                        log.info { "$prefix , $strategyType possible exit point ${it.barSeries.lastBar}" }
                        telegramClientService.sendMessageAsync("$prefix, ${strategyType.exitMessage}, Last price: ${it.barSeries.lastBar.closePrice}")
                    }
                }

    }

}

data class PairBar(val pair: String, val barSeries: BaseBarSeries)