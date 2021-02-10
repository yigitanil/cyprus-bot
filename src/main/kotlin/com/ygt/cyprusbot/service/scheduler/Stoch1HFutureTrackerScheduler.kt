package com.ygt.cyprusbot.service.scheduler

import com.binance.api.client.domain.market.CandlestickInterval
import com.ygt.cyprusbot.model.Strategies
import com.ygt.cyprusbot.service.BinanceClientService
import com.ygt.cyprusbot.service.TelegramClientService
import com.ygt.cyprusbot.strategy.InverseFisherTransformStochStrategy
import mu.KotlinLogging
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Service
import reactor.core.publisher.Flux

@Service
class Stoch1HFutureTrackerScheduler(private val binanceClientService: BinanceClientService, private val telegramClientService: TelegramClientService) {
    private val log = KotlinLogging.logger {}


    @Scheduled(cron = "20 */15 * * * *")
    fun run() {
        log.info { "Stoch1HFutureTrackerScheduler is started" }
        val strategyType = Strategies.STOCH

        binanceClientService
                .getExchangeInfo()
                .flatMapMany { Flux.fromIterable(it.symbols) }
                .filter { it.contractType.equals("PERPETUAL") }
                .parallel()
                .flatMap { binanceClientService.getCandlesticks(it.symbol, CandlestickInterval.HOURLY.intervalId) }
                .doOnNext {
                    val ndx = it.getBarCount() - 1
                    val strategy = InverseFisherTransformStochStrategy(it, 21, 9)
                    val evaluate = strategy.evaluate(ndx)
                    val prefix = "${it.name.toUpperCase()}, ${it.lastBar.timePeriod}"
                    if (evaluate == 1) {
                        log.info { "$prefix , $strategyType possible enter point ${it.lastBar}" }
                        telegramClientService.sendMessageAsync("$prefix, ${strategyType.enterMessage}, Last price: ${it.lastBar.closePrice}")
                    }
                    if (evaluate == -1) {
                        log.info { "$prefix , $strategyType possible exit point ${it.lastBar}" }
                        telegramClientService.sendMessageAsync("$prefix, ${strategyType.exitMessage}, Last price: ${it.lastBar.closePrice}")
                    }
                }
                .subscribe()


    }
}