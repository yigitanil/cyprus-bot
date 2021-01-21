package com.ygt.cyprusbot.service

import com.binance.api.client.domain.event.CandlestickEvent
import com.ygt.cyprusbot.model.Strategies
import com.ygt.cyprusbot.strategy.AbstractCustomStrategy
import com.ygt.cyprusbot.strategy.BollingerStrategy
import com.ygt.cyprusbot.strategy.MacdStrategy
import com.ygt.cyprusbot.strategy.RsiStrategy
import mu.KotlinLogging
import org.springframework.stereotype.Service
import org.springframework.web.reactive.function.client.WebClientRequestException
import org.ta4j.core.BaseBarSeries
import reactor.util.retry.Retry

@Service
class StrategyRunner(private val telegramClientService: TelegramClientService) {
    private val log = KotlinLogging.logger {}

     fun run(notificationMap: HashMap<String, Boolean>, barSeries: BaseBarSeries, symbol: String, it: CandlestickEvent) {
        val bollingerStrategy = BollingerStrategy(barSeries)
        val macdStrategy = MacdStrategy(barSeries)
        val rsiStrategy = RsiStrategy(barSeries)
        run(Strategies.BOLLINGER, bollingerStrategy, notificationMap, barSeries, symbol)
        run(Strategies.MACD, macdStrategy, notificationMap, barSeries, symbol)
        run(Strategies.RSI, rsiStrategy, notificationMap, barSeries, symbol)
        if (it.barFinal) {
            for (value in Strategies.values()) {
                if (notificationMap.containsKey(value.name)) {
                    notificationMap.put(value.name, false)
                }
            }
        }
    }

    private fun run(strategyType: Strategies,
                            strategy: AbstractCustomStrategy,
                            notificationMap: HashMap<String, Boolean>,
                            barSeries: BaseBarSeries,
                            symbol: String) {

        if (!notificationMap.get(strategyType.name)!!) {
            val ndx = barSeries.getBarCount() - 1
            val evaluate = strategy.evaluate(ndx)
            val prefix = "${symbol.toUpperCase()}, ${barSeries.lastBar.timePeriod}"
            if (evaluate == 1) {
                log.info { "$prefix , $strategyType possible enter point ${barSeries.lastBar}" }
                sendMessage(prefix, strategyType.enterMessage)
                notificationMap.put(strategyType.name, true)
            }
            if (evaluate == -1) {
                log.info { "$prefix , $strategyType possible exit point ${barSeries.lastBar}" }
                sendMessage(prefix, strategyType.exitMessage)
                notificationMap.put(strategyType.name, true)
            }
        }

    }

    private fun sendMessage(prefix: String, message: String) {
        telegramClientService.sendMessage("${prefix} $message")
                .retryWhen(Retry.withThrowable { it.all { t -> t::class.java == WebClientRequestException::class.java } })
                .subscribe()
    }

}