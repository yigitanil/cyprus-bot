package com.ygt.cyprusbot.service.scheduler

import com.binance.api.client.domain.market.CandlestickInterval
import com.ygt.cyprusbot.model.Strategies
import com.ygt.cyprusbot.service.BinanceClientService
import com.ygt.cyprusbot.service.TelegramClientService
import com.ygt.cyprusbot.strategy.CustomStrategy
import com.ygt.cyprusbot.strategy.InverseFisherTransformStochStrategy
import com.ygt.cyprusbot.strategy.LargePinStrategy
import com.ygt.cyprusbot.strategy.TilsonT3Strategy
import mu.KotlinLogging
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Service
import org.ta4j.core.BaseBarSeries
import reactor.core.publisher.Flux

@Service
class Future1MRunner(private val binanceClientService: BinanceClientService,
                     private val telegramClientService: TelegramClientService) {
    private val log = KotlinLogging.logger {}


    @Scheduled(cron = "2 */1 * * * *")
    fun runFuture() {
        binanceClientService
            .getFutureExchangeInfo()
            .flatMapMany { Flux.fromIterable(it.symbols) }
            .filter { it.contractType.equals("PERPETUAL") }
            .parallel()
            .flatMap {
                binanceClientService.getFutureCandlesticks(
                    it.symbol,
                    CandlestickInterval.ONE_MINUTE.intervalId
                )
            }
            .doOnNext {
                runStrategy(it, Strategies.LARGE_PIN, LargePinStrategy(it), "Future")
                runStrategy(it, Strategies.LARGE_PIN, LargePinStrategy(it, 0.0199999), "Future")
            }
            .subscribe()
    }

    @Scheduled(cron = "2 */5 * * * *")
    fun runFutureLargePing5() {
        binanceClientService
            .getFutureExchangeInfo()
            .flatMapMany { Flux.fromIterable(it.symbols) }
            .filter { it.contractType.equals("PERPETUAL") }
            .parallel()
            .flatMap {
                binanceClientService.getFutureCandlesticks(
                    it.symbol,
                    CandlestickInterval.FIVE_MINUTES.intervalId
                )
            }
            .doOnNext {
                runStrategy(it, Strategies.LARGE_PIN, LargePinStrategy(it), "Future")
                runStrategy(it, Strategies.LARGE_PIN, LargePinStrategy(it, 0.0199999), "Future")
            }
            .subscribe()
    }


    //    @Scheduled(cron = "2 */5 * * * *")
    fun runFutureTilson() {
        log.info { "Tilson T3 5 Minutes" }
        binanceClientService
            .getFutureExchangeInfo()
            .flatMapMany { Flux.fromIterable(it.symbols) }
            .filter { it.contractType.equals("PERPETUAL") }
            .parallel()
            .flatMap {
                binanceClientService.getFutureCandlesticks(
                    it.symbol,
                    CandlestickInterval.FIVE_MINUTES.intervalId
                )
            }
            .doOnNext {
                runStrategy(it, Strategies.TILSON_T3, TilsonT3Strategy(it, 0.5, 3), "Future")
                runStrategy(it, Strategies.STOCH, InverseFisherTransformStochStrategy(it, 54, 9), "Future")
            }
            .subscribe()
    }


    private fun runStrategy(it: BaseBarSeries, strategyType: Strategies, strategy: CustomStrategy, market: String) {
        val ndx = it.getBarCount() - 1
        val evaluate = strategy.evaluate(ndx)
        val prefix = "*$market*, ${it.name.toUpperCase()}, $strategyType ${it.lastBar.timePeriod}"
        if (evaluate == 1) {
            log.info { "$prefix ,Entry point  ${it.lastBar}" }
            telegramClientService.sendMessageAsync("$prefix, ${strategyType.enterMessage}, Last price: ${it.lastBar.closePrice}")
        }
        if (evaluate == -1) {
            log.info { "$prefix ,Exit point  ${it.lastBar}" }
            telegramClientService.sendMessageAsync("$prefix, ${strategyType.exitMessage}, Last price: ${it.lastBar.closePrice}")
        }
    }
}