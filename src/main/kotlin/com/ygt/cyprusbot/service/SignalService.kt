package com.ygt.cyprusbot.service

import com.binance.api.client.BinanceApiRestClient
import com.binance.api.client.domain.event.CandlestickEvent
import com.ygt.cyprusbot.config.BinanceWebSocketClient
import com.ygt.cyprusbot.model.RunningStrategy
import com.ygt.cyprusbot.model.Strategies
import com.ygt.cyprusbot.service.CandleStickMapper.Companion.candleStickBarToBar
import com.ygt.cyprusbot.service.CandleStickMapper.Companion.candleStickEventToBar
import mu.KotlinLogging
import org.springframework.stereotype.Service
import org.ta4j.core.BaseBar
import org.ta4j.core.BaseBarSeries
import java.util.concurrent.atomic.AtomicBoolean

@Service
class SignalService(private val strategyRunner: StrategyRunner,
                    private val restClient: BinanceApiRestClient) {
    private val log = KotlinLogging.logger {}

    fun run(runningStrategy: RunningStrategy, strategies: List<Strategies>) {
        val symbol = runningStrategy.symbol
        val interval = runningStrategy.interval
        log.info { "${symbol.toUpperCase()} with $interval interval is started" }
        val candlestickBars = restClient.getCandlestickBars(symbol.toUpperCase(), interval)
        val bars = candlestickBars.map { candleStickBarToBar(it, interval.intervalId) }
        val barSeries = BaseBarSeries(bars)

        val newBar = AtomicBoolean(false);
        val notificationMap = HashMap<String, Boolean>()
        strategies.forEach { notificationMap.put(it.name, false) }

        val onCandlestickEvent = BinanceWebSocketClient.get().onCandlestickEvent(symbol.toLowerCase(), interval) {
            try {
                val bar = candleStickEventToBar(it)
                handleNewItem(newBar, barSeries, it, bar)
                strategyRunner.run(notificationMap, barSeries, symbol, it, strategies)
            } catch (e: Exception) {
                log.error(e) { "Error on candleStickEvent" }
            }
        }
        runningStrategy.closeable = onCandlestickEvent;
    }


    private fun handleNewItem(newBar: AtomicBoolean, barSeries: BaseBarSeries, it: CandlestickEvent, bar: BaseBar?) {
        if (!newBar.get()) {
            barSeries.addPrice(it.high)
            barSeries.addPrice(it.low)
            barSeries.addPrice(it.close)
        } else {
            barSeries.addBar(bar)
            newBar.set(false);
            log.info { "Adding new bar: $bar" }
        }
        newBar.set(it.barFinal)
    }


}