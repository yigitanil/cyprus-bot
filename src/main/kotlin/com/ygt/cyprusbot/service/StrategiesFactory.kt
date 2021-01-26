package com.ygt.cyprusbot.service

import com.ygt.cyprusbot.model.Strategies
import com.ygt.cyprusbot.strategy.*
import org.springframework.stereotype.Service
import org.ta4j.core.BarSeries

@Service
class StrategiesFactory {

    fun get(strategy: Strategies, barSeries: BarSeries): CustomStrategy {
        if (strategy == Strategies.BOLLINGER) {
            return BollingerStrategy(barSeries)
        }
        if (strategy == Strategies.BOLLINGER_MIDDLE) {
            return BollingerMiddleStrategy(barSeries)
        }
        if (strategy == Strategies.RSI) {
            return RsiStrategy(barSeries)
        }
        if (strategy == Strategies.MACD) {
            return MacdStrategy(barSeries)
        }
        if (strategy == Strategies.LARGE_PIN) {
            return LargePinStrategy(barSeries)
        }

        throw IllegalArgumentException("Cannot get strategy: ${strategy.name}")
    }

}