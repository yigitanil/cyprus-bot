package com.ygt.cyprusbot.service.scheduler

import com.binance.api.client.domain.market.CandlestickInterval
import com.ygt.cyprusbot.model.Strategies
import com.ygt.cyprusbot.service.BinanceClientService
import com.ygt.cyprusbot.service.TelegramClientService
import com.ygt.cyprusbot.strategy.CustomStrategy
import com.ygt.cyprusbot.strategy.MavilimWStrategy
import com.ygt.cyprusbot.strategy.TilsonT3Strategy
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
    fun runMavilim() {
        log.info { "MavilimW tracker is started" }

        binanceClientService
            .getFutureExchangeInfo()
            .flatMapMany { Flux.fromIterable(it.symbols) }
            .filter { it.contractType.equals("PERPETUAL") }
            .parallel()
            .flatMap { binanceClientService.getFutureCandlesticks(it.symbol, CandlestickInterval.HOURLY.intervalId) }
            .map { runStrategy(it, Strategies.MAVILIMW) }
            .filter(StringUtils::isNotEmpty)
            .reduce { s1, s2 -> s1 + " \n" + s2 }
            .map { "__MAVILIMW - 1H__\n" + it }
            .doOnNext { println(it) }
            .doOnNext { telegramClientService.sendMessageAsync(it) }
            .subscribe()


    }

    @Scheduled(cron = "03 0 */1 * * *")
    fun runTilsonT3() {
        log.info { "Tilson T3 tracker is started" }

        binanceClientService
            .getFutureExchangeInfo()
            .flatMapMany { Flux.fromIterable(it.symbols) }
            .filter { it.contractType.equals("PERPETUAL") }
            .parallel()
            .flatMap { binanceClientService.getFutureCandlesticks(it.symbol, CandlestickInterval.HOURLY.intervalId) }
            .map { runStrategy(it, Strategies.TILSON_T3) }
            .filter(StringUtils::isNotEmpty)
            .reduce { s1, s2 -> s1 + " \n" + s2 }
            .map { "__TILSONT3 - 1H__\n" + it }
            .doOnNext { println(it) }
            .doOnNext { telegramClientService.sendMessageAsync(it) }
            .subscribe()


    }


    private fun runStrategy(it: BaseBarSeries, strategyType: Strategies): String {
        if (strategyType == Strategies.MAVILIMW) {
            val strategy = MavilimWStrategy(it)
            return runStrategy(it, strategy)
        }
        if (strategyType == Strategies.TILSON_T3) {
            val strategy = TilsonT3Strategy(it, 0.5, 3)
            return runStrategy(it, strategy)
        }
        return "";
    }

    private fun runStrategy(
        it: BaseBarSeries,
        strategy: CustomStrategy
    ): String {
        val ndx = it.getBarCount() - 1
        val evaluate = strategy.evaluate(ndx)
        val prefix = "*$${it.name.toUpperCase().replace("_PERP", "")}*, ${it.lastBar.closePrice}"
        if (evaluate == 1) {
            log.info { "$prefix ,Entry point  ${it.lastBar}" }
            return "$prefix, **BUY**";
        }
        if (evaluate == -1) {
            log.info { "$prefix ,Exit point  ${it.lastBar}" }
            return "$prefix, *SELL*";
        }
        return "";
    }
}