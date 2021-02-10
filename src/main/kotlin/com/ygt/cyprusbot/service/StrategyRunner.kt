package com.ygt.cyprusbot.service

import com.binance.api.client.domain.event.CandlestickEvent
import com.ygt.cyprusbot.model.Strategies
import com.ygt.cyprusbot.strategy.CustomStrategy
import mu.KotlinLogging
import org.springframework.stereotype.Service
import org.ta4j.core.BaseBarSeries

@Service
class StrategyRunner(private val telegramClientService: TelegramClientService, private val strategiesFactory: StrategiesFactory) {
    private val log = KotlinLogging.logger {}

    fun run(notificationMap: HashMap<String, Boolean>, barSeries: BaseBarSeries, symbol: String, candlestickEvent: CandlestickEvent, strategies: List<Strategies>) {
        strategies.forEach {
            val strategy = strategiesFactory.get(it, barSeries, candlestickEvent.intervalId)
            run(it, strategy, notificationMap, barSeries, symbol)
        }


        if (candlestickEvent.barFinal) {
            for (value in strategies) {
                if (notificationMap.containsKey(value.name)) {
                    notificationMap.put(value.name, false)
                }
            }
        }
    }

    private fun run(strategyType: Strategies,
                    strategy: CustomStrategy,
                    notificationMap: HashMap<String, Boolean>,
                    barSeries: BaseBarSeries,
                    symbol: String) {

        if (!notificationMap.get(strategyType.name)!!) {
            val ndx = barSeries.getBarCount() - 1
            val evaluate = strategy.evaluate(ndx)
            val prefix = "${symbol.toUpperCase()}, ${barSeries.lastBar.timePeriod}"
            if (evaluate == 1) {
                log.info { "$prefix , $strategyType possible enter point ${barSeries.lastBar}" }
                telegramClientService.sendMessageAsync("$prefix, ${strategyType.enterMessage}, Last price: ${barSeries.lastBar.closePrice}")
                notificationMap.put(strategyType.name, true)
            }
            if (evaluate == -1) {
                log.info { "$prefix , $strategyType possible exit point ${barSeries.lastBar}" }
                telegramClientService.sendMessageAsync("$prefix, ${strategyType.exitMessage}, Last price: ${barSeries.lastBar.closePrice}")
                notificationMap.put(strategyType.name, true)
            }
        }

    }


}