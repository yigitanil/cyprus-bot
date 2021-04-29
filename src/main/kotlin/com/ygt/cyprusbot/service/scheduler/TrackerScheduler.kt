package com.ygt.cyprusbot.service.scheduler

import com.binance.api.client.domain.market.CandlestickInterval
import com.ygt.cyprusbot.model.Strategies
import com.ygt.cyprusbot.service.BinanceClientService
import com.ygt.cyprusbot.service.TelegramClientService
import com.ygt.cyprusbot.strategy.*
import mu.KotlinLogging
import org.apache.commons.lang3.StringUtils
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Service
import org.ta4j.core.BaseBarSeries
import reactor.core.publisher.Flux

@Service
class TrackerScheduler(
    private val binanceClientService: BinanceClientService,
    private val telegramClientService: TelegramClientService
) {
    private val log = KotlinLogging.logger {}


    @Scheduled(cron = "02 0 */1 * * *")
    fun runFuture() {
        log.info { "Future tracker is started" }

        binanceClientService
            .getFutureExchangeInfo()
            .flatMapMany { Flux.fromIterable(it.symbols) }
            .filter { it.contractType.equals("PERPETUAL") }
            .parallel()
            .flatMap { binanceClientService.getFutureCandlesticks(it.symbol, CandlestickInterval.HOURLY.intervalId) }
            .map { runStrategy(it, Strategies.MAVILIMW, "FUTURE") }
            .filter(StringUtils::isNotEmpty)
            .reduce { s1, s2 -> "- " + s1 + " \n " + s2 }
            .doOnNext { println(it) }
            .doOnNext { telegramClientService.sendMessageAsync(it) }
            .subscribe()


    }


    private fun runStrategy(it: BaseBarSeries, strategyType: Strategies, market: String): String {
        if (strategyType == Strategies.STOCH) {
            val strategy = InverseFisherTransformStochStrategy(it, 21, 9)
            return runStrategy(it, strategyType, strategy, market)
        }
        if (strategyType == Strategies.TILSONT3_MAVILIM) {
            val strategy = TilsonT3MavilimStrategy(it)
            return runStrategy(it, strategyType, strategy, market)
        }
        if (strategyType == Strategies.COMBO_1H) {
            val strategy = Combo1HStrategy(it)
            return runStrategy(it, strategyType, strategy, market)
        }
        if (strategyType == Strategies.LARGE_PIN) {
            val strategy = LargePinStrategy(it)
            return runStrategy(it, strategyType, strategy, market)
        }
        if (strategyType == Strategies.MACD_DEMA) {
            val strategy = MacdStrategy(it)
            return runStrategy(it, strategyType, strategy, market)
        }
        if (strategyType == Strategies.MAVILIMW) {
            val strategy = MavilimWStrategy(it)
            return runStrategy(it, strategyType, strategy, market)
        }
        return "";
    }

    private fun runStrategy(
        it: BaseBarSeries,
        strategyType: Strategies,
        strategy: CustomStrategy,
        market: String
    ): String {
        val ndx = it.getBarCount() - 1
        val evaluate = strategy.evaluate(ndx)
        val prefix = "*${it.name.toUpperCase()}, $strategyType ${it.lastBar.timePeriod}"
        if (evaluate == 1) {
            log.info { "$prefix ,Entry point  ${it.lastBar}" }
            return "$prefix, ${strategyType.enterMessage}, Last price: ${it.lastBar.closePrice}";
        }
        if (evaluate == -1) {
            log.info { "$prefix ,Exit point  ${it.lastBar}" }
            return "$prefix, ${strategyType.exitMessage}, Last price: ${it.lastBar.closePrice}";
        }
        return "";
    }
}