package com.ygt.cyprusbot.service.scheduler

import com.binance.api.client.domain.market.CandlestickInterval
import com.ygt.cyprusbot.model.Strategies
import com.ygt.cyprusbot.service.BinanceClientService
import com.ygt.cyprusbot.service.TelegramClientService
import com.ygt.cyprusbot.strategy.*
import mu.KotlinLogging
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Service
import org.ta4j.core.BaseBarSeries
import org.ta4j.core.num.PrecisionNum
import reactor.core.publisher.Flux

@Service
class TrackerScheduler(private val binanceClientService: BinanceClientService, private val telegramClientService: TelegramClientService) {
    private val log = KotlinLogging.logger {}


    @Scheduled(cron = "20 */15 * * * *")
    fun runFuture() {
        log.info { "Future tracker is started" }

        binanceClientService
                .getFutureExchangeInfo()
                .flatMapMany { Flux.fromIterable(it.symbols) }
                .filter { it.contractType.equals("PERPETUAL") }
                .parallel()
                .flatMap { binanceClientService.getFutureCandlesticks(it.symbol, CandlestickInterval.HOURLY.intervalId) }
                .doOnNext {
                    runStrategy(it, Strategies.STOCH, "FUTURE")
                    runStrategy(it, Strategies.COMBO_1H, "FUTURE")
                    runStrategy(it, Strategies.LARGE_PIN, "FUTURE")
                    runStrategy(it, Strategies.MACD_DEMA, "FUTURE")
                }
                .subscribe()


    }

    @Scheduled(cron = "30 */15 * * * *")
    fun runSpot() {
        log.info { "Spot tracker is started" }

        binanceClientService
                .getSpotExchangeInfo()
                .flatMapMany { Flux.fromIterable(it.symbols) }
                .filter { it.quoteAsset.equals("BTC") }
                .filter { it.isMarginTradingAllowed }
                .filter { it.status.equals("TRADING") }
                .parallel()
                .flatMap { binanceClientService.getSpotCandlesticks(it.symbol, CandlestickInterval.HOURLY.intervalId) }
                .filter {
                    it.lastBar.closePrice.multipliedBy(it.lastBar.volume).isGreaterThanOrEqual(PrecisionNum.valueOf(30))
                }
                .doOnNext {
                    runStrategy(it, Strategies.STOCH, "MARGIN")
                    runStrategy(it, Strategies.TILSONT3_MAVILIM, "MARGIN")
                }
                .subscribe()


    }

    private fun runStrategy(it: BaseBarSeries, strategyType: Strategies, market: String) {
        if (strategyType == Strategies.STOCH) {
            val strategy = InverseFisherTransformStochStrategy(it, 21, 9)
            runStrategy(it, strategyType, strategy, market)
        }
        if (strategyType == Strategies.TILSONT3_MAVILIM) {
            val strategy = TilsonT3MavilimStrategy(it)
            runStrategy(it, strategyType, strategy, market)
        }
        if (strategyType == Strategies.COMBO_1H) {
            val strategy = Combo1HStrategy(it)
            runStrategy(it, strategyType, strategy, market)
        }
        if (strategyType == Strategies.LARGE_PIN) {
            val strategy = LargePinStrategy(it)
            runStrategy(it, strategyType, strategy, market)
        }
        if (strategyType == Strategies.MACD_DEMA) {
            val strategy = MacdStrategy(it)
            runStrategy(it, strategyType, strategy, market)
        }
    }

    private fun runStrategy(it: BaseBarSeries, strategyType: Strategies, strategy: CustomStrategy, market: String) {
        val ndx = it.getBarCount() - 1
        val evaluate = strategy.evaluate(ndx)
        val prefix = "*$market*, ${it.name.toUpperCase()}, $strategyType ${it.lastBar.timePeriod}"
        if (evaluate == 1) {
            log.info { "$prefix ,Entry point  ${it.lastBar}" }
            telegramClientService.sendMessageAsync("$prefix, ${strategyType.enterMessage}, Last price: ${it.lastBar.closePrice}")
        }

    }
}